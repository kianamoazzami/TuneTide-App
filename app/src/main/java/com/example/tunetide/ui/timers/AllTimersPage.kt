package com.example.tunetide.ui.timers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tunetide.R
import com.example.tunetide.ui.TuneTideBottomAppBar
import com.example.tunetide.ui.TuneTideTopAppBar
import com.example.tunetide.ui.navigation.NavigationDestination

object AllTimersPageDestination : NavigationDestination {
    override val route = "allTimersPage"
    override val titleRes = R.string.all_timers_page_name
}

val AllTimersPageBackground = Color(0xC0BFE0)

@Composable
fun AllTimersPageScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TuneTideTopAppBar()
        },
        bottomBar = {
            TuneTideBottomAppBar()
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* No action for now */ },
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
