package com.example.masterproject.ui.screens.mainscreen

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.masterproject.App
import com.example.masterproject.model.marketpair.MarketPairRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    //private val marketPairRepository: MarketPairRepository,
): ViewModel()
