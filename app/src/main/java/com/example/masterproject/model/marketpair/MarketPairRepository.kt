package com.example.masterproject.model.marketpair

import com.example.masterproject.model.database.DBRepositories
import com.example.masterproject.model.marketpair.entities.MarketPair
import com.example.masterproject.model.marketpair.entities.MarketPairWithDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarketPairRepository @Inject constructor(
    private val dbRepositories: DBRepositories,
    private val symbolPriceSource: SymbolPriceSource
){
    private val _listMarketPairDetails: MutableStateFlow<List<MarketPairWithDetails>> = MutableStateFlow(
        emptyList()
    )
    private var _listMarketPriceOnStartDay: MutableStateFlow<List<MarketPairWithDetails>> = MutableStateFlow(
        emptyList()
    )

    private val customScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        val listFromDb = dbRepositories.roomMarketPairRepository.getAllMarketPairs()
        customScope.launch{
            listFromDb.collect{
                    list ->
                val mappedList = list.map {
                    val marketPair = it ?:
                    MarketPair(id = 0, tradePair = "Not Found", sourceName = "Not Found")
                    MarketPairWithDetails(
                        id = marketPair.id,
                        tradePair = marketPair.tradePair,
                        sourceName = marketPair.sourceName,
                        price = -1.0 // Default price
                    )
                }
                _listMarketPairDetails.value = mappedList
                _listMarketPriceOnStartDay.value = mappedList
                val d = symbolPriceSource.getPriceAtTheStartDay("BTCUSDT")
            }
        }
    }

    suspend fun saveMarketSnapshot(){
        val list = _listMarketPairDetails.value
        if(list.isEmpty()) return
        dbRepositories.roomMarketSnapshotRepository.createSnapshotWithDetails(list = list)
    }

    //Delete pair from DB
    suspend fun deletePairFromList(id: Long){
        dbRepositories.roomMarketPairRepository.deleteMarketPair(id)
    }

    //Add pair in DB
    suspend fun addPairInList(pair: String, sourceName: String){
        dbRepositories.roomMarketPairRepository.AddMarketPair(MarketPair(0,
            tradePair = pair,
            sourceName = sourceName))
    }

    fun getMarketPairWithDetailsList(): Flow<List<MarketPairWithDetails>> = _listMarketPairDetails

    fun getIdForPair(pair: String): Long{
        val listMarketPairDetailsCoin = _listMarketPairDetails.value.filter { it.tradePair == pair }

        return if (listMarketPairDetailsCoin.isNotEmpty()) listMarketPairDetailsCoin[0].id else -1
    }

    //TODO Get info for pair from API dependency timeline
    suspend fun getInfoForPair(id: Long, time: String){

    }

    //Reset price value to -1
    fun resetToDefaultPrice(){
        _listMarketPairDetails.update { currentList ->
            currentList.map { pairCoin ->
                pairCoin.copy(price = -1.0)
            }
        }
    }

    private suspend fun getMarketPrice(): Map<String, Double>{
        val pairs = _listMarketPairDetails.value.map { it.tradePair }

        return symbolPriceSource.getSymbolPrice(pairs)
    }

    // Fetch all prices concurrently and update the state
    fun updateMarketPairDetails(){
        customScope.launch {
            try {
                val prices = getMarketPrice()
                _listMarketPairDetails.update { currentList ->
                    currentList.map { pairCoin ->
                        val updatedPrice = prices[pairCoin.tradePair] ?: pairCoin.price
                        pairCoin.copy(price = updatedPrice)
                    }
                }
            } catch (e: Exception) {
                println("Error updating coin prices: ${e.message}")
            }
        }
    }


//    private suspend fun getPriceOnStartDay(): Map<String, Double?> = coroutineScope {
//        val listMarketPairs = _listMarketPairDetails.single()
//
//        val listPairs = listMarketPairs.map { it.tradePair }
//
//        val baseUrl = "https://api.binance.com/api/v3/klines"
//        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//        val startOfDay = dateFormatter.parse(dateFormatter.format(Date()))?.time
//
//        listPairs.map { pair ->
//            async {
//                try {
//                    val url = URL("$baseUrl?symbol=$pair&interval=1d&startTime=$startOfDay&limit=1")
//                    with(url.openConnection() as HttpURLConnection) {
//                        requestMethod = "GET"
//                        inputStream.bufferedReader().use { reader ->
//                            val response = JsonParser.parseString(reader.readText()).asJsonArray
//                            val klineData = response.firstOrNull()?.asJsonArray
//                            val openingPrice = klineData?.get(1)?.asString?.toDouble()
//                            pair to openingPrice
//                        }
//                    }
//                } catch (e: Exception) {
//                    println("Error fetching opening price for $pair: ${e.message}")
//                    pair to 0.0 // У випадку помилки повертаємо 0.0
//                }
//            }
//        }.awaitAll().toMap()
//    }

}

