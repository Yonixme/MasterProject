package com.example.masterproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.masterproject.screens.ExchangeRateRoute
import com.example.masterproject.screens.LocalNavController
import com.example.masterproject.screens.MainRoute
import com.example.masterproject.screens.AppSettingRoute
import com.example.masterproject.screens.EditRoute
import com.example.masterproject.screens.SettingRoute
import com.example.masterproject.screens.StorageRoute
import com.example.masterproject.screens.datastore.StorageScreen
import com.example.masterproject.screens.exchangerate.edititem.EditItemScreen
import com.example.masterproject.screens.exchangerate.ExchangeRateScreen
import com.example.masterproject.screens.mainscreen.MainScreen
import com.example.masterproject.screens.setting.InnerSettingScreen
import com.example.masterproject.screens.setting.SettingScreen
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
    var title by rememberSaveable { mutableStateOf("") }

    CompositionLocalProvider(
        LocalNavController provides navController
    ) {
        NavHost(
            navController = navController,
            startDestination = MainRoute,
            modifier = Modifier.fillMaxSize()
        )
        {
            composable(MainRoute) { MainScreen() }
            composable(ExchangeRateRoute) { ExchangeRateScreen(returnTitle = {string -> title = string}) }
            composable(StorageRoute) { StorageScreen() }
            composable(AppSettingRoute) { SettingScreen() }
            composable(EditRoute) { EditItemScreen() }
            composable(SettingRoute) { InnerSettingScreen {
                //TODO
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

