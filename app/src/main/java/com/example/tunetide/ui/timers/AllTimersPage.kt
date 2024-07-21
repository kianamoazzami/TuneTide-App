package com.example.tunetide.ui.timers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tunetide.R
import com.example.tunetide.ui.TuneTideBottomAppBar
import com.example.tunetide.ui.TuneTideTopAppBar
import com.example.tunetide.ui.home.HomeDestination
import com.example.tunetide.ui.navigation.NavigationDestination
import com.example.tunetide.ui.timer.TimerEditDestination
import androidx.compose.ui.graphics.painter.Painter


object AllTimersPageDestination : NavigationDestination {
    override val route = "allTimersPage"
    override val titleRes = R.string.all_timers_page_name
}

val AllTimersPageBackground = Color(0xC0BFE0)

@Composable
fun AllTimersPageScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    navigateToSettings: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToTimersList: () -> Unit,
    navigateToTimerEntry: () -> Unit,
    navigateToTimerEdit: () -> Unit
) {
    Scaffold(
        topBar = {
            TuneTideTopAppBar()
        },
        bottomBar = {
            TuneTideBottomAppBar(
                currentScreen = R.string.all_timers_page_name,
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
        AllTimersPageBodyContent(
            navController = navController,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun AllTimersPageBodyContent(
    navController: NavController,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp)
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AllTimersPageBackground)
            .padding(contentPadding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AllTimersPageIconRow(navController)
        AllTimersBody(navController)

        // Add content for the All Timers page here
    }
}

@Composable
fun AllTimersPageIconRow(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AllTimersPageIcon(
            painter = painterResource(R.drawable.ic_all_timer),
            onClick = { navController.navigate(AllTimersPageDestination.route) }
        )
        AllTimersPageIcon(
            painter = painterResource(R.drawable.old_standard),
            onClick = { navController.navigate(StandardPageDestination.route) }
        )
        AllTimersPageIcon(
            painter = painterResource(R.drawable.interval_standard),
            onClick = { navController.navigate(SavedTimersDestination.route) } // Navigate back to the same screen
        )
    }

}
@Composable
fun AllTimersBody(
    navController: NavController,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp)
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleBackground),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        BoxWithImageList(navController)
        // Add content for saved timers here
    }
}

@Composable
fun BoxWithImageList(navController: NavController) {
    val boxWithImageItems = listOf(
        BoxWithImageItem(
            title = R.string.instrumental_studying,
            subTitle = R.string.instrumental_studying_time,
            onClick = { navController.navigate(HomeDestination.route) },
            onEditClick = { navController.navigate("${TimerEditDestination.route}/1") } // Pass the timer ID
        ),
        BoxWithImageItem(
            title = R.string.finishing_project,
            subTitle = R.string.finishing_project_time,
            onClick = { navController.navigate(HomeDestination.route) },
            onEditClick = { navController.navigate("${TimerEditDestination.route}/2") } // Pass the timer ID
        ),
        BoxWithImageItem(
            title = R.string.chill_work,
            subTitle = R.string.chill_work_time,
            onClick = { navController.navigate(HomeDestination.route) },
            onEditClick = { navController.navigate("${TimerEditDestination.route}/3") } // Pass the timer ID
        ),
        BoxWithImageItem(
            title = R.string.speedy,
            subTitle = R.string.speedy_time,
            onClick = { navController.navigate(HomeDestination.route) },
            onEditClick = { navController.navigate("${TimerEditDestination.route}/4") } // Pass the timer ID
        ),
        BoxWithImageItem(
            title = R.string.get_ready,
            subTitle = R.string.get_ready_time,
            onClick = { navController.navigate(HomeDestination.route) },
            onEditClick = { navController.navigate("${TimerEditDestination.route}/5") } // Pass the timer ID
        )
    )

    LazyColumn {
        items(boxWithImageItems) { item ->
            BoxWithImage(
                title = stringResource(id = item.title),
                subTitle = stringResource(id = item.subTitle),
                onClick = item.onClick,
                onEditClick = item.onEditClick
            )
        }
    }
}


@Composable
fun AllTimersPageIcon(
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
