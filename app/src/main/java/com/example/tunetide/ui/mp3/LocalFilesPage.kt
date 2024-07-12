package com.example.tunetide.ui.mp3

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tunetide.R
import com.example.tunetide.database.MP3Playlist
import com.example.tunetide.ui.AppViewModelProvider
import com.example.tunetide.ui.TuneTideTopAppBarBack
import com.example.tunetide.ui.navigation.NavigationDestination

object LocalFilesDestination : NavigationDestination {
    override val route = "localFiles"
    override val titleRes = R.string.local_files_page_name
}

@Composable
fun LocalFilesScreen(
    navigateBack: () -> Unit,
    navigateToMP3PlaylistEntry: () -> Unit,
    navigateToMP3PlaylistUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LocalFilesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val localFilesUIState by viewModel.localFilesUIState.collectAsState()

    Scaffold(
        //TODO: add scrolling if it doesnt work already
        modifier = modifier,
        topBar = {
            TuneTideTopAppBarBack(navigateBack)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToMP3PlaylistEntry,
                shape = CircleShape,
                backgroundColor = Color.Transparent,
                elevation = FloatingActionButtonDefaults.elevation(0.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = "Create MP3 Playlist",
                    tint = Color.Unspecified
                )
            }
        }
    )  { innerPadding ->
        LocalFilesBody(
            mp3Playlists = localFilesUIState.mp3Playlists,
            onPlaylistClick = navigateToMP3PlaylistUpdate,
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding
        ) }
}

@Composable
fun LocalFilesBody(
    mp3Playlists: List<MP3Playlist>,
    onPlaylistClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (mp3Playlists.isEmpty()) {
            Text(
                text = "No Playlists", //temp
                modifier = Modifier.padding(contentPadding),
            )
        } else {
            MP3PlaylistList(
                mp3Playlists = mp3Playlists,
                onPlaylistClick = onPlaylistClick,
                contentPadding = contentPadding,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}

@Composable
private fun MP3PlaylistList(
    mp3Playlists: List<MP3Playlist>,
    onPlaylistClick: (Int) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = mp3Playlists, key = { it.playlistId }) { mp3Playlist ->
            MP3PlaylistItem(
                mp3Playlist = mp3Playlist,
                onClick = { onPlaylistClick(mp3Playlist.playlistId) },
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun MP3PlaylistItem(
    mp3Playlist: MP3Playlist,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = mp3Playlist.playlistName,
                    style = MaterialTheme.typography.h6,
                )
                Spacer(Modifier.weight(1f))
            }
        }
    }
}