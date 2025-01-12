package com.example.masterproject.screens.datastore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.masterproject.R
import com.example.masterproject.model.database.SavedCoinPairs
import com.example.masterproject.screens.ExchangeRateRoute
import com.example.masterproject.screens.LocalNavController
import com.example.masterproject.screens.MainRoute
import com.example.masterproject.screens.AppSettingRoute
import com.example.masterproject.screens.tools.NavBarItemContainer
import com.example.masterproject.screens.tools.cryptoCoins
import kotlin.random.Random

@Composable
@Preview(showSystemUi = true)
fun StorageScreenPreview(){
    StorageContent(
        onExchangeRateScreen = { },
        onSettingScreen = { },
        onMainScreen = { }
    )
}

@Composable
fun StorageScreen(){
    val navController = LocalNavController.current

    StorageContent(
        onMainScreen = { navController.navigate(MainRoute) },
        onExchangeRateScreen = { navController.navigate(ExchangeRateRoute) },
        onSettingScreen = { navController.navigate(AppSettingRoute) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageContent(onMainScreen: () -> Unit,
                  onExchangeRateScreen: () -> Unit,
                  onSettingScreen: () -> Unit) {
    Scaffold(
    topBar = {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth(),
            title = {
                Text(
                    text = stringResource(R.string.storage),
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
            NavBarItemContainer(description = "exchange", runTask =  { onExchangeRateScreen.invoke() }, scope = this, icon = R.drawable.analitic_icon, selected = false)
            NavBarItemContainer("storage", runTask =  { }, scope = this, icon = R.drawable.storage_icon, selected = true)
            NavBarItemContainer("setting", runTask =  { onSettingScreen.invoke() }, scope = this, icon = R.drawable.setting_icon, selected = false)
        }

    }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 26.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE5E4E4)),
                elevation = CardDefaults.elevatedCardElevation(6.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(savedCoinPairs.size){
                        ContainerForData(it)
                    }
                }
            }
        }
    }
}

//@Preview(showSystemUi = true)
@Composable
fun ContainerForData(index: Int){
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F4F4))
    ) {
        Column (
            modifier = Modifier
                .heightIn(min = 100.dp, max = 900.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Text(
                text = "Data ${savedCoinPairs[index].id + 1}",
                fontSize = 24.sp,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp)
            )
            Text(
                text = "Time: ${savedCoinPairs[index].time + 1}",
                fontSize = 20.sp,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ){
                items(savedCoinPairs[index].pairs.size){
                    Text(
                        text = "Coin ${savedCoinPairs[index].pairs[it]} ${savedCoinPairs[index].price[it]}",
                        modifier = Modifier
                            .padding(6.dp)
                            .wrapContentSize()
                    )
                }
            }
        }

    }
}

private val savedCoinPairs = (0..10).map {
    SavedCoinPairs(time = it.toLong() * 1000, pairs = cryptoCoins.subList(0, 10), id = it.toLong(), price = (0..10).map{(Random.nextInt(1000)).toLong()})
}
