package com.example.masterproject.ui.screens.exchangerate

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.masterproject.model.entities.PairCoinWithPrice
import com.example.masterproject.model.PairCoinsRepository
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
    private val pairsCoinsRepository: PairCoinsRepository
): ViewModel() {
    private var isUpdatingData = false

    val stateFlow: StateFlow<ScreenState> = pairsCoinsRepository.getPairList()
        .map (ScreenState::Success)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = ScreenState.Loading,
        )


    sealed class ScreenState{
        data object Loading : ScreenState()
        data class Success(val pairCoins: List<PairCoinWithPrice>): ScreenState()
    }

    fun startUpdatingCoinPrice() {
        isUpdatingData = true
        viewModelScope.launch(Dispatchers.IO) {
            while (isUpdatingData){
                pairsCoinsRepository.updateCoinPrice()
                delay(2000L)
                Log.d("Start123", "aaaaaaaaa")
            }
        }
    }

    fun stopUpDatingCoinPrice(){
        isUpdatingData = false
        pairsCoinsRepository.resetToDefaultPrice()
    }
}
