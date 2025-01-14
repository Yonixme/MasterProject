package com.example.masterproject.ui.screens

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController

val LocalNavController = staticCompositionLocalOf<NavController> {
    error("Can't access NavController")
}