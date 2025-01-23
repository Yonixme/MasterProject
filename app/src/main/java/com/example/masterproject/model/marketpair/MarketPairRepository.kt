package com.example.masterproject.model.marketpair

import com.example.masterproject.model.marketpair.database.DBMarketPairRepository
import com.example.masterproject.model.marketpair.entities.MarketPair
import com.example.masterproject.model.marketpair.entities.MarketPairWithDetails
import com.example.masterproject.model.marketsnapshot.database.DBMarketSnapshotRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarketPairRepository @Inject constructor(
    private val dbMarketPairRepository: DBMarketPairRepository,
    private val symbolPriceSource: SymbolPriceSource
){
    private val _listMarketPairs: MutableStateFlow<List<MarketPair>?> = MutableStateFlow(null)
    private val _listMarketPairDetails: MutableStateFlow<List<MarketPairWithDetails>?> = MutableStateFlow(null)
    private var _listMarketPriceOnStartDay: MutableStateFlow<List<MarketPairWithDetails>?> = MutableStateFlow(null)

    private val customScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        val listFromDb = dbMarketPairRepository.getAllMarketPairs()
        customScope.launch{
            listFromDb.collect{
                    list ->
                val mappedList: List<MarketPair> = list.mapNotNull {
                    it
                }

                _listMarketPairs.value = mappedList

                _listMarketPriceOnStartDay.value = mappedList.map {
                        pairCoin ->
                    val price = getPriceAtTheStartDay(pairCoin.tradePair).get(pairCoin.tradePair) ?: -1.0
                    MarketPairWithDetails(
                        id = pairCoin.id,
                        tradePair = pairCoin.tradePair,
                        sourceName = pairCoin.sourceName,
                        price = price
                    )
                }

                val prices = getMarketPrice()
                _listMarketPairDetails.value = mappedList.map{
                        pairCoin ->
                    val price = prices[pairCoin.tradePair] ?: -1.0
                    MarketPairWithDetails(
                        id = pairCoin.id,
                        tradePair = pairCoin.tradePair,
                        sourceName = pairCoin.sourceName,
                        price = price
                    )
                }
            }
        }
    }

    suspend fun changeIgnoreFlagForMarketPair(id: Long){
        val marketPair = _listMarketPairs.value?.filter { id == it.id }
        if (!marketPair.isNullOrEmpty()) {
            dbMarketPairRepository.setIgnoreSavingFlagForMarketPair(marketPair[0])
        }
    }

    suspend fun clearTable(){
        dbMarketPairRepository.clearTable()
    }

    //Delete pair from DB
    suspend fun deletePairFromList(id: Long){
        _listMarketPairDetails.value = null
        dbMarketPairRepository.deleteMarketPair(id)
    }

    //Add pair in DB
    suspend fun addPairInList(pair: String, sourceName: String){
        dbMarketPairRepository.addMarketPair(MarketPair(
            0,
            tradePair = pair,
            sourceName = sourceName,
            ignoreWhenSaving = false
        ))
        _listMarketPairDetails.value = null
    }

    fun getMarketPairWithDetailsList(): Flow<List<MarketPairWithDetails>?> {
        return _listMarketPairDetails
    }

    fun getMarketPairAtTheStartDay(): Flow<List<MarketPairWithDetails>?> {
        return _listMarketPriceOnStartDay
    }

    fun getMarketPair(): Flow<List<MarketPair>?> {
        return _listMarketPairs
    }

    fun getIdForPair(pair: String): Long{
        val listMarketPairDetailsCoin = _listMarketPairDetails.value?.filter { it.tradePair == pair }

        return if (!listMarketPairDetailsCoin.isNullOrEmpty()) listMarketPairDetailsCoin[0].id else -1
    }

    //Reset price value to -1
    fun resetToDefaultPrice(){
        _listMarketPairDetails.update { currentList ->
            currentList?.map { pairCoin ->
                pairCoin.copy(price = -1.0)
            }
        }
    }

    private suspend fun getMarketPrice(): Map<String, Double>{
        val pairs = _listMarketPairs.value?.map { it.tradePair } ?: emptyList()
        if (pairs.isEmpty()) return mapOf()
        val listPrice: List<Double> = try {
            symbolPriceSource.getSymbolPrice(pairs).values.toList()
        }catch (e: Exception){
            List<Double>(pairs.size){ -1.0 }
        }
        val symbolToPrice = mutableMapOf<String, Double>()
        for (symbol in pairs){
            symbolToPrice[symbol] = listPrice.get(pairs.indexOfFirst { it == symbol })
        }
        return symbolToPrice.toMap()
    }

    private suspend fun getPriceAtTheStartDay(symbol: String): Map<String, Double>{
        val price = try {
            symbolPriceSource.getPriceAtTheStartDay(symbol).get(symbol) ?: -1.0
        }catch (e: Exception){
            -1.0
        }
        return mapOf(symbol to price)
    }

    // Fetch all prices concurrently and update the state
    fun updateListWithDetails(){
        customScope.launch {
            if (_listMarketPriceOnStartDay.value.isNullOrEmpty()) return@launch

            val listWithDefaultPriceNotInit: Boolean =
                _listMarketPriceOnStartDay.value!!.filter { it.price == -1.0 }.isNotEmpty()

            val prices = getMarketPrice()

            if (listWithDefaultPriceNotInit){
                resetToDefaultPrice()
                _listMarketPriceOnStartDay.update { currentList ->
                    currentList?.map { pairCoin ->
                        var price = pairCoin.price
                        if (price < 0 ) {
                            price = getPriceAtTheStartDay(pairCoin.tradePair).get(pairCoin.tradePair) ?: -1.0
                        }
                        MarketPairWithDetails(
                            id = pairCoin.id,
                            tradePair = pairCoin.tradePair,
                            sourceName = pairCoin.sourceName,
                            price = price
                        )
                    }
                }
                _listMarketPairDetails.value = _listMarketPairs.first()?.map{
                    val price = prices[it.tradePair] ?: -1.0
                    MarketPairWithDetails(
                        id = it.id,
                        tradePair = it.tradePair,
                        sourceName = it.sourceName,
                        price = price
                    )
                }
            }else{
                _listMarketPairDetails.update { currentList ->
                    currentList?.map { pairCoin ->
                        val updatedPrice = prices[pairCoin.tradePair] ?: pairCoin.price
                        pairCoin.copy(price = updatedPrice)
                    }
                }
            }
        }
    }
}