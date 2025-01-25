package com.example.masterproject.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.masterproject.model.config.colormode.ColorMode
import com.example.masterproject.model.config.colormode.SharedPreferencesColorModeDataSource
import com.example.masterproject.model.config.theme.AppTheme
import com.example.masterproject.model.config.theme.SharedPreferencesThemeDataSource
import com.example.masterproject.ui.config.colormode.AppModeController
import com.example.masterproject.ui.config.colormode.EmptyModeController
import com.example.masterproject.ui.config.colormode.RealModeController
import com.example.masterproject.ui.config.theme.AppThemeController
import com.example.masterproject.ui.config.theme.EmptyThemeController
import com.example.masterproject.ui.config.theme.RealThemeController

val LocalAppMode = compositionLocalOf<ColorMode> {
    ColorMode.DisEnabled
}

val LocalAppModeController = staticCompositionLocalOf <AppModeController> {
    EmptyModeController
}

val LocalAppTheme = compositionLocalOf<AppTheme> {
    AppTheme.Light
}

val LocalAppThemeController = staticCompositionLocalOf<AppThemeController> {
    EmptyThemeController
}

@Composable
fun AppConfigContainer(content:@Composable () -> Unit){
    val context = LocalContext.current
    val themeDataSource = remember {
        SharedPreferencesThemeDataSource(context)
    }
    val colorModeDataSource = remember {
        SharedPreferencesColorModeDataSource(context)
    }

    val themeController = remember {
        RealThemeController(themeDataSource)
    }

    val modeController = remember {
        RealModeController(colorModeDataSource)
    }

    val theme by themeDataSource.themeStateFlow.collectAsStateWithLifecycle()
    val colorMode by colorModeDataSource.modeStateFlow.collectAsStateWithLifecycle()
    CompositionLocalProvider(
        LocalAppTheme provides theme,
        LocalAppThemeController provides themeController,
        LocalAppMode provides colorMode,
        LocalAppModeController provides modeController
    ) {
        content()
    }
}