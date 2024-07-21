package com.example.tunetide.ui.timers

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tunetide.R
import com.example.tunetide.database.FilterType
import com.example.tunetide.ui.TuneTideBottomAppBar
import com.example.tunetide.ui.TuneTideTopAppBar
import com.example.tunetide.ui.navigation.NavigationDestination
import com.example.tunetide.ui.AppViewModelProvider

object IntervalTimersDestination : NavigationDestination {
    override val route = "interval_timers"
    override val titleRes = R.string.interval_timers_page_name
}

val PurpleBackground = Color(0xC0BFE0)

@Composable
fun IntervalTimersScreen(
    modifier: Modifier = Modifier,
    navigateToSettings: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToStandard: () -> Unit,
    navigateToAll: () -> Unit,
    navigateToTimerEntry: () -> Unit,
    navigateToTimerEdit: (Int) -> Unit,
    viewModel: TimersListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Scaffold(
        topBar = {
            TuneTideTopAppBar()
        },
        bottomBar = {
            TuneTideBottomAppBar(
                currentScreen = R.string.timers_screen,
                navigateToSettings = navigateToSettings,
                navigateToHome = navigateToHome,
                navigateToTimersList = { }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToTimerEntry() },
                shape = CircleShape,
                backgroundColor = Color.Transparent,
                elevation = FloatingActionButtonDefaults.elevation(0.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = "Add new timer",
                    tint = Color.Unspecified
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        IntervalTimersBody(
            modifier = modifier.padding(innerPadding),
            navigateToHome = navigateToHome,
            navigateToTimerEdit = navigateToTimerEdit,
            navigateToStandard = navigateToStandard,
            navigateToAll = navigateToAll
        )
    }
}

@Composable
fun IntervalTimersBody(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    navigateToHome: () -> Unit,
    navigateToTimerEdit: (Int) -> Unit,
    navigateToStandard: () -> Unit,
    navigateToAll: () -> Unit,
    viewModel: TimersListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(Unit) {
        viewModel.filterTimers(FilterType.INTERVAL)
    }

    val timerListUIState by viewModel.timerListUIState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleBackground)
            .padding(contentPadding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IntervalIconRow(
            navigateToStandard = navigateToStandard,
            navigateToAll = navigateToAll
        )
        BoxWithImageList(
            navigateToHome = navigateToHome,
            navigateToTimerEdit = navigateToTimerEdit,
            timers = timerListUIState.timers,
            viewModel = viewModel
        )
    }
}

@Composable
fun IntervalIconRow(
    navigateToStandard: () -> Unit,
    navigateToAll: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TimerTypeIcon(
            painterResource(R.drawable.old_all_timers),
            onClick = navigateToAll
        )
        TimerTypeIcon(
            painterResource(R.drawable.old_standard),
            onClick = navigateToStandard
        )
        TimerTypeIcon(
            painterResource(R.drawable.interval),
            onClick = { }
        )
    }
}
