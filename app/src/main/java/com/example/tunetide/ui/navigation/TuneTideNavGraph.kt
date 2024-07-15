package com.example.tunetide.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tunetide.ui.SettingsDestination
import com.example.tunetide.ui.SettingsScreen
import com.example.tunetide.ui.home.HomeDestination
import com.example.tunetide.ui.home.HomeScreen
import com.example.tunetide.ui.mp3.LocalFilesDestination
import com.example.tunetide.ui.mp3.LocalFilesScreen
import com.example.tunetide.ui.mp3.MP3PlaylistDetailsDestination
import com.example.tunetide.ui.mp3.MP3PlaylistDetailsScreen
import com.example.tunetide.ui.mp3.MP3PlaylistEntryDestination
import com.example.tunetide.ui.mp3.MP3PlaylistEntryScreen
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
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                // TODO @KATHERINE @NOUR navigation info here
            )
        }

        composable(route = SettingsDestination.route) {
            SettingsScreen(
                navigateBack = { navController.popBackStack() },
                navigateToLocalFiles = {navController.navigate(LocalFilesDestination.route) }
            )
        }

        composable(route = LocalFilesDestination.route) {
            LocalFilesScreen(
                navigateBack = { navController.popBackStack() },
                navigateToMP3PlaylistEntry = { navController.navigate(MP3PlaylistEntryDestination.route) },
                navigateToMP3PlaylistUpdate = {
                    navController.navigate("${MP3PlaylistDetailsDestination.route}/${it}")
                }
            )
        }

        composable(route = MP3PlaylistEntryDestination.route) {
            MP3PlaylistEntryScreen(
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = MP3PlaylistDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(MP3PlaylistDetailsDestination.mp3PlaylistIdArg) {
                type = NavType.IntType
            })
        ) {
            MP3PlaylistDetailsScreen(
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

