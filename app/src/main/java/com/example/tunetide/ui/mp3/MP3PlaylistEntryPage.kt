package com.example.tunetide.ui.mp3

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
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

object MP3PlaylistEntryDestination : NavigationDestination {
    override val route = "mp3PlaylistEntry"
    override val titleRes = R.string.mp3_playlist_entry_name
}

@Composable
fun MP3PlaylistEntryScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MP3PlaylistEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TuneTideTopAppBarBack(navigateBack)
        }
    )  { innerPadding ->
        MP3PlaylistEntryBody(
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding
        ) }
}

fun MP3PlaylistEntryBody(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp)
) {

}