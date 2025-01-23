package com.example.masterproject.model.config.colormode

import androidx.compose.ui.graphics.Color
import com.example.masterproject.ui.theme.TextColorUpperThanDP
import com.example.masterproject.ui.theme.textColorDark

data class ColorMode(
    val upperTextColor: Color,
    val lowerTextColor: Color
){
    companion object{
        val Enabled = ColorMode(
            upperTextColor = TextColorUpperThanDP,
            lowerTextColor = TextColorUpperThanDP
        )

        val DisEnabled = ColorMode(
            upperTextColor = textColorDark,
            lowerTextColor = textColorDark
        )
    }
}