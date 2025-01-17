package com.example.masterproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.masterproject.ui.screens.AppNavigationBar
import com.example.masterproject.ui.screens.AppSettingGraphs
import com.example.masterproject.ui.screens.AppSettingGraphs.AppSettingRoute
import com.example.masterproject.ui.screens.LocalNavController
import com.example.masterproject.ui.screens.SettingRoute
import com.example.masterproject.ui.screens.storage.StorageScreen
import com.example.masterproject.ui.screens.exchangerate.editmenu.EditItemScreen
import com.example.masterproject.ui.screens.exchangerate.ExchangeRateScreen
import com.example.masterproject.ui.screens.mainscreen.MainScreen
import com.example.masterproject.ui.screens.setting.InnerSettingScreen
import com.example.masterproject.ui.screens.setting.SettingScreen
import com.example.masterproject.ui.screens.AppToolBar
import com.example.masterproject.ui.screens.ExchangeRateGraphs
import com.example.masterproject.ui.screens.ExchangeRateGraphs.EditRoute
import com.example.masterproject.ui.screens.ExchangeRateGraphs.ExchangeRateRoute
import com.example.masterproject.ui.screens.MainTabs
import com.example.masterproject.ui.screens.MainGraph
import com.example.masterproject.ui.screens.MainGraph.MainRoute
import com.example.masterproject.ui.screens.NavigateUpAction
import com.example.masterproject.ui.screens.StrorageGraphs
import com.example.masterproject.ui.screens.StrorageGraphs.StorageRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExcApp()
        }
    }
}

@Composable
fun ExcApp(){
    val navController = rememberNavController()
    var titleRes by rememberSaveable { mutableStateOf(R.string.app_name) }
    var navigateUpAction: NavigateUpAction by remember { mutableStateOf(
        NavigateUpAction.VisibleNavigateAndAction(
            onNavigateButtonPressed = { },
            onActionButtonPressed = { }
        )
    ) }

    Scaffold (
        topBar = { AppToolBar(titleRes = titleRes, navigateUpAction = navigateUpAction) },
        bottomBar = { AppNavigationBar(navController = navController, tabs = MainTabs) },
        containerColor = Color.White
    ){ paddingValues ->
        CompositionLocalProvider(
            LocalNavController provides navController
        ) {
            NavHost(
                navController = navController,
                startDestination = MainGraph,
                modifier = Modifier.fillMaxSize().padding(paddingValues)
            )
            {
                navigation<MainGraph>(startDestination = MainRoute){
                    composable<MainRoute> { MainScreen(
                        returnTitle = {
                                stringRes -> titleRes = stringRes
                        },
                        returnNavigateUpAction = {
                                navigateUPAction -> navigateUpAction = navigateUPAction
                        }
                    ) }
                }
                navigation<ExchangeRateGraphs>(startDestination = ExchangeRateRoute){
                    composable<ExchangeRateRoute> { ExchangeRateScreen(
                        returnTitle = {
                                stringRes -> titleRes = stringRes
                        },
                        returnNavigateUpAction = {
                                navigateUPAction -> navigateUpAction = navigateUPAction
                        }
                    )
                    }
                    composable<EditRoute> { EditItemScreen(
                        returnTitle = {
                                stringRes -> titleRes = stringRes
                        },
                        returnNavigateUpAction = {
                                navigateUPAction -> navigateUpAction = navigateUPAction
                        }
                    ) }
                }
                navigation<StrorageGraphs>(startDestination = StorageRoute){
                    composable<StorageRoute> { StorageScreen(
                        returnTitle = {
                                stringRes -> titleRes = stringRes
                        },
                        returnNavigateUpAction = {
                                navigateUPAction -> navigateUpAction = navigateUPAction
                        }
                    ) }
                }
                navigation<AppSettingGraphs>(startDestination = AppSettingRoute){
                    composable<AppSettingRoute> { SettingScreen(
                        returnTitle = {
                                stringRes -> titleRes = stringRes
                        },
                        returnNavigateUpAction = {
                                navigateUPAction -> navigateUpAction = navigateUPAction
                        }
                    ) }
                }



                composable<SettingRoute> { InnerSettingScreen {
                    //TODO
                }
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun ExcAppPreview(){
    ExcApp()
}

