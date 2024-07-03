package com.example.tunetide.ui.navigation

/**
 * describes navigation destinations for the app
 */
interface NavigationDestination {

    // unique name that defines the path for a composable
    val route: String

    // string resource id, contains title to be displayed for the screen
    val titleRes: Int
}