package com.example.masterproject.ui.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.masterproject.R
import kotlinx.collections.immutable.persistentListOf

data class AppTab(
    @DrawableRes val iconRes: Int,
    @StringRes val labelRes: Int,
    val graph: Any
)

val MainTabs = persistentListOf(
    AppTab(
        iconRes = R.drawable.home_icon_vector,
        labelRes = R.string.main_screen,
        graph = MainGraph
    ),
    AppTab(
        iconRes = R.drawable.histogram_icon,
        labelRes = R.string.exchange_rate_short,
        graph = ExchangeRateGraphs
    ),
    AppTab(
        iconRes = R.drawable.storage_icon,
        labelRes = R.string.storage,
        graph = StrorageGraphs
    ),
    AppTab(
        iconRes = R.drawable.setting_icon,
        labelRes = R.string.app_setting,
        graph = AppSettingGraphs
    ),
)
