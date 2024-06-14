package com.example.tunetide.ui

import android.widget.ImageButton
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
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
                Icon(
                    painter = painterResource(id = R.drawable.settings),
                    contentDescription = "Settings", tint = PurpleAccent
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
                    contentDescription = "Queue", tint = PurpleAccent
                )
            }
        }

        // Replace Chill Work (Long) with tide flow and style it
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp), // Reduce padding to minimize space
            horizontalArrangement = Arrangement.Start // Move to the left
        ) {
            Text(
                text = "tide flow",
                color = Color.White.copy(alpha = 0.4f), // Adjusted color with 40% opacity
                fontSize = 24.sp, // Smaller font size
                fontWeight = FontWeight.Normal, // Unbold
                modifier = Modifier.padding(start = 16.dp)
            )
        }

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
            // TODO: Make background bar go behind the buttons
           // Image(painter = painterResource(id = R.drawable.bottom_bar_blank), contentDescription = "Bottom Bar", contentScale = ContentScale.FillBounds)
            IconButton(onClick = { /* add navigation action */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = "Calendar", tint = PurpleAccent
                )
            }
            IconButton(onClick = { /* add navigation action */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = "Home", tint = PurpleAccent
                )
            }
            IconButton(onClick = { /* add navigation action */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.timer),
                    contentDescription = "Timer", tint = PurpleAccent
                )
            }
        }
    }
}
