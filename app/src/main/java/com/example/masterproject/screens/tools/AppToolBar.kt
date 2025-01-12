package com.example.masterproject.screens.tools

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.masterproject.R

sealed class NavigateUpAction{
    data object Hidden: NavigateUpAction()
    data class VisibleNavigate(
        val onNavigateButtonPressed: () -> Unit
    ): NavigateUpAction()
    data class VisibleNavigateAndAction(
        val onNavigateButtonPressed: () -> Unit,
        val onActionButtonPressed: () -> Unit
    ): NavigateUpAction()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolBar(
    @StringRes titleRes: Int,
    navigateUpAction: NavigateUpAction
){
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        ),
        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(
                text = stringResource(titleRes),
                fontSize = 32.sp,
            )
        },
        navigationIcon = {
            if(navigateUpAction is NavigateUpAction.VisibleNavigateAndAction || navigateUpAction is NavigateUpAction.VisibleNavigate){

                IconButton(
                    onClick = {
                        if (navigateUpAction is NavigateUpAction.VisibleNavigate) navigateUpAction.onNavigateButtonPressed.invoke()
                        else if (navigateUpAction is NavigateUpAction.VisibleNavigateAndAction) navigateUpAction.onNavigateButtonPressed.invoke()
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        },
        actions = {
            if (navigateUpAction is NavigateUpAction.VisibleNavigateAndAction) {
                IconButton(
                    onClick = navigateUpAction.onActionButtonPressed
                ) {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        painter = painterResource(R.drawable.setting_icon),
                        contentDescription = null
                    )
                }
            }
        }
    )


}