package com.example.masterproject.ui.screens.setting

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.masterproject.model.marketpair.MarketPairRepository
import com.example.masterproject.model.marketpair.entities.MarketPair
import com.example.masterproject.model.marketsnapshot.MarketSnapshotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val marketSnapshotRepository: MarketSnapshotRepository,
    private val marketPairRepository: MarketPairRepository
) : ViewModel(){

    val stateFlow: StateFlow<ScreenState> = marketPairRepository.getMarketPair()
        .map {
            if (it != null)
                ScreenState.Success(it)
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
        data class Success(val pairCoins: List<MarketPair>): ScreenState()
    }

    fun changeFlagForMarketPair(id: Long){
        viewModelScope.launch(Dispatchers.IO) {
            marketPairRepository.changeIgnoreFlagForMarketPair(id)
        }
    }



    fun resetDatabase(){
        viewModelScope.launch {
            marketSnapshotRepository.clearTable()
            marketPairRepository.clearTable()
        }
    }


}