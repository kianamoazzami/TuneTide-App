package com.example.tunetide.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tunetide.ui.theme.*
import com.example.tunetide.ui.HomePage

@Composable
fun HomePage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = { Text("tunetide", color = Color.White) },
            backgroundColor = PurpleAccent,
            actions = {
                IconButton(onClick = { /* add navigation action*/ }) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
                }
            }
        )

        Text(
            text = "Chill Work (Long)",
            color = TextColorCode,
            fontSize = 20.sp,
            modifier = Modifier
                .background(FlowPink)
                .padding(8.dp)
        )

        Box(
            modifier = Modifier
                .size(200.dp)
                .background(FlowPink2),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "12:36",
                color = FlowText,
                fontSize = 48.sp,
                textAlign = TextAlign.Center
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Completed", color = TextColorCode)
            Text("Interval 3 of 4", color = TextColorCode)
            Text("Remaining", color = TextColorCode)
        }

        // Please add the music player UI below

        BottomNavigation {
            BottomNavigationItem(
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                selected = true,
                onClick = { /* add navigation action */ }
            )
            BottomNavigationItem(
                icon = { Icon(Icons.Default.List, contentDescription = "List") },
                selected = false,
                onClick = { /* add navigation action*/ }
            )

        }
    }
}
