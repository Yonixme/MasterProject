package com.example.masterproject.model.config.theme

import kotlinx.coroutines.flow.StateFlow

/**
 * Store for the app theme. It holds and allows changing the current app theme
*/
interface ThemeDataSource {

    /**
     * Observe the current app theme.
     * This flow emits a theme after updating it by [setTheme] call
     */
    val themeStateFlow: StateFlow<AppTheme>

    /**
     * Update the current app theme
     */
    fun setTheme(theme: AppTheme)
}