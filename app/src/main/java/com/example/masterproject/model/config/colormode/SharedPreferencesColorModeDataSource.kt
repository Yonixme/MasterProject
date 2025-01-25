package com.example.masterproject.model.config.colormode

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow

class SharedPreferencesColorModeDataSource(
    context: Context): ColorModeDataSource
{
    private val preferences = context.getSharedPreferences(
        MODE_NAME, Context.MODE_PRIVATE)

    override val modeStateFlow: MutableStateFlow<ColorMode> = MutableStateFlow(readMode())

    override fun setMode(mode: ColorMode) {
        preferences.edit()
            .putBoolean(KEY_MODE_ENABLE, mode.colorModeEnabled)
            .apply()
        modeStateFlow.value = mode
    }

    private fun hasSavedMode(): Boolean = preferences.contains(KEY_MODE_ENABLE)

    private fun readMode(): ColorMode{
        if (!hasSavedMode()) return ColorMode.DisEnabled
        val mode = ColorMode(
            colorModeEnabled = preferences.getBoolean(KEY_MODE_ENABLE, false)
        )
        return mode
    }

    private companion object{
        const val MODE_NAME = "Mode name"
        const val KEY_MODE_ENABLE = "Mode Enable"
    }
}