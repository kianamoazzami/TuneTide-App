package com.example.tunetide.ui.mp3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tunetide.R
import com.example.tunetide.database.MP3Playlist
import com.example.tunetide.ui.AppViewModelProvider
import com.example.tunetide.ui.TuneTideTopAppBarBack
import com.example.tunetide.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object MP3PlaylistDetailsDestination : NavigationDestination {
    override val route = "mp3_playlist_details"
    override val titleRes = R.string.mp3_playlist_details_name
    const val mp3PlaylistIdArg = "mp3PlaylistId"
    val routeWithArgs = "$route/{$mp3PlaylistIdArg}"
}

//TODO: display songs and playlist name (details)

//TODO IF TIME: MP3PlaylistEditPage - add ability to change name of playlist and maybe change songs?
//this could break things

@Composable
fun MP3PlaylistDetailsScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MP3PlaylistDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TuneTideTopAppBarBack(navigateBack)
        },
        modifier = modifier,
    ) { innerPadding ->
        MP3PlaylistDetailsBody(
            mp3PlaylistDetailsUIState = uiState.value,
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteMP3Playlist()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                )
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
private fun MP3PlaylistDetailsBody(
    mp3PlaylistDetailsUIState: MP3PlaylistDetailsUIState,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    //TODO: change to lazy column for scrolling
    Column(
        modifier = modifier.padding(16.dp), //not sure
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
        MP3PlaylistDetails(
            mp3Playlist = mp3PlaylistDetailsUIState.mp3PlaylistDetails.toMP3Playlist(),
            modifier = Modifier.fillMaxWidth()
        )
        IconButton(
            onClick = { deleteConfirmationRequired = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.deleteplaylist),
                contentDescription = stringResource(R.string.delete),
                tint = Color.Unspecified
            )
        }
        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

//TODO
@Composable
fun MP3PlaylistDetails(
    mp3Playlist: MP3Playlist,
    modifier: Modifier = Modifier
) {

}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit, onDeleteCancel: () -> Unit, modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = stringResource(R.string.yes))
            }
        })
}