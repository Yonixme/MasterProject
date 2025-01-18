package com.example.masterproject.ui.screens.exchangerate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.masterproject.model.MarketPairRepository
import com.example.masterproject.model.marketpair.entities.MarketPairWithDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeRateViewModel @Inject constructor(
    private val marketPairRepository: MarketPairRepository,
): ViewModel() {
    private var isUpdatingData = false

    val stateFlow: StateFlow<ScreenState> = marketPairRepository.getMarketPairWithDetailsList()
        .map (ScreenState::Success)
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
                marketPairRepository.updateMarketPairDetails()

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
            marketPairRepository.saveMarketSnapshot()
        }

    }
}
