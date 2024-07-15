package com.example.tunetide.ui.mp3

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
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

/* --- helper function --- */
fun getFileName(context: Context, uri: Uri): String? {
    var fileName: String? = null
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (displayNameIndex != -1) {
                fileName = it.getString(displayNameIndex)
            }
        }
    }
    return fileName
}

/* --- start page --- */
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
            mp3FilesUIState = viewModel.mp3FilesUIState,
            onPlaylistValueChange = viewModel::updatePlaylistUIState,
            onAddFiles = viewModel::updateFilesUIState,
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
    mp3FilesUIState: MP3FilesUIState,
    onPlaylistValueChange: (MP3PlaylistDetails) -> Unit,
    onAddFiles: (MutableList<MP3FileDetails>) -> Unit,
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
                 mp3FileDetailsList = mp3FilesUIState.mp3FileDetailsList,
                 onPlaylistValueChange = onPlaylistValueChange,
                 onAddFiles = onAddFiles,
                 modifier = Modifier.fillMaxWidth()
             )
        }

        IconButton(
            onClick = onSaveClick,
            enabled = (mp3PlaylistUIState.isEntryValid && mp3FilesUIState.isEntryValid),
            modifier = Modifier
                //.padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                painter =
                if (mp3PlaylistUIState.isEntryValid && mp3FilesUIState.isEntryValid) {
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
    mp3FileDetailsList: MutableList<MP3FileDetails>,
    onPlaylistValueChange: (MP3PlaylistDetails) -> Unit,
    onAddFiles: (MutableList<MP3FileDetails>) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val focusManager = LocalFocusManager.current
    val curContext = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val activityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        coroutineScope.launch {
            if (result.resultCode == Activity.RESULT_OK) {
                val clipData = result.data?.clipData
                if (clipData != null) {
                    for (i in 0 until clipData.itemCount) {
                        val uri = clipData.getItemAt(i).uri
                        val fileName = getFileName(curContext, uri)
                        mp3FileDetailsList.add(
                            MP3FileDetails(
                                fileName = fileName,
                                filePath = uri.toString()
                            )
                        )
                    }
                } else {
                    result.data?.data?.let { uri ->
                        val fileName = getFileName(curContext, uri)
                        mp3FileDetailsList.add(
                            MP3FileDetails(
                                fileName = fileName,
                                filePath = uri.toString()
                            )
                        )
                    }
                }
                onAddFiles(mp3FileDetailsList)
            }
        }
    }

    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        .addCategory(Intent.CATEGORY_OPENABLE)
        .setType("audio/mpeg")
        .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        item {
            OutlinedTextField(
                value = mp3PlaylistDetails.playlistName,
                onValueChange = {
                    onPlaylistValueChange(mp3PlaylistDetails.copy(playlistName = it))
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
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        item {
            IconButton(
                onClick = {
                    activityResultLauncher.launch(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(R.drawable.addsongs),
                    contentDescription = stringResource(R.string.add_songs),
                    tint = Color.Unspecified
                )
            }
        }
        items(mp3FileDetailsList) { fileDetails ->
            Text(
                text = fileDetails.fileName ?: "Unknown",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface
            )
        }
    }
}
