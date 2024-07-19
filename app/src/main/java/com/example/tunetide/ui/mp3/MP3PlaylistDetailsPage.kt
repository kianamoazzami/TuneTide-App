package com.example.tunetide.ui.mp3

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tunetide.R
import com.example.tunetide.database.MP3File
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
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
        MP3PlaylistDetails(
            mp3Playlist = mp3PlaylistDetailsUIState.mp3PlaylistDetails.toMP3Playlist(),
            mp3Files = mp3PlaylistDetailsUIState.mp3Files,
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

@Composable
fun MP3PlaylistDetails(
    mp3Playlist: MP3Playlist,
    mp3Files: List<MP3File>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        backgroundColor = Color(0xE6E5F2),
        contentColor = Color(0x6F6DB3)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ItemDetailsRow(
                labelResID = R.string.playlist,
                textInfo = mp3Playlist.playlistName,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            if (mp3Files.isNotEmpty()) {
                ItemDetailsRow(
                    labelResID = R.string.songs,
                    textInfo = mp3Files[0].fileName.toString(),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                for (i in 1 until mp3Files.size) {
                    ItemDetailsRow(
                        labelResID = R.string.empty,
                        textInfo = mp3Files[i].fileName.toString(),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}


@Composable
private fun ItemDetailsRow(
    @StringRes labelResID: Int, textInfo: String, modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(text = stringResource(labelResID), fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = textInfo)
    }
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