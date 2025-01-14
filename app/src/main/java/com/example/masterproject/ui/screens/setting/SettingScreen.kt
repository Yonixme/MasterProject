package com.example.masterproject.ui.screens.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.masterproject.R
import com.example.masterproject.ui.screens.LocalNavController
import com.example.masterproject.ui.screens.MainGraph
import com.example.masterproject.ui.screens.NavigateUpAction

@Composable
@Preview(showSystemUi = true)
fun SettingScreenPreview(){
    SettingContent()
}

@Composable
fun SettingScreen(
    returnTitle: (Int) -> Unit,
    returnNavigateUpAction: (NavigateUpAction) -> Unit
) {
    val navController = LocalNavController.current

    LaunchedEffect(Unit) {
        returnTitle.invoke(R.string.app_setting)
        returnNavigateUpAction.invoke(
            NavigateUpAction.VisibleNavigate(
                onNavigateButtonPressed = { navController.navigate(MainGraph){
                    popUpTo(MainGraph){
                        inclusive = true
                    }
                } },
            ))

    }

    SettingContent()

}


@Composable
fun SettingContent(){
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Color Coin Theme ",
                    fontSize = 26.sp
                )

                Checkbox(
                    checked = false,
                    onCheckedChange = {}
                )
            }
        }
    }
}
