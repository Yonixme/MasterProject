package com.example.masterproject

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun MainScreen(){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()

    ) {
        Text(
            text = stringResource(R.string.header_app),
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 200.dp)
        )
        Text(
            text = stringResource(R.string.exchange_rate),
            fontSize = 25.sp,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.padding(30.dp)
                .clickable(
                    onClick = {

                    },
                    interactionSource = MutableInteractionSource(),
                    indication = rememberRipple()
                )
        )
        Text(
            text = stringResource(R.string.forecast_from_gpt),
            fontSize = 25.sp,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.padding(30.dp)
                .clickable(
                    onClick = {

                    },
                    interactionSource = MutableInteractionSource(),
                    indication = rememberRipple()
                )
        )
        Text(
            text = stringResource(R.string.data_store),
            fontSize = 25.sp,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.padding(30.dp)
                .clickable(
                    onClick = {

                    },
                    interactionSource = MutableInteractionSource(),
                    indication = rememberRipple()
                )
        )
        Text(
            text = stringResource(R.string.setting),
            fontSize = 25.sp,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.padding(30.dp)
                .clickable(
                    onClick = {

                    },
                    interactionSource = MutableInteractionSource(),
                    indication = rememberRipple()
                )
        )
    }
}
