package com.example.masterproject.ui.screens.exchangerate.edititem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.masterproject.R
import com.example.masterproject.ui.screens.EventConsumer
import com.example.masterproject.ui.screens.LocalNavController
import com.example.masterproject.ui.screens.exchangerate.edititem.EditItemViewModel.ScreenState
import com.example.masterproject.ui.components.CustomButton
import com.example.masterproject.ui.screens.ExchangeRateGraphs
import com.example.masterproject.ui.screens.ExchangeRateGraphs.EditRoute
import com.example.masterproject.ui.screens.NavigateUpAction
import com.example.masterproject.ui.screens.routeClass

@Preview(showSystemUi = true)
@Composable
fun EditItemPreview(){
    EditItemContent(
        onBackPressed = { },
        screenState = ScreenState(),
        onRemoveButtonClicked = { },
        onAddButtonClicked = { }
    )
}

@Composable
fun EditItemScreen(
    returnTitle: (Int) -> Unit,
    returnNavigateUpAction: (NavigateUpAction) -> Unit
){
    val viewModel: EditItemViewModel = hiltViewModel()
    val navController = LocalNavController.current
    val screenState by viewModel.stateFlow.collectAsState()

    LaunchedEffect(Unit) {
        returnTitle.invoke(R.string.edit_menu)
        returnNavigateUpAction.invoke(
            NavigateUpAction.VisibleNavigate(
                onNavigateButtonPressed = { navController.popBackStack()}
            ))
    }


    EditItemContent(
        onBackPressed = {
            if (navController.currentBackStackEntry?.routeClass() == EditRoute::class){
            navController.popBackStack()
        }},
        screenState = screenState,
        onAddButtonClicked = viewModel::addPair,
        onRemoveButtonClicked = viewModel::removePair
    )

    EventConsumer(viewModel.exitChannel) {
        if (navController.currentBackStackEntry?.routeClass() == EditRoute::class){
            navController.popBackStack()
        }
    }
}

@Composable
fun EditItemContent(
    onBackPressed: () -> Unit,
    screenState: ScreenState,
    onAddButtonClicked: (String) -> Unit,
    onRemoveButtonClicked: (String) -> Unit
){
    val hintText = stringResource(R.string.enter_the_pair)

    var textValueForAdding by rememberSaveable {
        mutableStateOf("")
    }
    var textValueForDeleting by rememberSaveable {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    OutlinedTextField(
                        value = textValueForAdding,
                        onValueChange = { newValue ->
                            textValueForAdding = newValue
                        },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f),
                        placeholder = { Text(text = hintText) },
                        enabled = screenState.isTextInputEnabled
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {onAddButtonClicked.invoke(textValueForAdding)},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF4F4F4),
                            contentColor = Color.Black
                        ),
                        enabled = screenState.isButtonEnabled(textValueForAdding),
                    ) {
                        Text(
                            text = stringResource(R.string.add)
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedTextField(
                        value = textValueForDeleting,
                        onValueChange = { newValue ->
                            textValueForDeleting = newValue
                        },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f),
                        placeholder = { Text(text = hintText) },
                        enabled = screenState.isTextInputEnabled
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { onRemoveButtonClicked.invoke(textValueForDeleting) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF4F4F4),
                            contentColor = Color.Black
                        ),
                        enabled = screenState.isButtonEnabled(textValueForDeleting),
                    ) {
                        Text(
                            text = stringResource(R.string.delete),
                        )
                    }
                }
            }
            Box(modifier = Modifier.size(64.dp)){
                if (screenState.isProgressVisible){
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    )
                }
            }

            CustomButton(
                text = "Back",
            ) { onBackPressed.invoke() }
        }
    }
}
