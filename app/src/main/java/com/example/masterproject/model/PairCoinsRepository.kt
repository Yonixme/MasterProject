package com.example.masterproject.model

import android.content.Context
import com.example.masterproject.screens.tools.baseSecondCoin
import com.example.masterproject.screens.tools.cryptoCoins
import com.example.masterproject.screens.tools.sourceNames
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
class PairCoinsRepository @Inject constructor(
    @ApplicationContext private val context: Context
){
    private val _listCoins: MutableStateFlow<List<PairCoin>> = MutableStateFlow(
        List(cryptoCoins.size) { index ->
            val coinList = cryptoCoins
            val secondCoin = baseSecondCoin
            val pair = unitCoinInPair(coinList[index], secondCoin)
            PairCoin(
                id = index.toLong() + 1,
                pair = pair,
                sourceName = getRandomSourceName(),
                price = -1.0 // Default price
            )
        }
    )

    fun getPairList(): StateFlow<List<PairCoin>> = _listCoins

    suspend fun removePairFromList(id: Long){
        //TODO Remove pair from DB

        val pairCoin = _listCoins.value.filter { it.id == id }

        _listCoins.update { it - pairCoin.toSet() }
    }

    suspend fun addPairInList(pair: String){
        //TODO Add pair in DB

        val id = _listCoins.value.size.toLong()
        val sourceName = "Binance"
        _listCoins.update { it + PairCoin(id = id, pair = pair, sourceName = sourceName, price = -1.0) }
    }

    suspend fun getInfoForPair(id: Long, time:Int){
        //TODO Get info for pair from API dependency timeline


    }

    private fun getRandomSourceName(): String{
        val list = sourceNames

        return list[Random.nextInt(list.size)]
    }

    private fun unitCoinInPair(coin1: String, coin2: String): String{
        return coin1+coin2
    }

    fun resetToDefaultPrice(){
        //TODO Reset price value to -1

        _listCoins.update { currentList ->
            currentList.map { pairCoin ->
                val updatedPrice: Double = -1.0

                pairCoin.copy(price = updatedPrice)
            }
        }
    }

    private suspend fun getPriceForPairs(): Map<String, Double>{
        return suspendCoroutine {continuation ->
            val pairs = _listCoins.value.map { it.pair }

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

    fun updateCoinPrice(){
        // Fetch all prices concurrently and update the state
        CoroutineScope(Dispatchers.IO).launch {
            try {
                println("On updating")
                val updatedPrices = getPriceForPairs()

                _listCoins.update { currentList ->
                    currentList.map { pairCoin ->
                        val updatedPrice = updatedPrices[pairCoin.pair] ?: pairCoin.price

                        pairCoin.copy(price = updatedPrice)
                    }
                }
            } catch (e: Exception) {
                println("Error updating coin prices: ${e.message}")
            }
        }
    }
}

