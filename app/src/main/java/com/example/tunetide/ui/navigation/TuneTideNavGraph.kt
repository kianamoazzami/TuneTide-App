package com.example.tunetide.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tunetide.ui.home.HomeDestination
import com.example.tunetide.ui.home.HomeScreen

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
        startDestination = HomeDestination.route, // to get
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                // TODO @KATHERINE @NOUR navigation info here
            )
        }
    }
}