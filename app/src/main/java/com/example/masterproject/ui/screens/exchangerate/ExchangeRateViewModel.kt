package com.example.masterproject.ui.screens.exchangerate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.masterproject.model.marketpair.MarketPairRepository
import com.example.masterproject.model.marketpair.entities.MarketPairWithDetails
import com.example.masterproject.model.marketsnapshot.MarketSnapshotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeRateViewModel @Inject constructor(
    private val marketPairRepository: MarketPairRepository,
    private val marketSnapshotRepository: MarketSnapshotRepository
): ViewModel() {
    private var isUpdatingData = false

    val comparedPrice: MutableStateFlow<Map<Long, Boolean>> = MutableStateFlow(mapOf())

    val marketPairsFlow: StateFlow<ScreenState> = marketPairRepository.getMarketPairWithDetailsList()
        .map {
            if (it != null) {
                comparedPrice.value = compareWithStartPrice()
                ScreenState.Success(it)
            }
            else
                ScreenState.Loading
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = ScreenState.Loading,
        )


    sealed class ScreenState{
        data object Loading : ScreenState()
        data class Success(val pairCoins: List<MarketPairWithDetails>): ScreenState()
    }

    fun startUpdatingCoinPrice() {
        if(isUpdatingData) return
        isUpdatingData = true
        viewModelScope.launch(Dispatchers.IO) {
            while (isUpdatingData){
                marketPairRepository.updateListWithDetails()

                delay(2000L)
            }
        }
    }

    fun stopUpDatingCoinPrice(){
        isUpdatingData = false
        marketPairRepository.resetToDefaultPrice()
    }

    fun saveMarketSnapshot(){
        viewModelScope.launch(Dispatchers.IO) {
            marketSnapshotRepository.saveMarketSnapshot(
                listWithDetails = marketPairRepository.getMarketPairWithDetailsList().first(),
                listMarketPair = marketPairRepository.getMarketPair().first()
            )
        }
    }

    private fun compareWithStartPrice(): Map<Long, Boolean>{
       return marketPairRepository.comparePriceUpperThanStartDay()
    }

}
