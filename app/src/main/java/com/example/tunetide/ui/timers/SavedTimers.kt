package com.example.tunetide.ui.timers

import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tunetide.R
import com.example.tunetide.ui.TuneTideBottomAppBar
import com.example.tunetide.ui.TuneTideTopAppBar
import com.example.tunetide.ui.navigation.NavigationDestination
import com.example.tunetide.ui.home.HomeDestination
import com.example.tunetide.ui.AppViewModelProvider
import com.example.tunetide.database.Timer

object SavedTimersDestination : NavigationDestination {
    override val route = "savedtimers"
    override val titleRes = R.string.saved_timers_page_name
}

val PurpleBackground = Color(0xC0BFE0)

@Composable
fun SavedTimersScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    navigateToSettings: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToTimersList: () -> Unit,
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
                currentScreen = R.string.saved_timers_page_name,
                navigateToSettings = navigateToSettings,
                navigateToHome = navigateToHome,
                navigateToTimersList = navigateToTimersList
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
        SavedTimersBody(
            navController = navController,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun SavedTimersBody(
    navController: NavController,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    viewModel: TimersListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val timerListUIState by viewModel.timerListUIState.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleBackground)
            .padding(contentPadding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconRow(navController)
        Spacer(modifier = Modifier.height(16.dp)) // Add space between the rows
        BoxWithImageList(navController, timerListUIState.timers)
    }
}

//@Composable
//fun BoxWithImageList(navController: NavController, timers: List<Timer>) {
//    LazyColumn {
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


@Composable
fun IconRow(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomIcon(
            painterResource(R.drawable.old_all_timers),
            onClick = { navController.navigate(AllTimersPageDestination.route) }
        )
        CustomIcon(
            painterResource(R.drawable.old_standard),
            onClick = { navController.navigate(StandardPageDestination.route) }
        )
        CustomIcon(
            painterResource(R.drawable.interval),
            onClick = { navController.navigate(SavedTimersDestination.route) } // Navigate back to the same screen
        )
    }
}

@Composable
fun CustomIcon(
    icon: Painter,
    onClick: () -> Unit
) {
    Image(
        painter = icon,
        contentDescription = null,
        modifier = Modifier
            .size(100.dp)
            .clickable(onClick = onClick)
    )
}
