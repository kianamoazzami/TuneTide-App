package com.example.tunetide.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tunetide.Application
import com.example.tunetide.ui.home.HomePageViewModel
import com.example.tunetide.ui.mp3.LocalFilesViewModel
import com.example.tunetide.ui.mp3.MP3PlaylistDetailsViewModel
import com.example.tunetide.ui.mp3.MP3PlaylistEntryViewModel
import com.example.tunetide.ui.timer.TimerEditViewModel
import com.example.tunetide.ui.timer.TimerEntryViewModel

/**
 * Factory (creates instance of ViewModel for the whole app to use)
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            TimerEntryViewModel(
                TuneTideApplication().container.timerRepository,
                TuneTideApplication().container.spotifyRepository
            )
        }

        initializer {
            TimerEditViewModel(
                this.createSavedStateHandle(),
                TuneTideApplication().container.timerRepository,
                TuneTideApplication().container.spotifyRepository
            )
        }

        initializer {
            HomePageViewModel(
                TuneTideApplication().applicationContext,
                TuneTideApplication().container.playbackRepository,
                TuneTideApplication().container.mp3Repository,
                TuneTideApplication().container.spotifyRepository
            )
        }

        initializer {
            LocalFilesViewModel(
                TuneTideApplication().container.mp3Repository
            )
        }

        initializer {
            MP3PlaylistEntryViewModel(
                TuneTideApplication().container.mp3Repository
            )
        }

        initializer {
            MP3PlaylistDetailsViewModel(
                this.createSavedStateHandle(),
                TuneTideApplication().container.mp3Repository
            )
        }

        // TODO @MIA parse ViewModels into good sections
        // TODO @ERICA add all Spotify ViewModels required
        // TODO @KIANA add all MP3 ViewModel required
    }
}

// create queries for the application object, returns instance of the application
fun CreationExtras.TuneTideApplication(): Application =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as Application)