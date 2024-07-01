package com.example.tunetide.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tunetide.TuneTideApplication
import com.example.tunetide.ui.home.HomePageViewModel
import com.example.tunetide.ui.timer.TimerEditViewModel
import com.example.tunetide.ui.timer.TimerEntryViewModel

/**
 * Factory (creates instance of ViewModel for the whole app to use)
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            TimerEntryViewModel(
                TuneTideApplication().container.timerRepository
            )
        }

        initializer {
            TimerEditViewModel(
                this.createSavedStateHandle(),
                TuneTideApplication().container.timerRepository
            )
        }

        initializer {
            HomePageViewModel(
                TuneTideApplication().container.playbackRepository
            )
        }

        initializer {
            TimerEntryViewModel(
                TuneTideApplication().container.timerRepository
            )
        }

        // TODO @MIA parse ViewModels into good sections
        // TODO @ERICA add all Spotify ViewModels required
        // TODO @KIANA add all MP3 ViewModel required
    }
}

// create queries for the application object, returns instance of the application
fun CreationExtras.TuneTideApplication(): TuneTideApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as TuneTideApplication)