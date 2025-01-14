package com.example.masterproject.ui.screens

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.collections.immutable.ImmutableList

@Composable
fun AppNavigationBar(
    navController: NavController,
    tabs: ImmutableList<AppTab>,
){
    NavigationBar(
        containerColor = Color(0xFFF4F4F4),
        contentColor = Color.Black,
        modifier = Modifier.height(100.dp)
    ) {
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val closestNavGraphDestination = currentBackStackEntry?.destination?.hierarchy?.first{
            it is NavGraph
        }
        val closestNavGraphClass = closestNavGraphDestination.routeClass()
        val currentTab = tabs.firstOrNull{ it.graph::class == closestNavGraphClass}
        tabs.forEach{tab ->
            NavigationBarItem(
                selected = currentTab == tab,
                onClick = { navController.navigate(tab.graph){
                    if (currentTab != null){
                        popUpTo(currentTab.graph){
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }

                } },
                icon = {
                    Icon(painter = painterResource(tab.iconRes),
                        contentDescription = stringResource(tab.labelRes),
                        modifier = Modifier.size(25.dp),
                        tint = Color.Unspecified,
                        )
                },
                label = {
                    Text(text = stringResource( id = tab.labelRes ))
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color(0xFFD7D6D6),
                ),
            )
        }
    }
}