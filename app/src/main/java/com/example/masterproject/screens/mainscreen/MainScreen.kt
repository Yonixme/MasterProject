package com.example.masterproject.screens.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.example.masterproject.screens.AppSettingRoute
import com.example.masterproject.screens.StorageRoute
import com.example.masterproject.screens.tools.NavBarItemContainer

@Composable
@Preview(showSystemUi = true)
fun MainScreenPreview(){
    MainContent(
        onExchangeRateScreen = { },
        onSettingScreen = { },
        onStorageScreen = { }
    )
}

@Composable
fun MainScreen(){
    val navController = LocalNavController.current
    MainContent(
        onExchangeRateScreen = { navController.navigate(ExchangeRateRoute) },
        onSettingScreen = { navController.navigate(AppSettingRoute)},
        onStorageScreen = {navController.navigate(StorageRoute)})
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(onExchangeRateScreen: () -> Unit,
                onSettingScreen: () -> Unit,
                onStorageScreen: () -> Unit) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = stringResource(R.string.main_screen),
                        fontSize = 32.sp,
                    )
                },
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFF4F4F4),
                contentColor = Color.Black,
                modifier = Modifier.height(70.dp)
            ) {
                NavBarItemContainer(description = "main", runTask =  { }, scope = this, icon = R.drawable.home_icon, selected = true)
                NavBarItemContainer(description = "exchange", runTask =  { onExchangeRateScreen.invoke() }, scope = this, icon = R.drawable.analitic_icon, selected = false)
                NavBarItemContainer("storage", runTask =  { onStorageScreen.invoke() }, scope = this, icon = R.drawable.storage_icon, selected = false)
                NavBarItemContainer("setting", runTask =  { onSettingScreen.invoke() }, scope = this, icon = R.drawable.setting_icon, selected = false)
            }
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ContainerForText(stringResource(R.string.exchange_rate)){ onExchangeRateScreen.invoke()}
                ContainerForText("Storage Information"){ onStorageScreen.invoke()}
                ContainerForText("Setting"){ onSettingScreen.invoke()}
            }
        }
    }
}

@Composable
fun ContainerForText(text: String,
                     onExchangeRateScreen: () -> Unit, ) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF4F4F4),
            contentColor = Color.Black,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple()
                ) { onExchangeRateScreen.invoke() }
        ) {
            Text(text = text,
                style = MaterialTheme.typography.titleLarge)
        }
    }
}


/*@Composable
@Preview(showSystemUi = true)
fun MainScreenPreview(){
    MainContent {  }
}

@Composable
fun MainScreen(){
    val navController = LocalNavController.current
    MainContent(
        onExchangeRateScreen = {
            navController.navigate(ExchangeRateRoute)
    })
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(onExchangeRateScreen: () -> Unit) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = stringResource(R.string.main_screen),
                        fontSize = 32.sp,
                    )
                },
            )
        },
        bottomBar =
        {
            NavigationBar(
                containerColor = Color(0xFFF4F4F4),
                contentColor = Color.Black,
                modifier = Modifier.height(70.dp)
            ) {
                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color(0xFFD7D6D6),
                    ),
                    selected = true,
                    onClick = {  },
                    icon = {
                        Icon(painter = painterResource(R.drawable.home_icon),
                            contentDescription = null,
                            modifier = Modifier.size(25.dp),
                            tint = Color.Unspecified,
                        )
                    },
                )
                NavigationBarItem(
                    selected = false,
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color(0xFFD7D6D6),
                    ),
                    onClick = { onExchangeRateScreen.invoke() },
                    icon = {
                        Icon(painter = painterResource(R.drawable.analitic_icon),
                            contentDescription = null,
                            modifier = Modifier.size(25.dp),
                            tint = Color.Unspecified)
                    },
                )
                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color(0xFFD7D6D6),
                    ),
                    selected = false,
                    onClick = {  },
                    icon = {
                        Icon(painter = painterResource(R.drawable.storage_icon),
                            contentDescription = null,
                            modifier = Modifier.size(25.dp),
                            tint = Color.Unspecified)
                    },
                )
                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color(0xFFD7D6D6),
                    ),
                    selected = false,
                    onClick = {  },
                    icon = {
                        Icon(painter = painterResource(R.drawable.setting_icon),
                            contentDescription = null,
                            modifier = Modifier.size(25.dp),
                            tint = Color.Unspecified)
                    },
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ContainerForText(stringResource(R.string.exchange_rate)){ onExchangeRateScreen.invoke()}

                ContainerForText("Storage Information"){ onExchangeRateScreen.invoke()}

                ContainerForText("Setting"){ onExchangeRateScreen.invoke()}
            }
        }
    }
}

@Composable
fun ContainerForText(text: String,
                     onExchangeRateScreen: () -> Unit) {
    val exchangeRateLabel = stringResource(id = R.string.exchange_rate)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF4F4F4),
            contentColor = Color.Black,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple()
                ) {
                    if(text == exchangeRateLabel){
                         onExchangeRateScreen.invoke()
                    }else{
                        //TODO
                    }
                }
        ) {
            Text(text = text,
                style = MaterialTheme.typography.titleLarge)
        }
    }
}*/

