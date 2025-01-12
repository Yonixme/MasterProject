package com.example.masterproject.screens.tools

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun NavBarItemContainer(description: String,
                        runTask: () -> Unit,
                        scope: RowScope,
                        icon: Int,
                        selected: Boolean){
    scope.NavigationBarItem(
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = Color(0xFFD7D6D6),
        ),
        selected = selected,
        onClick = {
            runTask.invoke()
        },
        icon = {
            Icon(painter = painterResource(icon),
                contentDescription = description,
                modifier = Modifier.size(25.dp),
                tint = Color.Unspecified,)
        },
    )
}