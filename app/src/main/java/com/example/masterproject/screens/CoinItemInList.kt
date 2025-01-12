package com.example.masterproject.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@Composable
@Preview(showSystemUi = true)
fun CoinPairItemInListPreview(){
        CoinPairItemInList(1)
}

@Composable
fun CoinPairItemInList(index: Int){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = "Coin #$index",
            modifier = Modifier,
            fontSize = 25.sp
        )
        Text(
            text = Random.nextInt(1000).toString() + "$",
            modifier = Modifier
                .padding(horizontal = 16.dp),
            fontSize = 25.sp
        )
    }
}