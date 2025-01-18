package com.example.masterproject.model

import com.example.masterproject.model.database.DBRepositories
import com.example.masterproject.model.marketpair.entities.MarketPair
import com.example.masterproject.model.marketpair.entities.MarketPairWithDetails
import com.example.masterproject.ui.tools.sourceNames
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random

@Singleton
class MarketPairRepository @Inject constructor(
    private val dbRepositories: DBRepositories
){
    private var _listMarketPairDetails: MutableStateFlow<List<MarketPairWithDetails>> = MutableStateFlow(
        emptyList()
    )
    private val customScope = CoroutineScope(Dispatchers.IO + SupervisorJob())


    init {
        val listFromDb = dbRepositories.roomMarketPairRepository.getAllMarketPairs()

        customScope.launch{
            listFromDb.collect{
                    list ->
                _listMarketPairDetails.value = list.map {
                    val marketPair = it?.toMarketPair() ?:
                    MarketPair(id = 0, tradePair = "Not Found", sourceName = "Not Found")

                    MarketPairWithDetails(
                        id = marketPair.id,
                        tradePair = marketPair.tradePair,
                        sourceName = marketPair.sourceName,
                        price = -1.0 // Default price
                    )
                }
            }
        }
    }

    suspend fun saveMarketSnapshot(){
        val list = _listMarketPairDetails.first()
        dbRepositories.roomMarketSnapshotRepository.createSnapshotWithDetails(list = list)

//        val id = dbRepositories.roomMarketSnapshotRepository.createSnapshot()
//        for (marketPairWithDetails in list){
//            dbRepositories.roomMarketSnapshotRepository.createMarketSnapshotAndDetails(
//                id,
//                marketPairWithDetails = marketPairWithDetails
//            )
//        }
    }

    suspend fun deletePairFromList(id: Long){
        //Delete pair from DB
        dbRepositories.roomMarketPairRepository.deleteMarketPair(id)
    }

    suspend fun addPairInList(pair: String){
        //Add pair in DB
        val sourceName = "Binance"
        dbRepositories.roomMarketPairRepository.AddMarketPair(MarketPair(0, tradePair = pair, sourceName))
    }

    fun getMarketPairWithDetailsList(): Flow<List<MarketPairWithDetails>> = _listMarketPairDetails

    //fun getMarketPairList(): Flow<List<MarketPairWithDetails>> = dbRepositories.roomMarketPairRepository.getAllMarketPairs()

    fun getIdForPair(pair: String): Long{
        val listMarketPairDetailsCoin = _listMarketPairDetails.value.filter { it.tradePair == pair }

        return if (listMarketPairDetailsCoin.isNotEmpty()) listMarketPairDetailsCoin[0].id else -1
    }



    suspend fun getInfoForPair(id: Long, time: String){
        //TODO Get info for pair from API dependency timeline


    }

    private fun getRandomSourceName(): String{
        val list = sourceNames

        return list[Random.nextInt(list.size)]
    }

    fun resetToDefaultPrice(){
        //Reset price value to -1

        _listMarketPairDetails.update { currentList ->
            currentList.map { pairCoin ->
                val updatedPrice: Double = -1.0

                pairCoin.copy(price = updatedPrice)
            }
        }
    }

    private suspend fun getPriceForPairs(): Map<String, Double>{
        return suspendCoroutine {continuation ->
            val pairs = _listMarketPairDetails.value.map { it.tradePair }

            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://api.binance.com/api/v3/ticker/price")
                .build()

            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    println("Error 123: ${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        response.use {
                            if (!response.isSuccessful){
                                return
                            }
                            val responseBody = response.body()?.string()
                            val jsonArray = JSONArray(responseBody)
                            val pricesMap = mutableMapOf<String, Double>()

                            for(i in 0 until jsonArray.length()){
                                val obj = jsonArray.getJSONObject(i)
                                val symbol = obj.getString("symbol")
                                val price = obj.getString("price").toDouble()
                                pricesMap[symbol] = price
                            }
                            val filteredPrices = pricesMap.filterKeys { it in pairs }
                            continuation.resumeWith(Result.success(filteredPrices))
                        }
                    }catch (e: Exception){
                        println("Error parsing response: ${e.message}")
                    }
                }
            })
        }
    }

    fun updateMarketPairDetails(){
        // Fetch all prices concurrently and update the state
        customScope.launch {
            try {
                println("On updating")
                val updatedPrices = getPriceForPairs()

                _listMarketPairDetails.update { currentList ->
                    currentList.map { pairCoin ->
                        val updatedPrice = updatedPrices[pairCoin.tradePair] ?: pairCoin.price

                        pairCoin.copy(price = updatedPrice)
                    }
                }
            } catch (e: Exception) {
                println("Error updating coin prices: ${e.message}")
            }
        }
    }
}

