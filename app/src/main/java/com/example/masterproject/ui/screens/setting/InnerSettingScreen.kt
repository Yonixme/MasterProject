package com.example.masterproject.ui.screens.setting

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.masterproject.R

@Composable
@Preview(showSystemUi = true)
fun InnerSettingPreview(){
    InnerSettingContent {
        SettingItemWithList(R.string.update_coin)
    }
}

@Composable
fun InnerSettingScreen(content: @Composable () -> Unit){
    InnerSettingContent(content)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InnerSettingContent(
     content: @Composable () -> Unit
){
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = stringResource(R.string.setting),
                        fontSize = 32.sp,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {  }
                    ) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
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
                //SettingItemWithList(R.string.update_coin)
                content.invoke()
            }
        }
    }
}

@Composable
fun SettingItemWithList(
    @StringRes stringResource: Int,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(stringResource),
            fontSize = 24.sp
        )

        Text(
            text = stringResource(R.string.list),
            fontSize = 24.sp,
            modifier = Modifier
                .padding(end = 8.dp),
            style = TextStyle(
                textDecoration = TextDecoration.Underline
            )
        )
    }
}