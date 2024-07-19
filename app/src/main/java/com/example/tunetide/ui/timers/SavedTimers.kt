package com.example.tunetide.ui.timers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tunetide.R
import com.example.tunetide.ui.TuneTideBottomAppBar
import com.example.tunetide.ui.TuneTideTopAppBar
import com.example.tunetide.ui.navigation.NavigationDestination
import androidx.compose.ui.graphics.painter.Painter

object SavedTimersDestination : NavigationDestination {
    override val route = "savedtimers"
    override val titleRes = R.string.saved_timers_page_name
}

val PurpleBackground = Color(0xC0BFE0)

@Composable
fun SavedTimersScreen(
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
    contentPadding: PaddingValues = PaddingValues(16.dp)
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleBackground)
            .padding(contentPadding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconRow(navController)
        // Add content for saved timers here
    }
}

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
