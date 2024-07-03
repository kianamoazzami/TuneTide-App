package com.example.tunetide.ui.timer

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tunetide.R
import com.example.tunetide.ui.TuneTideTopAppBar
import com.example.tunetide.ui.AppViewModelProvider
import com.example.tunetide.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object TimerEntryDestination : NavigationDestination {
    override val route = "timer_entry"
    override val titleRes = R.string.timer_entry_name
}

@Composable
fun TimerEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    modifier: Modifier = Modifier,
    viewModel: TimerEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TuneTideTopAppBar()
        }
    ) { innerPadding ->
        TimerEntryBody(
            timerUIState = viewModel.timerUIState,
            onTimerValueChange = viewModel::updateUIState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.insertTimer()
                    navigateBack()
                }
            },
            modifier = modifier,
            innerPadding
        )
    }
}

@Composable
fun TimerEntryBody(
    timerUIState: TimerUIState,
    onTimerValueChange: (TimerDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
){}