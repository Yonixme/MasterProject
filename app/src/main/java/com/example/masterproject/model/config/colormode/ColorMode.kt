package com.example.masterproject.model.config.colormode

import androidx.compose.ui.graphics.Color
import com.example.masterproject.ui.theme.TextColorUpperThanDPLight
import com.example.masterproject.ui.theme.textColorDark

data class ColorMode(
    val colorModeEnabled: Boolean
){

    companion object{
        val Enabled = ColorMode(
            colorModeEnabled = true
        )

        val DisEnabled = ColorMode(
            colorModeEnabled = false
        )
    }
}