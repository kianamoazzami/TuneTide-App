package com.example.tunetide.ui.mp3

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    modifier: Modifier = Modifier,
    viewModel: LocalFilesPageViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val localFilesUIState by viewModel.localFilesUIState.collectAsState()

    Scaffold(
        topBar = {
            TuneTideTopAppBarBack(navigateBack)
        },
        modifier = modifier
    )  { innerPadding ->
        LocalFilesBody(
            mp3Playlists = localFilesUIState.mp3Playlists,
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding
        )
        }
}

@Composable
fun LocalFilesBody(
    mp3Playlists: List<MP3Playlist>,
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
                contentPadding = contentPadding,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun MP3PlaylistList(
    mp3Playlists: List<MP3Playlist>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = mp3Playlists, key = { it.playlistId }) { item ->
            MP3PlaylistItem(item = item,
                modifier = Modifier)
        }
    }
}

@Composable
private fun MP3PlaylistItem(
    item: MP3Playlist, modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.playlistName,
                    style = MaterialTheme.typography.h6,
                )
                Spacer(Modifier.weight(1f))
            }
        }
    }
}