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

object StandardPageDestination : NavigationDestination {
    override val route = "standardPage"
    override val titleRes = R.string.standard_page_name
}

val StandardPageBackground = Color(0xC0BFE0)

@Composable
fun StandardPageScreen(
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
                currentScreen = R.string.standard_page_name,
                navigateToSettings = navigateToSettings,
                navigateToHome = navigateToHome,
                navigateToTimersList = navigateToTimersList)
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
        StandardPageBodyContent(
            navController = navController,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun StandardPageBodyContent(
    navController: NavController,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp)
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(StandardPageBackground)
            .padding(contentPadding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StandardPageIconRow(navController)
        StandardBody(navController)
        // Add content for the Standard page here
    }
}
@Composable
fun StandardBody(
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
        BoxWithImageListStandard(navController)
        // Add content for saved timers here
    }
}

@Composable
fun BoxWithImageListStandard(navController: NavController) {
    val boxWithImageItems = listOf(
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
fun StandardPageIconRow(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TimerIcon(
            painter = painterResource(R.drawable.old_all_timers),
            onClick = { navController.navigate(AllTimersPageDestination.route) }
        )
        TimerIcon(
            painter = painterResource(R.drawable.standard_page),
            onClick = { navController.navigate(StandardPageDestination.route) }
        )
        TimerIcon(
            painter = painterResource(R.drawable.interval_standard),
            onClick = { navController.navigate(SavedTimersDestination.route) } // Navigate back to the same screen
        )
    }
}

@Composable
fun TimerIcon(
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
