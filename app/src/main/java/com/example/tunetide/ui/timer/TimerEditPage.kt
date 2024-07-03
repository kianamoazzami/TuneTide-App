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

object TimerEditDestination : NavigationDestination {
    override val route = "timer_edit"
    override val titleRes = R.string.timer_edit_name
    const val timerIdArg = "timerId"
    val routeWithArgs = "$route/{$timerIdArg}"
}

@Composable
fun TimerEditScreen(
    navigateBack: () -> Unit,
    navigateToEditTimer: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TimerEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TuneTideTopAppBar()
        }
    ) { innerPadding ->
        TimerEditBody(
            timerUIState = viewModel.timerUIState,
            onTimerValueChange = viewModel::updateUIState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateTimer()
                    navigateBack()
                }
            },
            modifier = modifier,
            innerPadding
        )
    }
}

@Composable
fun TimerEditBody(
    timerUIState: TimerUIState,
    onTimerValueChange: (TimerDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
){}
