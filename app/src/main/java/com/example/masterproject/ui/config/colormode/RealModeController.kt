package com.example.masterproject.ui.config.colormode

import com.example.masterproject.model.config.colormode.ColorMode
import com.example.masterproject.model.config.colormode.ColorModeDataSource

class RealModeController(
    private val modeDataSource: ColorModeDataSource
): AppModeController {
    override fun toggle() {
        val currentMode = modeDataSource.modeStateFlow.value
        if (currentMode == ColorMode.DisEnabled){
            modeDataSource.setMode(ColorMode.Enabled)
        }else{
            modeDataSource.setMode(ColorMode.DisEnabled)
        }
    }
}