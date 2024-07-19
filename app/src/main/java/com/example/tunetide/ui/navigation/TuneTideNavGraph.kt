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
import com.example.tunetide.ui.timers.*
import com.example.tunetide.ui.timers.AllTimersPageDestination
import com.example.tunetide.ui.timers.AllTimersPageScreen


@Composable
fun TuneTideNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = SavedTimersDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToSettings = { navController.navigate(SettingsDestination.route) },
                navigateToTimersList = { /* add this when its complete */ }
            )
        }

        composable(route = SettingsDestination.route) {
            SettingsScreen(
                navigateToHome = { navController.navigate(HomeDestination.route) },
                navigateToTimersList = { /* add this when its complete */ },
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
        composable(route = SavedTimersDestination.route) {
            SavedTimersScreen(navController = navController)
        }
        composable(route = StandardPageDestination.route) {
            StandardPageScreen(navController = navController)
        }
        composable(route = AllTimersPageDestination.route) {
            AllTimersPageScreen(navController = navController)
        }
    }
}
