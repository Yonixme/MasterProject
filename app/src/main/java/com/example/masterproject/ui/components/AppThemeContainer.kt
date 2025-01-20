package com.example.masterproject.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.masterproject.model.config.theme.AppTheme
import com.example.masterproject.model.config.theme.SharedPreferencesThemeDataSource
import com.example.masterproject.ui.config.theme.AppThemeController
import com.example.masterproject.ui.config.theme.EmptyThemeController
import com.example.masterproject.ui.config.theme.RealThemeController

val LocalAppTheme = compositionLocalOf<AppTheme> {
    AppTheme.Light
}

val LocalAppThemeController = staticCompositionLocalOf<AppThemeController> {
    EmptyThemeController
}

@Composable
fun AppThemeContainer(content:@Composable () -> Unit){
    val context = LocalContext.current
    val themeDataSource = remember {
        SharedPreferencesThemeDataSource(context)
    }
    val controller = remember {
        RealThemeController(themeDataSource)
    }
    val theme by themeDataSource.themeStateFlow.collectAsStateWithLifecycle()
    CompositionLocalProvider(
        LocalAppTheme provides theme,
        LocalAppThemeController provides controller,
    ) {
        content()
    }
}