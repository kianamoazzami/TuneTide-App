package com.example.tunetide.ui

import android.widget.ImageButton
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tunetide.ui.theme.*
import com.example.tunetide.R
import androidx.compose.ui.res.painterResource

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
            backgroundColor = PurpleBackground,
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            IconButton(onClick = { /* add navigation action */ }) {
                Icon(painter = painterResource(id = R.drawable.settings),
                    contentDescription = "Settings", tint = PurpleAccent)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "tunetide",
                color = Color(0xFF544FA3),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(6f)
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { /* add navigation action */ }) {
                Icon(painter = painterResource(id = R.drawable.queue),
                    contentDescription = "Queue", tint = PurpleAccent)
            }
        }

        Text(
            text = "Chill Work (Long)",
            color = TextColorCode,
            fontSize = 20.sp,
            modifier = Modifier
                .background(FlowPink)
                .padding(8.dp)
        )

        // Dark purple container with rounded corners
        Box(
            modifier = Modifier
                .width(344.dp)
                .height(277.dp)
                .background(Color(0xFF2B217F), shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "12:36",
                    color = Color.White,
                    fontSize = 48.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    Text("Completed", color = Color.White)
                    Text("Interval 3 of 4", color = Color.White)
                    Text("Remaining", color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Light purple container with rounded corners
        Box(
            modifier = Modifier
                .width(344.dp)
                .height(212.dp)
                .background(Color(0xFFE6E5F2), shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder for music player UI
            Text(
                text = "Music Player UI",
                color = Color.Gray,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }

        BottomNavigation {
            BottomNavigationItem(
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                selected = true,
                onClick = { /* add navigation action */ }
            )
            BottomNavigationItem(
                icon = { Icon(Icons.Default.List, contentDescription = "List") },
                selected = false,
                onClick = { /* add navigation action */ }
            )
        }
    }
}
