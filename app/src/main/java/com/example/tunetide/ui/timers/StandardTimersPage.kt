package com.example.tunetide.ui.timers

import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.painter.Painter

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.tunetide.ui.AppViewModelProvider
import com.example.tunetide.database.Timer
import com.example.tunetide.ui.timer.TimerEditDestination

object StandardTimersDestination : NavigationDestination {
    override val route = "standard_timers"
    override val titleRes = R.string.standard_timers_page_name
}

val StandardTimersBackground = Color(0xC0BFE0)

@Composable
fun StandardTimersScreen(
    modifier: Modifier = Modifier,
    navigateToSettings: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToInterval: () -> Unit,
    navigateToAll: () -> Unit,
    navigateToTimerEntry: () -> Unit,
    navigateToTimerEdit: () -> Unit,
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
        StandardBody(
            modifier = modifier.padding(innerPadding),
            navigateToHome = navigateToHome,
            navigateToTimerEdit = navigateToTimerEdit,
            navigateToInterval = navigateToInterval,
            navigateToAll = navigateToAll,
        )
    }
}

@Composable
fun StandardBody(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    navigateToHome: () -> Unit,
    navigateToTimerEdit: () -> Unit,
    navigateToInterval: () -> Unit,
    navigateToAll: () -> Unit,
    viewModel: TimersListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val timerListUIState by viewModel.timerListUIState.collectAsState()

    // Print the timers to the console using a normal for loop


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(StandardTimersBackground)
            .padding(contentPadding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StandardIconRow(
            navigateToInterval = navigateToInterval,
            navigateToAll = navigateToAll
        )
        BoxWithImageList(
            navigateToHome = navigateToHome,
            navigateToTimerEdit = navigateToTimerEdit,
            timerListUIState.timers
        )
    }
}

@Composable
fun StandardIconRow(
    navigateToInterval: () -> Unit,
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
            painter = painterResource(R.drawable.old_all_timers),
            onClick = navigateToAll
        )
        TimerTypeIcon(
            painter = painterResource(R.drawable.standard_page),
            onClick = { }
        )
        TimerTypeIcon(
            painter = painterResource(R.drawable.interval_standard),
            onClick = navigateToInterval
        )
    }
}

//@Composable
//fun StandardBody(
//    navController: NavController,
//    timers: List<Timer>,
//    modifier: Modifier = Modifier,
//    contentPadding: PaddingValues = PaddingValues(16.dp)
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(PurpleBackground),
//        verticalArrangement = Arrangement.Top,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Spacer(modifier = Modifier.height(16.dp))
//        BoxWithImageListStandard(navController, timers)
//    }
//}

//@Composable
//fun BoxWithImageListStandard(navController: NavController, timers: List<Timer>) {
//    LazyColumn(modifier = Modifier.padding(0.dp,0.dp,0.dp,60.dp)) {
//        items(timers) { timer ->
//            BoxWithImage(
//                title = timer.timerName,
//                subTitle = "${timer.numIntervals} intervals",
//                onClick = { navController.navigate(HomeDestination.route) },
//                onEditClick = { navController.navigate("${TimerEditDestination.route}/${timer.timerId}") }
//            )
//        }
//    }
//}
