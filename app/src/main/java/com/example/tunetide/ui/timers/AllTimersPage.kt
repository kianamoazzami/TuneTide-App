package com.example.tunetide.ui.timers

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tunetide.R
import com.example.tunetide.ui.TuneTideBottomAppBar
import com.example.tunetide.ui.TuneTideTopAppBar
import com.example.tunetide.ui.home.HomeDestination
import com.example.tunetide.ui.navigation.NavigationDestination
import com.example.tunetide.ui.timer.TimerEditDestination
import com.example.tunetide.ui.AppViewModelProvider
import com.example.tunetide.database.Timer
import kotlinx.coroutines.launch

object AllTimersDestination : NavigationDestination {
    override val route = "all_timers"
    override val titleRes = R.string.all_timers_page_name
}

val AllTimersPageBackground = Color(0xC0BFE0)

@Composable
fun AllTimersScreen(
    modifier: Modifier = Modifier,
    navigateToSettings: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToStandard: () -> Unit,
    navigateToInterval: () -> Unit,
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
        AllTimersBody(
            modifier = modifier.padding(innerPadding),
            navigateToHome = navigateToHome,
            navigateToTimerEdit = navigateToTimerEdit,
            navigateToStandard = navigateToStandard,
            navigateToInterval = navigateToInterval
        )
    }
}

@Composable
fun AllTimersBody(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    navigateToHome: () -> Unit,
    navigateToTimerEdit: (Int) -> Unit,
    navigateToStandard: () -> Unit,
    navigateToInterval: () -> Unit,
    viewModel: TimersListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Log.w("AllTimersPageBodyContent", "Inside AllTimersPageBodyContent")
    val timerListUIState by viewModel.timerListUIState.collectAsState()

    for (timer in timerListUIState.timers) {
        Log.d("AllTimersPageScreen", "Timer: ${timer.timerId}, Name: ${timer.timerName}, Duration: ${timer.numIntervals}")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AllTimersPageBackground)
            .padding(contentPadding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AllTimersIconRow(
            navigateToStandard = navigateToStandard,
            navigateToInterval = navigateToInterval
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
fun AllTimersIconRow(
    navigateToStandard: () -> Unit,
    navigateToInterval: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TimerTypeIcon(
            painter = painterResource(R.drawable.ic_all_timer),
            onClick = { }
        )
        TimerTypeIcon(
            painter = painterResource(R.drawable.old_standard),
            onClick = navigateToStandard
        )
        TimerTypeIcon(
            painter = painterResource(R.drawable.interval_standard),
            onClick = navigateToInterval
        )
    }
}

//USED FOR ALL TIMER PAGES:

@Composable
fun BoxWithImageList(
    navigateToHome: () -> Unit,
    navigateToTimerEdit: (Int) -> Unit,
    timers: List<Timer>,
    viewModel: TimersListViewModel) {

    val coroutineScope = rememberCoroutineScope()

    LazyColumn(modifier = Modifier.padding(0.dp,0.dp,0.dp,60.dp)) {
        items(timers) { timer ->
            BoxWithImage(
                title = timer.timerName,
                subTitle = if (timer.numIntervals != 1) {
                                "${timer.numIntervals} intervals"
                            } else {
                                "${timer.numIntervals} interval"
                            },
                onClick = {
                    coroutineScope.launch {
                        viewModel.setPlayback(timer)
                        navigateToHome()
                    }
                },
                onEditClick = { navigateToTimerEdit(timer.timerId) }
            )
        }
    }
}

@Composable
fun TimerTypeIcon(
    painter: Painter,
    onClick: () -> Unit
) {
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .size(100.dp)
            .clickable(onClick = onClick)
    )
}
