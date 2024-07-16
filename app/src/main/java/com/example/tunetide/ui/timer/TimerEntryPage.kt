package com.example.tunetide.ui.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.RadioButton
import androidx.compose.material.TextField
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tunetide.R
import com.example.tunetide.ui.TuneTideTopAppBar
import com.example.tunetide.ui.AppViewModelProvider
import com.example.tunetide.ui.mp3.LocalFilesViewModel
import com.example.tunetide.ui.navigation.NavigationDestination
import com.example.tunetide.ui.theme.PurpleAccent
import com.example.tunetide.ui.theme.PurpleDark
import com.example.tunetide.ui.theme.PurpleLight
import com.example.tunetide.ui.theme.PurpleLight2
import kotlinx.coroutines.launch

object TimerEntryDestination : NavigationDestination {
    override val route = "timer_entry"
    override val titleRes = R.string.timer_entry_name
}

@Composable
fun TimerEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    modifier: Modifier = Modifier,
    viewModel: TimerEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    localFilesViewModel: LocalFilesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TuneTideTopAppBar()
        }
    ) { innerPadding ->
        TimerEntryBody(
            timerUIState = viewModel.timerUIState,
            onTimerValueChange = viewModel::updateUIState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.insertTimer()
                    //navigateBack()
                }
            },
            modifier = modifier,
            innerPadding,
            localFilesViewModel = localFilesViewModel,
            viewModel = viewModel
        )
    }
}

@Composable
fun TimerEntryBody(
    timerUIState: TimerUIState,
    onTimerValueChange: (TimerDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    localFilesViewModel: LocalFilesViewModel,
    viewModel: TimerEntryViewModel
){
    Box(modifier = modifier) {
        Column(modifier = modifier) {
            Row(modifier = modifier.padding(16.dp)) {
                flowStateForm(timerUIState, modifier, localFilesViewModel)
            }
            Row(modifier = modifier.padding(16.dp)) {
                breakStateForm(timerUIState, modifier, localFilesViewModel)
            }
            Row(modifier = modifier.padding(16.dp)) {
                nameField(timerUIState, modifier)
            }
            Row(modifier = modifier.padding(16.dp)) {
                intervalOption(timerUIState, modifier, viewModel)
            }
        }
    }
}

@Composable
fun flowStateForm(
    timerUIState : TimerUIState,
    modifier: Modifier,
    localFilesViewModel: LocalFilesViewModel
){
    val state = "Flow"
    stateOptions(state, timerUIState, modifier, localFilesViewModel)
}

@Composable
fun breakStateForm(
    timerUIState : TimerUIState,
    modifier: Modifier,
    localFilesViewModel: LocalFilesViewModel
){
    val state = "Break"
    stateOptions(state, timerUIState, modifier, localFilesViewModel)
}

@Composable
fun intervalOption(
    timerUIState : TimerUIState,
    modifier: Modifier,
    viewModel: TimerEntryViewModel
){
    val focusManager = LocalFocusManager.current
    val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        textColor = MaterialTheme.colors.onSurface,
        focusedBorderColor = MaterialTheme.colors.secondary,
        unfocusedBorderColor = MaterialTheme.colors.secondary,
        disabledBorderColor = MaterialTheme.colors.secondary,
        backgroundColor = MaterialTheme.colors.surface.copy(alpha = TextFieldDefaults.BackgroundOpacity),
        cursorColor = MaterialTheme.colors.secondary,
        focusedLabelColor = MaterialTheme.colors.secondary)

    Column() {
        Text(
            text = "Interval Timer",
            color = PurpleDark,
            modifier = Modifier.padding(start = 8.dp)
        )
        Checkbox(
            checked = viewModel.timerUIState.timerDetails.isInterval,
            onCheckedChange = { /*UPDATE TIMER*/ }
        )

        if (true) {
            Row() {
                Text(
                    text = "Number of Intervals: ",
                    color = PurpleDark,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.CenterVertically)
                )
                OutlinedTextField(
                    value = "",
                    onValueChange = {
                        // UPDATE TIMER
                    },
                    label = { Text("") },
                    colors = textFieldColors,
                    modifier = modifier
                        .width(50.dp)
                        .height(50.dp),
                    //enabled = enabled,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    )
                )
            }
        }
    }
}

@Composable
fun nameField(
    timerUIState : TimerUIState,
    modifier: Modifier
){
    val focusManager = LocalFocusManager.current
    val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        textColor = MaterialTheme.colors.onSurface,
        focusedBorderColor = MaterialTheme.colors.secondary,
        unfocusedBorderColor = MaterialTheme.colors.secondary,
        disabledBorderColor = MaterialTheme.colors.secondary,
        backgroundColor = MaterialTheme.colors.surface.copy(alpha = TextFieldDefaults.BackgroundOpacity),
        cursorColor = MaterialTheme.colors.secondary,
        focusedLabelColor = MaterialTheme.colors.secondary)

        Text(
            text = "Timer Name: ",
            color = PurpleDark,
            modifier = Modifier.padding(start = 8.dp)
        )
        OutlinedTextField(
            value = "",
            onValueChange = {
                // UPDATE TIMER
            },
            label = { Text("") },
            colors = textFieldColors,
            modifier = modifier
                .fillMaxWidth()
                .height(20.dp),
            //enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun stateOptions(
    state: String,
    timerUIState : TimerUIState,
    modifier: Modifier,
    localFilesViewModel : LocalFilesViewModel
){
    Column(modifier = modifier) {
        timeSelect(state, timerUIState, modifier)
        playlistSelect(state, timerUIState, modifier, localFilesViewModel)
    }
}

@Composable
fun timeSelect(
    state: String,
    timerUIState : TimerUIState,
    modifier: Modifier,
) {
    val focusManager = LocalFocusManager.current
    val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        textColor = MaterialTheme.colors.onSurface,
        focusedBorderColor = MaterialTheme.colors.secondary,
        unfocusedBorderColor = MaterialTheme.colors.secondary,
        disabledBorderColor = MaterialTheme.colors.secondary,
        backgroundColor = MaterialTheme.colors.surface.copy(alpha = TextFieldDefaults.BackgroundOpacity),
        cursorColor = MaterialTheme.colors.secondary,
        focusedLabelColor = MaterialTheme.colors.secondary)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = state + " State",
            color = PurpleDark,
            modifier = Modifier.padding(16.dp),
        )
        //HOURS
        OutlinedTextField(
            value = "",
            onValueChange = {
                // UPDATE TIMER
            },
            label = { Text("") },
            colors = textFieldColors,
            modifier = modifier
                .width(50.dp)
                .height(50.dp),
            //enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )
        Text(
            text = " : ",
            color = PurpleDark,
            modifier = Modifier.padding(16.dp)
        )
        //MINUTES
        OutlinedTextField(
            value = "",
            onValueChange = {
                // UPDATE TIMER
            },
            label = { Text("") },
            colors = textFieldColors,
            modifier = modifier
                .width(50.dp)
                .height(50.dp),
            //enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun playlistSelect(
    state: String,
    timerUIState : TimerUIState,
    modifier: Modifier,
    localFilesViewModel : LocalFilesViewModel
) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
    ) {

        var expanded by remember { mutableStateOf(false) }
        val options = localFilesViewModel.localFilesUIState.collectAsState().value.mp3Playlists
        if (options.isEmpty()) {
            Text(
                text = "No Playlists Available",
                color = PurpleDark,
                modifier = Modifier.padding(8.dp),
            )
        }
        else {
            Text(
                text = "Playlist",
                color = PurpleDark,
                modifier = Modifier.padding(8.dp),
            )
            var selectedOption by remember { mutableStateOf(options[0]) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                modifier = modifier
                    .background(PurpleLight)
                    .height(50.dp)
            ) {
                TextField(
                    value = selectedOption.playlistName,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    modifier = Modifier
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { option.playlistName },
                            onClick = {
                                selectedOption = option
                                // UPDATE TIMER
                                expanded = false
                            },
                        )
                    }
                }
            }
        }
    }
}
