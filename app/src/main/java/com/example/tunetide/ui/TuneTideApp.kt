package com.example.tunetide.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tunetide.R
import com.example.tunetide.ui.navigation.TuneTideNavHost
import com.example.tunetide.ui.theme.PurpleAccent
import com.example.tunetide.ui.theme.PurpleBackground

/**
 * top level composable that represents the shell of a screen
 */
@Composable
fun TuneTideApp(navController: NavHostController = rememberNavController()) {
    TuneTideNavHost(navController = navController)
}

// TODO @NOUR @KATHERINE add arguments like 'canNavigateBack' etc to composables
@Composable
fun TuneTideTopAppBar() {
    TopAppBar(
        backgroundColor = PurpleBackground,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "tunetide",
            color = Color(0xFF544FA3),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.weight(6f)
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun TuneTideTopAppBarBack(
    navigateBack: () -> Unit
    //TODO: add canNavigateBack
) {
    TopAppBar(
        backgroundColor = PurpleBackground,
        contentPadding = PaddingValues(horizontal = 1.dp)
    ) {
        IconButton(onClick = navigateBack) {
            Icon(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "Back", tint = PurpleAccent,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "tunetide",
            color = Color(0xFF544FA3),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.weight(6f)
        )
        Spacer(modifier = Modifier.weight(2.3f))
    }
}

@Composable
fun TuneTideBottomAppBar(
    currentScreen: Int,
    navigateToSettings: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToTimersList: () -> Unit
) {
    val darkerTint = Color(0xFF3D3B8E) // Define a darker tint color
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.bottom_bar_blank),
            contentDescription = "Bottom Bar",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            IconButton(onClick = navigateToSettings) {
                Icon(
                    painter = painterResource(id = R.drawable.settings),
                    contentDescription = "Settings",
                    tint =
                    if (currentScreen == R.string.settings_screen) {
                        PurpleAccent
                    } else {
                        PurpleBackground },
                    modifier = Modifier.size(30.dp)
                )
            }
            IconButton(onClick = navigateToHome) {
                Icon(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = "Home",
                    tint =
                    if (currentScreen == R.string.home_screen) {
                        PurpleAccent
                    } else {
                        PurpleBackground },
                    modifier = Modifier.size(30.dp)
                )
            }
            IconButton(onClick = navigateToTimersList) {
                Icon(
                    painter = painterResource(id = R.drawable.timer),
                    contentDescription = "Timer",
                    tint =
                    if (currentScreen == R.string.timers_screen) {
                        PurpleAccent
                    } else {
                        PurpleBackground },
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}
