package com.example.masterproject.ui.screens.setting

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.masterproject.R
import com.example.masterproject.ui.components.CustomButton
import com.example.masterproject.ui.screens.LocalNavController
import com.example.masterproject.ui.screens.MainGraph
import com.example.masterproject.ui.screens.NavigateUpAction

@Composable
@Preview(showSystemUi = true)
fun SettingScreenPreview(){
    SettingContent(resetClicked = { })
}

@Composable
fun SettingScreen(
    returnTitle: (Int) -> Unit,
    returnNavigateUpAction: (NavigateUpAction) -> Unit
) {
    val currentReturnTitle = rememberUpdatedState(returnTitle)
    val currentReturnNavigateUpAction = rememberUpdatedState(returnNavigateUpAction)
    val viewModel: SettingViewModel = hiltViewModel()
    val context = LocalContext.current
    val navController = LocalNavController.current

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {

                    currentReturnTitle.value.invoke(R.string.app_setting)
                    currentReturnNavigateUpAction.value.invoke(
                        NavigateUpAction.VisibleNavigate(
                            onNavigateButtonPressed = { navController.navigate(MainGraph){
                                popUpTo(0) { inclusive = true }
                                restoreState = true
                            } },
                        ))
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    SettingContent(
        resetClicked = {
            Toast.makeText(context,
                context.getString(R.string.feature_in_planning),
                Toast.LENGTH_LONG)
                .show()
        }
    )

}


@Composable
fun SettingContent(resetClicked: () -> Unit){
    var showSavingPopUp by remember { mutableStateOf(false) }
    var showDeletingPopUp by remember { mutableStateOf(false) }
    var useColorMode by remember { mutableStateOf(true) }
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            SettingField(text = "Color Mode") {
                Checkbox(
                    checked = useColorMode,
                    onCheckedChange = {useColorMode = !useColorMode},
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.White,
                        uncheckedColor = Color(0xFFE5E4E4),
                        checkmarkColor = Color.Black,
                    )
                )
            }

            SettingField(text = "Choose pairs for saving") {
                Box(){
                    Button(
                        onClick = { showSavingPopUp = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF4F4F4),
                            contentColor = Color.Black
                        ),
                        elevation = ButtonDefaults.elevatedButtonElevation(6.dp)
                    ) {
                        Text(
                            text = "Choose"
                        )
                    }
                    DropdownMenu(
                        expanded = showSavingPopUp,
                        onDismissRequest = { showSavingPopUp = false },
                        containerColor = Color(0xFFF4F4F4),
                        shadowElevation = 3.dp
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                text = "text",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterHorizontally)
                                )
                                   },
                            onClick = { },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "text2",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally)
                                )
                            },
                            onClick = { }
                        )
                    }
                }
            }

            SettingField(text = "Delete item from Storage") {
                Box(){
                    Button(
                        onClick = { showDeletingPopUp = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF4F4F4),
                            contentColor = Color.Black
                        ),
                        elevation = ButtonDefaults.elevatedButtonElevation(6.dp)
                    ) {
                        Text(
                            text = "Choose"
                        )
                    }
                    DropdownMenu(
                        expanded = showDeletingPopUp,
                        onDismissRequest = { showDeletingPopUp = false },
                        containerColor = Color(0xFFF4F4F4),
                        shadowElevation = 3.dp
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "text",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally)
                                )
                            },
                            onClick = { },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "text2",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally)
                                )
                            },
                            onClick = { }
                        )
                    }
                }
            }

            SettingField(text = "Reset Database Data") {
                Button(
                    onClick = {resetClicked.invoke()},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF4F4F4),
                        contentColor = Color.Black
                    ),
                    elevation = ButtonDefaults.elevatedButtonElevation(6.dp)
                ) {
                    Text(
                        text = "Reset",
                    )
                }
            }
        }
        CustomButton(
            text = "Apply",
        ) { }
    }
}

@Composable
fun SettingField(
    text: String,
    content: @Composable () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            fontSize = 22.sp
        )
        content.invoke()
    }
}
