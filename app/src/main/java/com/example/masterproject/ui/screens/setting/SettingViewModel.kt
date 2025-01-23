package com.example.masterproject.ui.screens.setting

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.masterproject.model.database.DatabaseConfig
import com.example.masterproject.model.marketpair.MarketPairRepository
import com.example.masterproject.model.marketsnapshot.MarketSnapshotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val marketSnapshotRepository: MarketSnapshotRepository,
    private val marketPairRepository: MarketPairRepository
) : ViewModel(){

    fun resetDatabase(){
        viewModelScope.launch {
            marketSnapshotRepository.clearTable()
            marketPairRepository.clearTable()
        }
    }
}