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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.masterproject.R
import com.example.masterproject.ui.components.LocalAppTheme
import com.example.masterproject.ui.screens.AppSettingGraphs.AppSettingRoute
import com.example.masterproject.ui.tools.LocalNavController
import com.example.masterproject.ui.screens.ExchangeRateGraphs.ExchangeRateRoute
import com.example.masterproject.ui.components.NavigateUpAction
import com.example.masterproject.ui.screens.StrorageGraphs.StorageRoute
import com.example.masterproject.ui.screens.exchangerate.ExchangeRateViewModel

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
    val currentReturnTitle = rememberUpdatedState(returnTitle)
    val currentReturnNavigateUpAction = rememberUpdatedState(returnNavigateUpAction)
    val navController = LocalNavController.current
    //val viewModel: ExchangeRateViewModel = hiltViewModel()


//    LaunchedEffect(Unit) {
//        returnTitle.invoke(R.string.main_screen)
//        returnNavigateUpAction.invoke(NavigateUpAction.Hidden)
//    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    currentReturnTitle.value.invoke(R.string.main_screen)
                    currentReturnNavigateUpAction.value.invoke(NavigateUpAction.Hidden)
                }

                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
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
    val theme = LocalAppTheme.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(theme.bgColor)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ContainerForText(stringResource(R.string.exchange_rate)){ onExchangeRateScreen.invoke()}
        ContainerForText("Storage Information"){ onStorageScreen.invoke()}
        ContainerForText("Setting"){ onSettingScreen.invoke()}
    }
}



@Composable
fun ContainerForText(text: String,
                     onExchangeRateScreen: () -> Unit, ) {
    val theme = LocalAppTheme.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = theme.primaryColor,
            contentColor = theme.textColor,
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