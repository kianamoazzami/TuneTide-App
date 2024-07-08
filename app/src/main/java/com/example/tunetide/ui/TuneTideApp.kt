package com.example.tunetide.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
        IconButton(onClick = { /* add navigation action */ }) {
            Icon(
                painter = painterResource(id = R.drawable.settings),
                contentDescription = "Settings", tint = PurpleAccent,
                modifier = Modifier.size(30.dp)
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
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { /* add navigation action */ }) {
            Icon(
                painter = painterResource(id = R.drawable.queue),
                contentDescription = "Queue", tint = PurpleAccent,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun TuneTideTopAppBarBack(
    navigateBack: () -> Unit
) {
    TopAppBar(
        backgroundColor = PurpleBackground,
        contentPadding = PaddingValues(horizontal = 1.dp)
    ) {
        IconButton(onClick = { navigateBack }) {
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
fun TuneTideBottomAppBar() {
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
            IconButton(onClick = { /* add navigation action */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = "Calendar", tint = PurpleBackground,
                    modifier = Modifier.size(30.dp)
                )
            }
            IconButton(onClick = { /* add navigation action */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = "Home", tint = PurpleAccent,
                    modifier = Modifier.size(30.dp)
                )
            }
            IconButton(onClick = { /* add navigation action */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.timer),
                    contentDescription = "Timer", tint = PurpleBackground,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}