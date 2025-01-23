package com.example.masterproject.ui.screens.storage

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
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
import com.example.masterproject.ui.components.LocalAppTheme
import com.example.masterproject.ui.tools.LocalNavController
import com.example.masterproject.ui.screens.MainGraph
import com.example.masterproject.ui.components.NavigateUpAction
import com.example.masterproject.ui.screens.storage.StorageScreenViewModel.ScreenState
import com.example.masterproject.ui.tools.formatUnixTimeMillis

@Composable
@Preview(showSystemUi = true)
fun StorageScreenPreview(){
    StorageContent(
        getScreenState = {ScreenState.Loading},
        deleteSnapshot = { }
    )
}

@Composable
fun StorageScreen(
    returnTitle: (Int) -> Unit,
    returnNavigateUpAction: (NavigateUpAction) -> Unit
){
    val currentReturnTitle = rememberUpdatedState(returnTitle)
    val currentReturnNavigateUpAction = rememberUpdatedState(returnNavigateUpAction)
    val viewModel: StorageScreenViewModel = hiltViewModel()
    val navController = LocalNavController.current
    val screenState = viewModel.stateFlow.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    currentReturnTitle.value.invoke(R.string.storage)
                    currentReturnNavigateUpAction.value.invoke(
                        NavigateUpAction.VisibleNavigateAndAction(
                            onNavigateButtonPressed = { navController.navigate(MainGraph){
                                popUpTo(0) { inclusive = true }
                                restoreState = true
                            } },
                            onActionButtonPressed = {
                                Toast.makeText(context,
                                    context.getString(R.string.feature_in_planning),
                                    Toast.LENGTH_LONG)
                                    .show()
                            }
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

    StorageContent(
        getScreenState = {screenState.value},
        deleteSnapshot = viewModel::deleteSnapshotById
    )
}

@Composable
fun StorageContent(
    getScreenState: () -> ScreenState,
    deleteSnapshot: (Long) -> Unit
) {
    val theme = LocalAppTheme.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(theme.bgColor)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 26.dp),
            colors = CardDefaults.cardColors(containerColor = theme.secondaryColor),
            elevation = CardDefaults.elevatedCardElevation(6.dp)
        ) {
            when(val screenState = getScreenState()){
                is ScreenState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally),
                        color = theme.textColor
                    )
                }
                is ScreenState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(screenState.marketSnapshots.size){index ->
                            //ContainerForData(it)
                            var showDialog by remember { mutableStateOf(false) }
                            Card(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 16.dp)
                                    .fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = theme.primaryColor,
                                    contentColor = theme.textColor)
                            ) {
                                Column (
                                    modifier = Modifier
                                        .heightIn(min = 100.dp, max = 900.dp)
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ){
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Text(
                                            text = "Data ${screenState.marketSnapshots[index].marketSnapshot.id}",
                                            fontSize = 24.sp
                                        )
                                        IconButton(
                                            onClick = { showDialog = true },
                                            modifier = Modifier
                                                .align(Alignment.CenterEnd)
                                                .padding(end = 8.dp)
                                            ) {
                                            Icon(
                                                painter = painterResource(R.drawable.close_icon),
                                                contentDescription = stringResource(R.string.close),
                                                modifier = Modifier.size(20.dp),
                                                tint = Color.Unspecified
                                            )
                                            ConfirmationDialog(
                                                showDialog = showDialog,
                                                onConfirm = {
                                                    showDialog = false
                                                    deleteSnapshot.invoke(screenState.marketSnapshots[index].marketSnapshot.id)
                                                },
                                                onDismiss = {
                                                    showDialog = false
                                                },
                                                label = stringResource(R.string.delete_this_item_from_database)
                                            )
                                        }

                                    }
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

@Composable
fun ConfirmationDialog(
    showDialog: Boolean,
    label: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val theme = LocalAppTheme.current
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = "Apply")
            },
            text = {
                Text(text = label)
            },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(text = "Yes",
                        color = theme.textColor)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = "No",
                        color = theme.textColor)
                }
            }
        )
    }
}