package com.example.masterproject.ui.screens.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.masterproject.R
import com.example.masterproject.model.config.colormode.ColorMode
import com.example.masterproject.model.config.theme.AppTheme
import com.example.masterproject.ui.components.LocalAppMode
import com.example.masterproject.ui.components.LocalAppModeController
import com.example.masterproject.ui.components.LocalAppTheme
import com.example.masterproject.ui.components.LocalAppThemeController
import com.example.masterproject.ui.components.custom.CustomButton
import com.example.masterproject.ui.tools.LocalNavController
import com.example.masterproject.ui.screens.MainGraph
import com.example.masterproject.ui.components.NavigateUpAction
import com.example.masterproject.ui.screens.setting.SettingViewModel.ScreenState.*
import com.example.masterproject.ui.screens.storage.ConfirmationDialog

@Composable
@Preview(showSystemUi = true)
fun SettingScreenPreview(){
    SettingContent(
        resetClicked = { },
        getScreenState = { Loading },
        onChangeIgnoreValueClicked = { })
}

@Composable
fun SettingScreen(
    returnTitle: (Int) -> Unit,
    returnNavigateUpAction: (NavigateUpAction) -> Unit
) {
    val currentReturnTitle = rememberUpdatedState(returnTitle)
    val currentReturnNavigateUpAction = rememberUpdatedState(returnNavigateUpAction)
    val viewModel: SettingViewModel = hiltViewModel()
    val navController = LocalNavController.current
    val screenState = viewModel.stateFlow.collectAsState()

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
        resetClicked = viewModel::resetDatabase,
        getScreenState = {screenState.value},
        onChangeIgnoreValueClicked = viewModel::changeFlagForMarketPair
    )

}


@Composable
fun SettingContent(resetClicked: () -> Unit,
                   getScreenState: () -> SettingViewModel.ScreenState,
                   onChangeIgnoreValueClicked: (Long) -> Unit
){
    val theme = LocalAppTheme.current
    val colorMode = LocalAppMode.current

    val themeController = LocalAppThemeController.current
    val modeController = LocalAppModeController.current

    var showSavingPopUp by remember { mutableStateOf(false) }

    var initIsDarkTheme by remember { mutableStateOf(theme == AppTheme.Dark) }
    var isDarkTheme by remember { mutableStateOf(initIsDarkTheme) }

    var initIsEnabledMode by remember { mutableStateOf(colorMode == ColorMode.Enabled) }
    var isEnabledColorMode by remember { mutableStateOf(initIsEnabledMode) }

    var showDialog by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(theme.bgColor)
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            SettingField(text = "Dark Theme") {
                Checkbox(
                    checked = isDarkTheme,
                    onCheckedChange = {
                        isDarkTheme = !isDarkTheme },
                    colors = CheckboxDefaults.colors(
                        checkedColor = theme.primaryColor,
                        uncheckedColor = theme.primaryColor,
                        checkmarkColor = theme.textColor,
                    )
                )
            }
            SettingField(text = "Color Mode") {
                Checkbox(
                    checked = isEnabledColorMode,
                    onCheckedChange = {isEnabledColorMode = !isEnabledColorMode},
                    colors = CheckboxDefaults.colors(
                        checkedColor = theme.primaryColor,
                        uncheckedColor = theme.primaryColor,
                        checkmarkColor = theme.textColor,
                    )
                )
            }

            SettingField(text = "Ignore pairs for saving") {
                Box(){
                    Button(
                        onClick = { showSavingPopUp = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = theme.primaryColor,
                            contentColor = theme.textColor
                        ),
                        elevation = ButtonDefaults.elevatedButtonElevation(6.dp)
                    ) {
                        Text(
                            text = "Choose"
                        )
                    }
                    when(val screenState = getScreenState()){
                        is Loading -> {
                            CircularProgressIndicator(
                                color = theme.textColor
                            )
                        }
                        is Success ->{
                            DropdownMenu(
                                expanded = showSavingPopUp,
                                onDismissRequest = { showSavingPopUp = false },
                                containerColor = theme.primaryColor,
                                shadowElevation = 3.dp,
                                modifier = Modifier.fillMaxHeight(0.33f)
                            ) {
                                screenState.pairCoins.forEach{
                                    DropdownMenuItem(
                                        onClick = { onChangeIgnoreValueClicked.invoke(it.id) },
                                        text = { Text( text = it.tradePair) },
                                        trailingIcon = {
                                            if(it.ignoreWhenSaving) {
                                                Icon(
                                                    painter = painterResource(R.drawable.selected_icon),
                                                    contentDescription = stringResource(R.string.selected),
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
            SettingField(text = "Reset Database") {
                Button(
                    onClick = {showDialog = true},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = theme.primaryColor,
                        contentColor = theme.textColor
                    ),
                    elevation = ButtonDefaults.elevatedButtonElevation(6.dp),
                ) {
                    Text(
                        text = "Reset",
                    )
                }
            }
        }
        CustomButton(
            text = "Apply",
        ) {
            if (isDarkTheme != initIsDarkTheme){
                themeController.toggle()
                initIsDarkTheme = isDarkTheme
            }
            if(isEnabledColorMode != initIsEnabledMode){
                modeController.toggle()
                initIsEnabledMode = isEnabledColorMode
            }
        }
    }
    ConfirmationDialog(
        showDialog = showDialog,
        onConfirm = {
            showDialog = false
            resetClicked()
        },
        onDismiss = {
            showDialog = false
        },
        label = stringResource(R.string.reset_all_data_in_database)
    )
}

@Composable
fun SettingField(
    text: String,
    content: @Composable () -> Unit
){
    val theme = LocalAppTheme.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            fontSize = 22.sp,
            color = theme.textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(0.6f)
        )
            content()
    }
}
