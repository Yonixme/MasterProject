package com.example.masterproject.ui.screens.mainscreen

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.masterproject.R
import com.example.masterproject.ui.screens.AppSettingGraphs.AppSettingRoute
import com.example.masterproject.ui.screens.LocalNavController
import com.example.masterproject.ui.screens.ExchangeRateGraphs.ExchangeRateRoute
import com.example.masterproject.ui.screens.NavigateUpAction
import com.example.masterproject.ui.screens.StrorageGraphs.StorageRoute

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
fun MainScreen(
    returnTitle: (Int) -> Unit,
    returnNavigateUpAction: (NavigateUpAction) -> Unit
){
    val navController = LocalNavController.current

    LaunchedEffect(Unit) {
        returnTitle.invoke(R.string.main_screen)
        returnNavigateUpAction.invoke(NavigateUpAction.Hidden)
    }

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
    Box(
        modifier = Modifier
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