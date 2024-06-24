package com.example.tunetide.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tunetide.TuneTideApplication
import com.example.tunetide.model.CurrentTimerViewModel
import com.example.tunetide.model.TimerViewModel

/**
 * Factory (creates instance of ViewModel for the whole app to use)
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            TimerViewModel(
                TuneTideApplication().container.timerRepository
            )
        }

        initializer {
            CurrentTimerViewModel(
                TuneTideApplication().container.timerRepository
            )
        }

        // TODO @ERICA add SpotifyViewModel
        // TODO @KIANA add MP3ViewModel
    }
}

// create queries for the application object, returns instance of the application
fun CreationExtras.inventoryApplication(): TuneTideApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as TuneTideApplication)