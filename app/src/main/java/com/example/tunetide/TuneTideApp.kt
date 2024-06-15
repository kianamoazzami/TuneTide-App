package com.example.tunetide

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

/**
 * Top level composable (screens for the app)
 */
@Composable
fun TuneTideApp(navController: NavHostController = rememberNavController()) {
    // TODO
}


// (Mia: June 10) Not sure how to configure the screens and nav yet - see Unit 4.2 Navigation
/**
 * Top bar (title and Nav to other screens)
 */
@Composable
fun TuneTideTopAppBar() {}

/**
 * Bottom bar (Nav to main screens)
 */
@Composable
fun TuneTideBottomAppBar() {}