package com.example.masterproject.ui.screens

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable
data object MainGraph{
    @Serializable
    data object MainRoute
}

@Serializable
data object ExchangeRateGraphs{
    @Serializable
    data object ExchangeRateRoute

    @Serializable
    data object EditRoute
}

@Serializable
data object StorageGraphs{
    @Serializable
    data object StorageRoute
}

@Serializable
data object AppSettingGraphs{
    @Serializable
    data object AppSettingRoute
}


fun NavBackStackEntry?.routeClass(): KClass<*>?{
    return this?.destination?.routeClass()
}

fun NavDestination?.routeClass(): KClass<*>?{
    return this?.route
        ?.split("/")
        ?.first()
        ?.let { className ->
            generateSequence(className, ::replaceLastDotByDollar)
                .mapNotNull (::tryParseClass)
                .firstOrNull()
        }
}

private fun tryParseClass(className: String): KClass<*>?{
    return runCatching { Class.forName(className).kotlin }. getOrNull()
}

private fun replaceLastDotByDollar(input: String): String?{
    val index = input.lastIndexOf('.')
    return if(index != -1){
        String(input.toCharArray().apply { set(index, '$') })
    } else {
        null
    }
}