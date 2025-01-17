package com.example.masterproject.ui.screens.storage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.masterproject.R
import com.example.masterproject.model.marketsnapshot.entities.MarketPairsWithDetailsSnapshot
import com.example.masterproject.ui.screens.LocalNavController
import com.example.masterproject.ui.screens.MainGraph
import com.example.masterproject.ui.screens.NavigateUpAction
import com.example.masterproject.ui.screens.exchangerate.ExchangeRateViewModel
import com.example.masterproject.ui.screens.storage.StorageScreenViewModel.ScreenState
import com.example.masterproject.ui.tools.cryptoCoins
import com.example.masterproject.ui.tools.formatUnixTimeMillis
import kotlin.random.Random

@Composable
@Preview(showSystemUi = true)
fun StorageScreenPreview(){
    StorageContent(
        getScreenState = {ScreenState.Loading}
    )
}

@Composable
fun StorageScreen(
    returnTitle: (Int) -> Unit,
    returnNavigateUpAction: (NavigateUpAction) -> Unit
){
    val viewModel: StorageScreenViewModel = hiltViewModel()
    val navController = LocalNavController.current
    val screenState = viewModel.stateFlow.collectAsState()

    LaunchedEffect(Unit) {
        returnTitle.invoke(R.string.storage)
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

    StorageContent(
        getScreenState = {screenState.value}
    )
}

@Composable
fun StorageContent(
    getScreenState: () -> ScreenState
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 26.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE5E4E4)),
            elevation = CardDefaults.elevatedCardElevation(6.dp)
        ) {
            when(val screenState = getScreenState()){
                is ScreenState.Loading -> {

                }
                is ScreenState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(screenState.marketSnapshots.size){index ->
                            //ContainerForData(it)
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
                                        text = "Data ${screenState.marketSnapshots[index].marketSnapshot.id}",
                                        fontSize = 24.sp,
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .padding(16.dp)
                                    )
                                    Text(
                                        text = "Time: ${formatUnixTimeMillis(screenState.marketSnapshots[index].marketSnapshot.time)}",
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
                                        items(screenState.marketSnapshots[index].tradePairs.size){
                                            Text(
                                                text = "${screenState.marketSnapshots[index].tradePairs[it]}: ${screenState.marketSnapshots[index].prices[it]}",
                                                modifier = Modifier
                                                    .padding(6.dp)
                                                    .wrapContentSize()
                                            )
                                        }
                                    }
                                }

                            }
                        }
                    }
                }

            }

        }
    }
}
//
//
//@Composable
//fun ContainerForData(index: Int){
//    Card(
//        modifier = Modifier
//            .padding(horizontal = 8.dp, vertical = 16.dp)
//            .fillMaxWidth(),
//        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F4F4))
//    ) {
//        Column (
//            modifier = Modifier
//                .heightIn(min = 100.dp, max = 900.dp)
//                .fillMaxWidth(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ){
//            Text(
//                text = "Data ${savedCoinPairs[index].id}",
//                fontSize = 24.sp,
//                modifier = Modifier
//                    .wrapContentSize()
//                    .padding(16.dp)
//            )
//            Text(
//                text = "Time: ${savedCoinPairs[index].time}",
//                fontSize = 20.sp,
//                modifier = Modifier
//                    .wrapContentSize()
//                    .padding(16.dp)
//            )
//            LazyColumn(
//                modifier = Modifier
//                    .wrapContentSize()
//                    .padding(8.dp),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.SpaceEvenly
//            ){
//                items(savedCoinPairs[index].tradePairs.size){
//                    Text(
//                        text = "Coin ${savedCoinPairs[index].tradePairs[it]} ${savedCoinPairs[index].prices[it]}",
//                        modifier = Modifier
//                            .padding(6.dp)
//                            .wrapContentSize()
//                    )
//                }
//            }
//        }
//
//    }
//}
//
//private val savedCoinPairs = (0..10).map {
//    MarketPairsWithDetailsSnapshot(
//        time = it.toLong() * 1000,
//        tradePairs = cryptoCoins.subList(0, 10),
//        id = it.toLong(),
//        prices = (0..10).map{(Random.nextInt(1000)).toDouble()},
//        sourceName = List(10) { "Binance" })
//}
