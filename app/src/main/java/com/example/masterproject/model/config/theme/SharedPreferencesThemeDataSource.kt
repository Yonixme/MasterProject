package com.example.masterproject.model.config.theme

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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
import kotlinx.coroutines.flow.MutableStateFlow


class SharedPreferencesThemeDataSource(
    context: Context
) : ThemeDataSource{
    private val preferences = context.getSharedPreferences(
        THEME_NAME, Context.MODE_PRIVATE)

    override val themeStateFlow: MutableStateFlow<AppTheme> = MutableStateFlow(readTheme())

    override fun setTheme(theme: AppTheme) {
        preferences.edit()
            .putInt(KEY_THEME_PRIMARY_COLOR, theme.primaryColor.toArgb())
            .putInt(KEY_THEME_SECONDARY_COLOR, theme.secondaryColor.toArgb())
            .putInt(KEY_THEME_BG_COLOR, theme.bgColor.toArgb())
            .putInt(KEY_THEME_TEXT_COLOR, theme.textColor.toArgb())
            .putInt(KEY_THEME_UPPER_TEXT_COLOR, theme.upperTextColor.toArgb())
            .putInt(KEY_THEME_LOWER_TEXT_COLOR, theme.lowerTextColor.toArgb())
            .apply()
        themeStateFlow.value = theme
    }

    private fun readTheme(): AppTheme{
        if (!hasSavedTheme()) return AppTheme.Light
        val theme = getThemeFromPreferences()
        return if(checkThemeOnValidation(theme)) theme else AppTheme.Light
    }

    private fun hasSavedTheme(): Boolean = preferences.contains(KEY_THEME_BG_COLOR)

    private fun checkThemeOnValidation(theme: AppTheme) : Boolean{
        return  (theme.bgColor == bgColorLight &&
                theme.textColor == textColorLight &&
                theme.primaryColor == primaryColorLight &&
                theme.secondaryColor == secondaryColorLight &&
                theme.upperTextColor == TextColorUpperThanDPLight &&
                theme.lowerTextColor == TextColorLowerThanDPLight)||
                (theme.textColor == textColorDark &&
                theme.primaryColor == primaryColorDark &&
                theme.bgColor == bgColorDark &&
                theme.secondaryColor == secondaryColorDark &&
                theme.upperTextColor == TextColorUpperThanDPDark &&
                theme.lowerTextColor == TextColorLowerThanDPDark)
    }
    private fun getThemeFromPreferences(): AppTheme{
        return AppTheme(
            primaryColor = Color(preferences.getInt(KEY_THEME_PRIMARY_COLOR, 0)),
            secondaryColor = Color(preferences.getInt(KEY_THEME_SECONDARY_COLOR, 0)),
            bgColor = Color(preferences.getInt(KEY_THEME_BG_COLOR, 0)),
            textColor = Color(preferences.getInt(KEY_THEME_TEXT_COLOR, 0)),
            upperTextColor = Color(preferences.getInt(KEY_THEME_UPPER_TEXT_COLOR, 0)),
            lowerTextColor = Color(preferences.getInt(KEY_THEME_LOWER_TEXT_COLOR, 0)),
        )
    }


    private companion object{
        const val THEME_NAME = "Theme name"
        const val KEY_THEME_BG_COLOR = "Background Color"
        const val KEY_THEME_PRIMARY_COLOR = "Primary Color"
        const val KEY_THEME_SECONDARY_COLOR = "Secondary Color"
        const val KEY_THEME_TEXT_COLOR = "Text Color"
        const val KEY_THEME_UPPER_TEXT_COLOR = "Upper Text Color"
        const val KEY_THEME_LOWER_TEXT_COLOR = "Lower Text Color"
    }
}