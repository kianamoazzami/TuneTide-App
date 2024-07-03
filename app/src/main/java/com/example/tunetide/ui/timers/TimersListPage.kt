package com.example.tunetide.ui.timers

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tunetide.R
import com.example.tunetide.ui.TuneTideTopAppBar
import com.example.tunetide.database.FilterType
import com.example.tunetide.ui.AppViewModelProvider
import com.example.tunetide.ui.navigation.NavigationDestination
import com.example.tunetide.ui.timer.TimerUIState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

object TimersListDestination : NavigationDestination {
    override val route = "timers_list"
    override val titleRes = R.string.timers_list_name
}

@Composable
fun TimersListScreen(
    navigateToTimerEntry: () -> Unit,
    navigateToTimerEdit: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TimersListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    // TODO @NOUR @KATHERINE this should not be set here
    val filterType = FilterType.ALL
    Scaffold(
        topBar = {
            TuneTideTopAppBar()
        }
    ) { innerPadding ->
        TimersListBody(
            /*
            timersUIState = viewModel.timersUIState,
            onFilterClick = {
                coroutineScope.launch {
                    viewModel.filterTimers(filterType)
                }
            },*/
            modifier = modifier,
            innerPadding
        )
    }
}

@Composable
fun TimersListBody(
    //timersUIState: StateFlow<TimerUIState>,
    //onFilterClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
){}