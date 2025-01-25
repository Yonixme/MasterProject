package com.example.masterproject.model.config.theme
import androidx.compose.ui.graphics.Color
import com.example.masterproject.ui.theme.TextColorLowerThanDPDark
import com.example.masterproject.ui.theme.TextColorLowerThanDPLight
import com.example.masterproject.ui.theme.TextColorUpperThanDPDark
import com.example.masterproject.ui.theme.TextColorUpperThanDPLight
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
    val upperTextColor: Color,
    val lowerTextColor: Color
) {

    fun getUpperAndLowerPriceByEnabledMode(enabledMode: Boolean): Pair<Color, Color> {
        return if (enabledMode) Pair(upperTextColor, lowerTextColor)
        else Pair(textColor, textColor)
    }

    companion object{
        val Light = AppTheme(
            primaryColor = primaryColorLight,
            secondaryColor = secondaryColorLight,
            bgColor = bgColorLight,
            textColor = textColorLight,
            upperTextColor = TextColorUpperThanDPLight,
            lowerTextColor = TextColorLowerThanDPLight,

        )

        val Dark = AppTheme(
            primaryColor = primaryColorDark,
            secondaryColor = secondaryColorDark,
            bgColor = bgColorDark,
            textColor = textColorDark,
            upperTextColor = TextColorUpperThanDPDark,
            lowerTextColor = TextColorLowerThanDPDark,
        )
    }
}

//data class ColorMode(
//    val upperTextColor: Color,
//    val lowerTextColor: Color
//){
//    companion object{
//        val Enabled = ColorMode(
//            upperTextColor = TextColorUpperThanDPLight,
//            lowerTextColor = TextColorUpperThanDPLight
//        )
//
//        val DisEnabled = ColorMode(
//            upperTextColor = textColorDark,
//            lowerTextColor = textColorDark
//        )
//    }
//}