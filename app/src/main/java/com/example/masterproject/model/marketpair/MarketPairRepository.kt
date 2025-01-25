package com.example.masterproject.model.marketpair

import androidx.compose.ui.res.stringArrayResource
import com.example.masterproject.model.marketpair.database.DBMarketPairRepository
import com.example.masterproject.model.marketpair.entities.MarketPair
import com.example.masterproject.model.marketpair.entities.MarketPairWithDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
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

                val pricesForListOnStartDay: MutableMap<String, Double> = mutableMapOf()
                if (_listMarketPriceOnStartDay.value != null){
                    val listAtTheStartDay = _listMarketPriceOnStartDay.first() ?: emptyList()
                    val listOfUniqueElements = findUniqueElements(
                        primaryList = listAtTheStartDay,
                        keyList = mappedList,
                        comparator = {marketPair, marketPairWithDetails ->
                            marketPair.tradePair == marketPairWithDetails.tradePair &&
                                    marketPair.sourceName == marketPairWithDetails.sourceName
                        })
                    var listDeleteElements: List<MarketPairWithDetails> = emptyList()
                    for(item in listOfUniqueElements.second.toList()){
                        listDeleteElements = listAtTheStartDay.filter{ it.id == item.id }
                    }
                    _listMarketPriceOnStartDay.update { it?.filterNot { item -> item in listDeleteElements.toSet() } }

                    listOfUniqueElements.first.toList().forEach {
                        pricesForListOnStartDay[it.tradePair] = getPriceAtTheStartDay(it.tradePair).get(it.tradePair) ?: -1.0
                    }
                    _listMarketPriceOnStartDay.update {
                            currentList ->
                        currentList?.map { pairCoin ->
                            val updatedPrice = pricesForListOnStartDay[pairCoin.tradePair] ?: pairCoin.price
                            pairCoin.copy(price = updatedPrice)
                        }
                    }
                }else{
                    coroutineScope {
                        mappedList.forEach {
                            launch { pricesForListOnStartDay[it.tradePair] = getPriceAtTheStartDay(it.tradePair).get(it.tradePair) ?: -1.0 }
                        }
                    }

                    _listMarketPriceOnStartDay.value = mappedList.map {
                            pairCoin ->
                        val price = pricesForListOnStartDay[pairCoin.tradePair] ?: -1.0
                        pairCoin.toMarketPairWithDetails(price = price)
                    }
                }

                val prices: Map<String, Double>
                if (_listMarketPairDetails.value != null){
                    val listMPWithDetails = _listMarketPairDetails.first() ?: emptyList()

                    val listOfUniqueElements = findUniqueElements(
                        primaryList = listMPWithDetails,
                        keyList = mappedList,
                        comparator = {marketPair, marketPairWithDetails ->
                            marketPair.tradePair == marketPairWithDetails.tradePair &&
                                    marketPair.sourceName == marketPairWithDetails.sourceName
                        })

                    if (listOfUniqueElements.first.toList().isEmpty() &&
                        listOfUniqueElements.second.toList().isEmpty()){
                        prices = getMarketPrice(true)
                    }
                }else{
                    prices = getMarketPrice()
                    _listMarketPairDetails.value = mappedList.map{
                            pairCoin ->
                        val price = prices[pairCoin.tradePair] ?: -1.0
                        pairCoin.toMarketPairWithDetails(price = price)
                    }
                }
            }
        }
    }

    suspend fun changeIgnoreFlagForMarketPair(id: Long){
        val marketPair = _listMarketPairs.value?.filter { id == it.id }
        println("Debug1234 ${marketPair?.size}")

        if (!marketPair.isNullOrEmpty()) {
            val newIgnoreValue = !marketPair[0].ignoreWhenSaving
            val neededMarketPair = marketPair[0].copy(ignoreWhenSaving = newIgnoreValue)
            dbMarketPairRepository.setIgnoreSavingFlagForMarketPair(neededMarketPair)
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

    fun comparePriceUpperThanStartDay(): Map<Long, Boolean>{
        val currentList = _listMarketPairDetails.value ?: emptyList()
        val atTheStartDay = _listMarketPriceOnStartDay.value ?: emptyList()
        if (currentList.isEmpty() || atTheStartDay.isEmpty()) return mapOf()

        val currentPriceMap: MutableMap<Long, Double> = mutableMapOf()
        currentList.forEach(){
            currentPriceMap[it.id] = it.price
        }

        val atTheStartDayPriceMap: MutableMap<Long, Double> = mutableMapOf()
            atTheStartDay.forEach {
                atTheStartDayPriceMap[it.id] = it.price
            }

        val comparedPriceMap: MutableMap<Long, Boolean> = mutableMapOf()
        currentPriceMap.forEach {id, price ->
            comparedPriceMap[id] = price > (atTheStartDayPriceMap[id] ?: -1.0)
        }
        return comparedPriceMap
    }

    private suspend fun getMarketPrice(getDefaultPrice: Boolean = false): Map<String, Double>{
        val pairs = _listMarketPairs.value?.map { it.tradePair } ?: emptyList()
        val symbolToPrice = mutableMapOf<String, Double>()
        if (pairs.isEmpty()) return mapOf()

        if (getDefaultPrice){
            for (symbol in pairs){
                symbolToPrice[symbol] = -1.0
            }
        } else {
            val listPrice: List<Double> = try {
                symbolPriceSource.getSymbolPrice(pairs).values.toList()
            } catch (e: Exception) {
                List<Double>(pairs.size) { -1.0 }
            }
            for (symbol in pairs) {
                symbolToPrice[symbol] = listPrice.get(pairs.indexOfFirst { it == symbol })
            }
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
                        pairCoin.copy(price = price)
                    }
                }
                _listMarketPairDetails.value = _listMarketPairs.first()?.map{
                    val price = prices[it.tradePair] ?: -1.0
                    it.toMarketPairWithDetails(price = price)
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

    private fun findUniqueElements(
        primaryList: List<MarketPairWithDetails>,
        keyList: List<MarketPair>,
        comparator: (MarketPair, MarketPairWithDetails) -> Boolean
    ): Pair<List<MarketPair>, List<MarketPair>> {
        val addList: List<MarketPair> =
            keyList.filter { item1 ->
                primaryList.none { item2 ->
                    comparator(item1, item2) } }

        val deleteHelp: List<MarketPairWithDetails> =
            primaryList.filterNot { item1 ->
                keyList.any{ item2 -> comparator(item2, item1) } }

        val deleteList = deleteHelp.map {
            it.toMarketPair()
        }
        return addList to deleteList
    }
}