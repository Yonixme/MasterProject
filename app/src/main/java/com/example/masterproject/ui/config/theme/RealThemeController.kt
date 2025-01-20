package com.example.masterproject.ui.config.theme

import com.example.masterproject.model.config.theme.AppTheme
import com.example.masterproject.model.config.theme.ThemeDataSource
import javax.inject.Inject
import javax.inject.Singleton


class RealThemeController(
    private val themeDataSource: ThemeDataSource
) : AppThemeController{

    override fun toggle() {
        val currentTheme = themeDataSource.themeStateFlow.value
        if (currentTheme == AppTheme.Dark){
            themeDataSource.setTheme(AppTheme.Light)
        }else{
            themeDataSource.setTheme(AppTheme.Dark)
        }
    }
}