package com.example.tunetide.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tunetide.ui.SettingsDestination
import com.example.tunetide.ui.SettingsScreen
import com.example.tunetide.ui.home.HomeDestination
import com.example.tunetide.ui.home.HomeScreen
import com.example.tunetide.ui.timer.TimerEditDestination
import com.example.tunetide.ui.timer.TimerEditScreen
import com.example.tunetide.ui.timer.TimerEntryDestination
import com.example.tunetide.ui.timer.TimerEntryScreen
import com.example.tunetide.ui.timers.TimersListDestination
import com.example.tunetide.ui.timers.TimersListScreen

/**
 * navigation graph for the app
 */
// TODO @KATHERINE @NOUR more navigation parameters and structure is missing -> see resources

@Composable
fun TuneTideNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route, // to get SettingsDestination.route
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                // TODO @KATHERINE @NOUR navigation info here
            )
        }

        composable(route = SettingsDestination.route) {
            SettingsScreen(
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(route = TimerEntryDestination.route) {
            TimerEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = TimerEditDestination.routeWithArgs,
            arguments = listOf(navArgument(TimerEditDestination.timerIdArg) {
                type = NavType.IntType
            })
        ) {
            TimerEditScreen(
                navigateToEditTimer = { navController.navigate("${TimerEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }

        composable(route = TimersListDestination.route) {
            TimersListScreen(
                navigateToTimerEntry = { navController.navigate(TimerEntryDestination.route) },
                navigateToTimerEdit = {
                    navController.navigate("${TimerEditDestination.route}/${it}")
                }
            )
        }
    }
}

