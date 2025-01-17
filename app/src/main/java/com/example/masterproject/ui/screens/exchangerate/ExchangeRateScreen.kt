package com.example.masterproject.ui.screens.exchangerate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.masterproject.R
import com.example.masterproject.ui.screens.LocalNavController
import com.example.masterproject.ui.screens.exchangerate.ExchangeRateViewModel.*
import com.example.masterproject.ui.components.CustomButton
import com.example.masterproject.ui.screens.ExchangeRateGraphs.EditRoute
import com.example.masterproject.ui.screens.MainGraph
import com.example.masterproject.ui.screens.NavigateUpAction

@Preview(showSystemUi = true)
@Composable
fun ExchangeRateScreenPreview(){
    ExchangeRateContent(
        onEditItemListScreen = { },
        getScreenState = { ScreenState.Loading },
        onSaveClicked = { })
}

@Composable
fun ExchangeRateScreen(
    returnTitle: (Int) -> Unit,
    returnNavigateUpAction: (NavigateUpAction) -> Unit
){
    val viewModel: ExchangeRateViewModel = hiltViewModel()
    val navController = LocalNavController.current
    val screenState = viewModel.stateFlow.collectAsState()

    LaunchedEffect(Unit) {
        returnTitle.invoke(R.string.exchange_rate)
        returnNavigateUpAction.invoke(
            NavigateUpAction.VisibleNavigateAndAction(
                onNavigateButtonPressed = { navController.navigate(MainGraph){
                    popUpTo(MainGraph){
                        inclusive = true
                    }
                } },
                onActionButtonPressed = { }
            ))
    }


    ExchangeRateContent(
        getScreenState = {screenState.value},
        onEditItemListScreen = { navController.navigate(EditRoute)},
        onSaveClicked = { viewModel.saveMarketSnapshot() }
    )

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    viewModel.startUpdatingCoinPrice()
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            viewModel.stopUpDatingCoinPrice()
        }
    }
}

@Composable
fun ExchangeRateContent(getScreenState: () -> ScreenState,
                        onEditItemListScreen: () -> Unit,
                        onSaveClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(550.dp)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F4F4)),
                elevation = CardDefaults.elevatedCardElevation(6.dp)
            ) {
                when(val screenState = getScreenState()){
                    ScreenState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
                    }
                    is ScreenState.Success -> {
                        LazyColumn(modifier = Modifier.fillMaxSize()){
                            items(screenState.pairCoins){
                                Row (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Text(
                                        text = it.tradePair,
                                        modifier = Modifier,
                                        fontSize = 25.sp
                                    )
                                    Text(
                                        text = if(it.price >= 0 )it.price.toString() else "",
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp),
                                        fontSize = 25.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                CustomButton(stringResource(R.string.save)) { onSaveClicked.invoke()}
                CustomButton(stringResource(R.string.edit)) { onEditItemListScreen.invoke() }
            }
        }
    }
}


/*@Composable
fun ExchangeRateScreen(){
    val viewModel: ExchangeRateViewModel = hiltViewModel()
    val navController = LocalNavController.current
    val screenState = viewModel.stateFlow.collectAsState()

    ExchangeRateContent(
        getScreenState = {screenState.value},
        onMainScreen = { navController.navigate(MainRoute)},
        onSettingScreen = { navController.navigate(AppSettingRoute)},
        onStorageScreen = { navController.navigate(StorageRoute)},
        onEditItemListScreen = { navController.navigate(EditRoute)}
    )

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    viewModel.startUpdatingCoinPrice()
                }

                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            viewModel.stopUpDatingCoinPrice()
        }
    }
}*/