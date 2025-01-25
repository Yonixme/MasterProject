package com.example.masterproject.model.config.colormode

import kotlinx.coroutines.flow.StateFlow

interface ColorModeDataSource {

    /**
     * Observe the current app color mode.
     * This flow emits a mode after updating it by [setMode] call
     */
    val modeStateFlow: StateFlow<ColorMode>

    /**
     * Update the current app color mode
     */
    fun setMode(mode: ColorMode)
}