package com.example.tunetide.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tunetide.ui.theme.*

class HomePage {

    @Composable
    fun layout() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleBackground)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar().layout()

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
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Timer on the left
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "12:36",
                            color = Color.White,
                            fontSize = 48.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    // Adding the new container on the right
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(180.dp) // Adjusted height for better fit
                            .background(Color(0xFF544FA3), shape = RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        ) {
                            // Completed Box
                            Box(
                                modifier = Modifier
                                    .height(30.dp) // Adjusted height for better fit
                                    .background(
                                        Color(0xC0BFE0).copy(alpha = 0.75f),
                                        shape = RoundedCornerShape(4.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 8.dp)
                                ) {
                                    Text("completed", color = Color(0xFF2B217F), fontSize = 12.sp)
                                    Text("2", color = Color(0xFF2B217F), fontSize = 12.sp)
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            // Interval Box
                            Box(
                                modifier = Modifier
                                    .height(100.dp) // Adjusted height for better fit
                                    .background(
                                        Color(0xFFE6E5F2).copy(alpha = 0.75f),
                                        shape = RoundedCornerShape(4.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp)
                                ) {
                                    Text("interval 3 of 4", color = Color(0x241673).copy(alpha = 0.5f), fontSize = 12.sp)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("flow", color = Color.Magenta, fontSize = 12.sp)
                                        Text("12:36", color = Color.Black, fontSize = 12.sp)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("break", color = Color.Magenta, fontSize = 12.sp)
                                        Text("5:00", color = Color.Black, fontSize = 12.sp)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            // Remaining Box
                            Box(
                                modifier = Modifier
                                    .height(30.dp) // Adjusted height for better fit
                                    .background(Color(0xC0BFE0), shape = RoundedCornerShape(4.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 8.dp)
                                ) {
                                    Text("remaining", color = Color(0xFF2B217F), fontSize = 12.sp)
                                    Text("1", color = Color(0xFF2B217F), fontSize = 12.sp)
                                }
                            }
                        }
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

            BottomBar().layout()
        }
    }
}
