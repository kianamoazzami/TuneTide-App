package com.example.tunetide.ui.timers



import com.example.tunetide.ui.navigation.NavigationDestination
import com.example.tunetide.R

object TimerEditDestination : NavigationDestination {
    override val route: String = "timerEdit"
    override val titleRes: Int = R.string.timer_edit_page_name
    val routeWithArgs: String = "$route/{timerId}"
    const val timerIdArg: String = "timerId"
}
