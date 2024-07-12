package com.example.tunetide.ui.mp3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tunetide.R
import com.example.tunetide.ui.AppViewModelProvider
import com.example.tunetide.ui.TuneTideTopAppBarBack
import com.example.tunetide.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object MP3PlaylistEntryDestination : NavigationDestination {
    override val route = "mp3PlaylistEntry"
    override val titleRes = R.string.mp3_playlist_entry_name
}

@Composable
fun MP3PlaylistEntryScreen(
    navigateBack: () -> Unit, //TODO: can navigate back
    modifier: Modifier = Modifier,
    viewModel: MP3PlaylistEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        modifier = modifier,
        topBar = {
            TuneTideTopAppBarBack(navigateBack)
        }
    )  { innerPadding ->
        MP3PlaylistEntryBody(
            mp3PlaylistUIState = viewModel.mp3PlaylistUIState,
            onPlaylistValueChange = viewModel::updateUIState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveItem()
                    navigateBack()
                }
            },
            modifier = Modifier.padding(innerPadding)
        ) }
}

@Composable
fun MP3PlaylistEntryBody(
    mp3PlaylistUIState: MP3PlaylistUIState,
    onPlaylistValueChange: (MP3PlaylistDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp), // not sure if this is needed
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = 64.dp), // not sure if this is needed
            verticalArrangement = Arrangement.Center
        ) {
             MP3PlaylistInputForm(
                 mp3PlaylistDetails = mp3PlaylistUIState.mp3PlaylistDetails,
                 onValueChange = onPlaylistValueChange,
                 modifier = Modifier.fillMaxWidth()
             )
        }

        IconButton(
            onClick = onSaveClick,
            enabled = mp3PlaylistUIState.isEntryValid,
            modifier = Modifier
                //.padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                painter =
                if (mp3PlaylistUIState.isEntryValid) {
                    painterResource(R.drawable.save)
                } else {
                    painterResource(R.drawable.savegrey)
                },
                contentDescription = stringResource(R.string.save),
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
fun MP3PlaylistInputForm(
    mp3PlaylistDetails: MP3PlaylistDetails,
    onValueChange: (MP3PlaylistDetails) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = mp3PlaylistDetails.playlistName,
            onValueChange = {
                onValueChange(mp3PlaylistDetails.copy(playlistName = it))
            },
            label = { Text(stringResource(R.string.playlist_name_req)) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = MaterialTheme.colors.onSurface,
                focusedBorderColor = MaterialTheme.colors.secondary,
                unfocusedBorderColor = MaterialTheme.colors.secondary,
                disabledBorderColor = MaterialTheme.colors.secondary,
                backgroundColor = MaterialTheme.colors.surface.copy(alpha = TextFieldDefaults.BackgroundOpacity),
                cursorColor = MaterialTheme.colors.secondary,
                focusedLabelColor = MaterialTheme.colors.secondary
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
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

        if (enabled) {
            Text(
                text = stringResource(R.string.required_fields),
                color = Color.Red, // Set text color explicitly
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

