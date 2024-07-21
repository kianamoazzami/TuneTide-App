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
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tunetide.R
import com.example.tunetide.ui.TuneTideTopAppBar
import com.example.tunetide.ui.AppViewModelProvider
import com.example.tunetide.ui.mp3.LocalFilesViewModel
import com.example.tunetide.ui.navigation.NavigationDestination
import com.example.tunetide.ui.theme.PurpleBackground
import com.example.tunetide.ui.theme.PurpleDark
import com.example.tunetide.ui.theme.PurpleLight
import kotlinx.coroutines.launch

object TimerEditDestination : NavigationDestination {
    override val route = "timer_edit"
    override val titleRes = R.string.timer_edit_name
    const val timerIdArg = "timerId"
    val routeWithArgs = "$route/{$timerIdArg}"
}

@Composable
fun TimerEditScreen(
    navigateBack: () -> Unit,
    navigateToEditTimer: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TimerEditViewModel = viewModel(factory = AppViewModelProvider.Factory),
    localFilesViewModel: LocalFilesViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TuneTideTopAppBar()
        }
    ) { innerPadding ->
        TimerEditBody(
            timerUIState = viewModel.timerUIState,
            onTimerValueChange = viewModel::updateUIState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateTimer()
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
fun TimerEditBody(
    timerUIState: TimerUIState,
    onTimerValueChange: (TimerDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    localFilesViewModel: LocalFilesViewModel,
    viewModel: TimerEditViewModel
){
    Box(modifier = modifier) {
        Column(modifier = modifier) {
            Row(modifier = modifier.padding(16.dp)) {
                flowStateFormEdit(timerUIState, modifier, localFilesViewModel, onTimerValueChange, viewModel)
            }
            Row(modifier = modifier.padding(16.dp)) {
                breakStateFormEdit(timerUIState, modifier, localFilesViewModel, onTimerValueChange, viewModel)
            }
            Row(modifier = modifier.padding(16.dp)) {
                nameFieldEdit(timerUIState, modifier, onTimerValueChange)
            }
            Row(modifier = modifier.padding(16.dp)) {
                intervalOptionEdit(timerUIState, modifier, onTimerValueChange)
            }
            Row(modifier = modifier
                .padding(16.dp)
                .align(Alignment.End)) {
                saveButtonEdit(timerUIState, onSaveClick, modifier)
            }
        }
    }
}

@Composable
fun flowStateFormEdit(
    timerUIState : TimerUIState,
    modifier: Modifier,
    localFilesViewModel: LocalFilesViewModel,
    onTimerValueChange: (TimerDetails) -> Unit,
    viewModel: TimerEditViewModel
){
    val state = "Flow"
    stateOptionsEdit(state, timerUIState, modifier, localFilesViewModel, onTimerValueChange, viewModel)
}

@Composable
fun breakStateFormEdit(
    timerUIState : TimerUIState,
    modifier: Modifier,
    localFilesViewModel: LocalFilesViewModel,
    onTimerValueChange: (TimerDetails) -> Unit,
    viewModel: TimerEditViewModel
){
    val state = "Break"
    stateOptionsEdit(state, timerUIState, modifier, localFilesViewModel, onTimerValueChange, viewModel)
}

@Composable
fun intervalOptionEdit(
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
        var intervalNum by remember { mutableStateOf(0) }
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
fun nameFieldEdit(
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
fun stateOptionsEdit(
    state: String,
    timerUIState : TimerUIState,
    modifier: Modifier,
    localFilesViewModel : LocalFilesViewModel,
    onTimerValueChange: (TimerDetails) -> Unit,
    viewModel: TimerEditViewModel
){
    Column(modifier = modifier) {
        timeSelectEdit(state, timerUIState, modifier, onTimerValueChange, viewModel)
        playlistSelectEdit(state, timerUIState, modifier, localFilesViewModel, onTimerValueChange, viewModel)
    }
}

@Composable
fun timeSelectEdit(
    state: String,
    timerUIState : TimerUIState,
    modifier: Modifier,
    onTimerValueChange: (TimerDetails) -> Unit,
    viewModel: TimerEditViewModel
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
            var timeVal by remember { mutableStateOf(0) }
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
            var timeVal by remember { mutableStateOf(0) }
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
fun playlistSelectEdit(
    state: String,
    timerUIState : TimerUIState,
    modifier: Modifier,
    localFilesViewModel : LocalFilesViewModel,
    onTimerValueChange: (TimerDetails) -> Unit,
    viewModel: TimerEditViewModel
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

        if (spotify) {
            // SPOTIFY VIEW
            var expanded by remember { mutableStateOf(false) }
            var options = viewModel.spotifyRepository.getSpotifyPlaylists().collectAsState(initial = emptyList()).value
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
                        .width(225.dp)
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
                                text = { option.playlistName.toString() },
                                onClick = {
                                    selectedOption = option
                                    chosenOptionName = selectedOption.playlistName.toString()
                                    if (state == "Flow") {
                                        onTimerValueChange(timerUIState.timerDetails.copy(spotifyFlowMusicPlaylistId = selectedOption.playlistId, mp3FlowMusicPlaylistId = -1))
                                    }
                                    else {
                                        onTimerValueChange(timerUIState.timerDetails.copy(spotifyBreakMusicPlaylistId = selectedOption.playlistId, mp3BreakMusicPlaylistId = -1))
                                    }
                                    expanded = false
                                },
                            )
                        }
                    }
                }
                var shuffle by remember { mutableStateOf(false) }
                Column( modifier = Modifier.padding(start = 8.dp)) {
                    Text(
                        text = "Shuffle",
                        color = PurpleDark,
                        modifier = Modifier.padding(end = 8.dp),
                    )
                    Checkbox(
                        checked = shuffle,
                        onCheckedChange = {
                            shuffle = !shuffle
                        }
                    )
                }
            }
        }
        else {
            // MP3 VIEW
            var expanded by remember { mutableStateOf(false) }
            var options = localFilesViewModel.localFilesUIState.collectAsState().value.mp3Playlists
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
                        .width(225.dp)
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
                                        onTimerValueChange(timerUIState.timerDetails.copy(mp3FlowMusicPlaylistId = selectedOption.playlistId, spotifyFlowMusicPlaylistId = -1))
                                    }
                                    else {
                                        onTimerValueChange(timerUIState.timerDetails.copy(mp3BreakMusicPlaylistId = selectedOption.playlistId, spotifyBreakMusicPlaylistId = -1))
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
}

@Composable
fun saveButtonEdit(timerUIState: TimerUIState, onSaveClick: () -> Unit, modifier: Modifier) {
    if (!timerUIState.isValidEntry) {
        Text(
            text = "Incomplete Input",
            color = Color.Red,
            modifier = Modifier.padding(8.dp),
        )
    }
    Button(onClick = onSaveClick, colors = ButtonDefaults.buttonColors(PurpleDark)) {
        Text(
            text = "Save",
            color = PurpleBackground,
            modifier = Modifier.padding(8.dp),
        )
    }

}
