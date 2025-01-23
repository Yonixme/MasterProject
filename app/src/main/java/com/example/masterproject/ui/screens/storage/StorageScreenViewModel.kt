package com.example.masterproject.ui.screens.storage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.masterproject.model.marketsnapshot.MarketSnapshotRepository
import com.example.masterproject.model.marketsnapshot.entities.MarketSnapshotAndDetails
import com.example.masterproject.ui.screens.exchangerate.ExchangeRateViewModel.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StorageScreenViewModel @Inject constructor(
    private val marketSnapshotRepository: MarketSnapshotRepository,
): ViewModel(){

    val stateFlow: StateFlow<ScreenState> = marketSnapshotRepository.geListMarketSnapshotDetails()
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

    fun deleteSnapshotById(id: Long){
        viewModelScope.launch {
            marketSnapshotRepository.deleteSnapshotById(id)
        }
    }

    sealed class ScreenState{
        data object Loading : ScreenState()
        data class Success(val marketSnapshots: List<MarketSnapshotAndDetails>): ScreenState()
    }
}