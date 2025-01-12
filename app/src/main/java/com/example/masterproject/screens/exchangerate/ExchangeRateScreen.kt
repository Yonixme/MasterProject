package com.example.masterproject.screens.exchangerate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.masterproject.R
import com.example.masterproject.screens.LocalNavController
import com.example.masterproject.screens.MainRoute
import com.example.masterproject.screens.AppSettingRoute
import com.example.masterproject.screens.EditRoute
import com.example.masterproject.screens.StorageRoute
import com.example.masterproject.screens.exchangerate.ExchangeRateViewModel.*
import com.example.masterproject.screens.exchangerate.edititem.EditItemScreen
import com.example.masterproject.screens.tools.CustomButton
import com.example.masterproject.screens.tools.NavBarItemContainer

@Preview(showSystemUi = true)
@Composable
fun ExchangeRateScreenPreview(){
    ExchangeRateContent(
        onMainScreen = { },
        onSettingScreen = { },
        onStorageScreen = { },
        onEditItemListScreen = { },
        getScreenState = { ScreenState.Loading })
}

@Composable
fun ExchangeRateScreen(
    returnTitle: (String) -> Unit
){
    val viewModel: ExchangeRateViewModel = hiltViewModel()
    val navController = LocalNavController.current
    val screenState = viewModel.stateFlow.collectAsState()
    returnTitle.invoke("ddd")

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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeRateContent(getScreenState: () -> ScreenState,
                        onMainScreen: () -> Unit,
                        onSettingScreen: () -> Unit,
                        onStorageScreen: () -> Unit,
                        onEditItemListScreen: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = stringResource(R.string.exchange_rate),
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
                },
                actions = {
                    IconButton(
                        onClick = {   }
                    ) {
                        Icon(
                            modifier = Modifier.size(25.dp),
                            painter = painterResource(R.drawable.setting_icon),
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
                NavBarItemContainer(description = "exchange", runTask =  { }, scope = this, icon = R.drawable.analitic_icon, selected = true)
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
                                                text = it.pair,
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
                    CustomButton(stringResource(R.string.save)) { }
                    CustomButton(stringResource(R.string.edit)) { onEditItemListScreen.invoke() }
                }
            }
        }
    }
}