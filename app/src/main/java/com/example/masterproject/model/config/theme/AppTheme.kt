package com.example.masterproject.model.config.theme
import androidx.compose.ui.graphics.Color
import com.example.masterproject.ui.theme.TextColorUpperThanDP
import com.example.masterproject.ui.theme.bgColorDark
import com.example.masterproject.ui.theme.bgColorLight
import com.example.masterproject.ui.theme.primaryColorDark
import com.example.masterproject.ui.theme.primaryColorLight
import com.example.masterproject.ui.theme.secondaryColorDark
import com.example.masterproject.ui.theme.secondaryColorLight
import com.example.masterproject.ui.theme.textColorDark
import com.example.masterproject.ui.theme.textColorLight

data class AppTheme(
    val primaryColor: Color,
    val secondaryColor: Color,
    val bgColor: Color,
    val textColor: Color,
) {
    companion object{
        val Light = AppTheme(
            primaryColor = primaryColorLight,
            secondaryColor = secondaryColorLight,
            bgColor = bgColorLight,
            textColor = textColorLight
        )

        val Dark = AppTheme(
            primaryColor = primaryColorDark,
            secondaryColor = secondaryColorDark,
            bgColor = bgColorDark,
            textColor = textColorDark
        )
    }
}

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