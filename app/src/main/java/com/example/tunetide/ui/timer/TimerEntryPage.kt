package com.example.tunetide.ui.timer

import android.util.Log
import android.widget.NumberPicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tunetide.R
import com.example.tunetide.ui.TuneTideTopAppBar
import com.example.tunetide.ui.AppViewModelProvider
import com.example.tunetide.ui.TuneTideTopAppBarBack
import com.example.tunetide.ui.mp3.LocalFilesViewModel
import com.example.tunetide.ui.navigation.NavigationDestination
import com.example.tunetide.ui.theme.PurpleAccent
import com.example.tunetide.ui.theme.PurpleBackground
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
            TuneTideTopAppBarBack(navigateBack)
        }
    ) { innerPadding ->
        TimerEntryBody(
            timerUIState = viewModel.timerUIState,
            onTimerValueChange = viewModel::updateUIState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.insertTimer()
                    Log.d("Timer: ", viewModel.timerUIState.toString())
                    navigateBack()
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
                flowStateForm(timerUIState, modifier, localFilesViewModel, onTimerValueChange, viewModel)
            }
            Row(modifier = modifier.padding(16.dp)) {
                breakStateForm(timerUIState, modifier, localFilesViewModel, onTimerValueChange, viewModel)
            }
            Row(modifier = modifier.padding(16.dp)) {
                nameField(timerUIState, modifier, onTimerValueChange)
            }
            Row(modifier = modifier.padding(16.dp)) {
                intervalOption(timerUIState, modifier, onTimerValueChange)
            }
            Row(modifier = modifier
                .padding(16.dp)
                .align(Alignment.End)) {
                saveButton(onSaveClick, modifier)
            }
        }
    }
}

@Composable
fun flowStateForm(
    timerUIState : TimerUIState,
    modifier: Modifier,
    localFilesViewModel: LocalFilesViewModel,
    onTimerValueChange: (TimerDetails) -> Unit,
    viewModel: TimerEntryViewModel
){
    val state = "Flow"
    stateOptions(state, timerUIState, modifier, localFilesViewModel, onTimerValueChange, viewModel)
}

@Composable
fun breakStateForm(
    timerUIState : TimerUIState,
    modifier: Modifier,
    localFilesViewModel: LocalFilesViewModel,
    onTimerValueChange: (TimerDetails) -> Unit,
    viewModel: TimerEntryViewModel
){
    val state = "Break"
    stateOptions(state, timerUIState, modifier, localFilesViewModel, onTimerValueChange, viewModel)
}

@Composable
fun intervalOption(
    timerUIState : TimerUIState,
    modifier: Modifier,
    onTimerValueChange: (TimerDetails) -> Unit
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
            checked = timerUIState.timerDetails.isInterval,
            onCheckedChange = {
                onTimerValueChange(timerUIState.timerDetails.copy(isInterval = !timerUIState.timerDetails.isInterval))
            }
        )
    }
    if (timerUIState.timerDetails.isInterval) {
        Box(modifier = modifier.height(60.dp),
            contentAlignment = Alignment.Center) {
            Text(
                text = "Number of Intervals: ",
                color = PurpleDark,
                modifier = Modifier
                    .padding(start = 26.dp),
            )
        }
        var intervalNum by remember {mutableStateOf(0)}
        OutlinedTextField(
            value = intervalNum.toString(),
            onValueChange = {
                intervalNum = it.toInt()
            },
            label = { Text("") },
            colors = textFieldColors,
            modifier = modifier
                .width(60.dp)
                .height(60.dp),
            //enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onTimerValueChange(timerUIState.timerDetails.copy(numIntervals = intervalNum))
                }
            )
        )

    }
}

@Composable
fun nameField(
    timerUIState : TimerUIState,
    modifier: Modifier,
    onTimerValueChange: (TimerDetails) -> Unit
){
    val focusManager = LocalFocusManager.current
    val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        textColor = PurpleDark,
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
            value = timerUIState.timerDetails.timerName,
            onValueChange = {
                onTimerValueChange(timerUIState.timerDetails.copy(timerName = it))
            },
            label = { Text("") },
            colors = textFieldColors,
            modifier = modifier
                .fillMaxWidth()
                .height(60.dp),
            enabled = true,
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
    localFilesViewModel : LocalFilesViewModel,
    onTimerValueChange: (TimerDetails) -> Unit,
    viewModel: TimerEntryViewModel
){
    Column(modifier = modifier) {
        timeSelect(state, timerUIState, modifier, onTimerValueChange, viewModel)
        playlistSelect(state, timerUIState, modifier, localFilesViewModel, onTimerValueChange)
    }
}

@Composable
fun timeSelect(
    state: String,
    timerUIState : TimerUIState,
    modifier: Modifier,
    onTimerValueChange: (TimerDetails) -> Unit,
    viewModel: TimerEntryViewModel
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
        Column() {
            var timeVal by remember {mutableStateOf(0)}
            OutlinedTextField(
                value = timeVal.toString(),
                onValueChange = {
                    if (it.isNotEmpty()) {
                        timeVal = it.toInt()
                    }
                    else {
                        timeVal = 0
                    }
                },
                label = { Text("") },
                colors = textFieldColors,
                modifier = modifier
                    .width(60.dp)
                    .height(60.dp),
                //enabled = enabled,
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        if (state == "Flow") {
                            onTimerValueChange(timerUIState.timerDetails.copy(
                                flowMusicDurationSeconds = (timerUIState.timerDetails.flowMusicDurationSeconds % 3600) + (timeVal * 3600)))
                        }
                        else {
                            onTimerValueChange(timerUIState.timerDetails.copy(
                                breakMusicDurationSeconds = (timerUIState.timerDetails.breakMusicDurationSeconds % 3600) + (timeVal * 3600)))
                        }
                    }
                )
            )
            Text(
                text = "Hours",
                color = PurpleDark,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Text(
            text = " : ",
            color = PurpleDark,
            modifier = Modifier.padding(16.dp)
        )
        //MINUTES
        Column() {
            var timeVal by remember {mutableStateOf(0)}
            OutlinedTextField(
                value = timeVal.toString(),
                onValueChange = {
                    if (it.isNotEmpty()) {
                        timeVal = it.toInt()
                    }
                    else {
                        timeVal = 0
                    }
                },
                label = { Text("") },
                colors = textFieldColors,
                modifier = modifier
                    .width(60.dp)
                    .height(60.dp),
                //enabled = enabled,
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        if (state == "Flow") {
                            onTimerValueChange(timerUIState.timerDetails.copy(
                                flowMusicDurationSeconds = (timerUIState.timerDetails.flowMusicDurationSeconds / 3600 ) * 3600 + (timeVal * 60)))
                        }
                        else {
                            onTimerValueChange(timerUIState.timerDetails.copy(
                                breakMusicDurationSeconds = (timerUIState.timerDetails.breakMusicDurationSeconds / 3600 ) * 3600 + (timeVal * 60)))
                        }
                    }
                )
            )
            Text(
                text = "Minutes",
                color = PurpleDark,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun playlistSelect(
    state: String,
    timerUIState : TimerUIState,
    modifier: Modifier,
    localFilesViewModel : LocalFilesViewModel,
    onTimerValueChange: (TimerDetails) -> Unit
) {
    var spotify by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
    ) {
        Column( modifier = Modifier.padding(0.dp)) {
            Text(
                text = "Spotify",
                color = PurpleDark,
                modifier = Modifier.padding(end = 8.dp),
            )
            Checkbox(
                checked = spotify,
                onCheckedChange = {
                    spotify = !spotify
                }
            )
        }

        var expanded by remember { mutableStateOf(false) }
        var options = localFilesViewModel.localFilesUIState.collectAsState().value.mp3Playlists
        if (spotify) {
            // TODO: options = list of spotify playlists
            options = emptyList()
        }
        if (options.isEmpty()) {
            Text(
                text = "No Playlists Available",
                color = PurpleDark,
                modifier = Modifier.padding(8.dp),
            )
        }
        else {
            var selectedOption by remember { mutableStateOf(options[0]) }
            var chosenOptionName by remember { mutableStateOf("Choose a Playlist") }
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
                    value = chosenOptionName,
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
                            text = { Text(option.playlistName) },
                            onClick = {
                                selectedOption = option
                                chosenOptionName = selectedOption.playlistName
                                if (state == "Flow") {
                                    if (spotify) {
                                        onTimerValueChange(timerUIState.timerDetails.copy(spotifyFlowMusicPlaylistId = selectedOption.playlistId))
                                    }
                                    else {
                                        onTimerValueChange(timerUIState.timerDetails.copy(mp3FlowMusicPlaylistId = selectedOption.playlistId))
                                    }
                                }
                                else {
                                    if (spotify) {
                                        onTimerValueChange(timerUIState.timerDetails.copy(spotifyBreakMusicPlaylistId = selectedOption.playlistId))
                                    }
                                    else {
                                        onTimerValueChange(timerUIState.timerDetails.copy(mp3BreakMusicPlaylistId = selectedOption.playlistId))
                                    }
                                }
                                expanded = false
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun saveButton(onSaveClick: () -> Unit, modifier: Modifier) {
    Button(onClick = onSaveClick, colors = ButtonDefaults.buttonColors(PurpleDark)) {
        Text(
            text = "Save",
            color = PurpleBackground,
            modifier = Modifier.padding(8.dp),
        )
    }

}
