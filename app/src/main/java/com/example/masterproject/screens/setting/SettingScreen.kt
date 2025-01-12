package com.example.masterproject.screens.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.masterproject.R
import com.example.masterproject.screens.ExchangeRateRoute
import com.example.masterproject.screens.LocalNavController
import com.example.masterproject.screens.MainRoute
import com.example.masterproject.screens.StorageRoute
import com.example.masterproject.screens.tools.NavBarItemContainer

@Composable
@Preview(showSystemUi = true)
fun SettingScreenPreview(){
    SettingContent(
        onMainScreen = { },
        onStorageScreen = { },
        onExchangeRateScreen = { }
    )
}

@Composable
fun SettingScreen() {
    val navController = LocalNavController.current

    SettingContent(
        onMainScreen = {navController.navigate(MainRoute) },
        onExchangeRateScreen = { navController.navigate(ExchangeRateRoute)},
        onStorageScreen = { navController.navigate(StorageRoute)}
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingContent(onMainScreen: () -> Unit,
                   onExchangeRateScreen: () -> Unit,
                   onStorageScreen: () -> Unit){
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = stringResource(R.string.app_setting),
                        fontSize = 32.sp,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onMainScreen.invoke() }
                    ) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFF4F4F4),
                contentColor = Color.Black,
                modifier = Modifier.height(70.dp)
            ) {
                NavBarItemContainer(description = "main", runTask =  { onMainScreen.invoke() }, scope = this, icon = R.drawable.home_icon, selected = false)
                NavBarItemContainer(description = "exchange", runTask =  { onExchangeRateScreen.invoke()}, scope = this, icon = R.drawable.analitic_icon, selected = false)
                NavBarItemContainer(description = "storage", runTask =  { onStorageScreen.invoke() }, scope = this, icon = R.drawable.storage_icon, selected = false)
                NavBarItemContainer(description = "setting", runTask =  {  }, scope = this, icon = R.drawable.setting_icon, selected = true)
            }

        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Color Coin Theme ",
                        fontSize = 26.sp
                    )

                    Checkbox(
                        checked = false,
                        onCheckedChange = {}
                    )
                }
            }
        }
    }
}