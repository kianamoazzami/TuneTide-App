package com.example.tunetide.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tunetide.R
import com.example.tunetide.ui.theme.HighlightGreen
import com.example.tunetide.ui.theme.PurpleBackground
import kotlinx.coroutines.delay
import java.util.Locale

fun timeFormat(timeMillis: Long): String {
    val minutes = (timeMillis / 1000) / 60
    val seconds = (timeMillis / 1000) % 60
    return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
}

var timerState = "play"

class HomePage() {
    var timerValue: Long = 30000

    @Composable
    fun layout() {

        var currentTimeMillis by remember { mutableStateOf(timerValue) }
        var isRunning by remember { mutableStateOf(true) }

        val timerText = remember { mutableStateOf(timeFormat(timerValue)) }

        LaunchedEffect(isRunning) {
            while (isRunning && currentTimeMillis > 0) {
                delay(1000)
                currentTimeMillis -= 1000
                timerText.value = timeFormat(currentTimeMillis)
            }
            if (currentTimeMillis <= 0) {
                isRunning = false
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleBackground)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar().layout()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "tide flow",
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

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
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = timerText.value,
                            color = Color.White,
                            fontSize = 48.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        IconButton(onClick = {
                            if (timerState == "play") {
                                timerState = "pause"
                                isRunning = false
                            } else if (timerState == "pause") {
                                timerState = "play"
                                isRunning = true
                            }
                        }) {
                            if (timerState == "play") {
                                Icon(
                                    painter = painterResource(id = R.drawable.pausebutton),
                                    contentDescription = "Pause Button", tint = HighlightGreen,
                                    modifier = Modifier.size(30.dp)
                                )
                            } else if (timerState == "pause") {
                                Icon(
                                    painter = painterResource(id = R.drawable.playbutton),
                                    contentDescription = "Play Button", tint = HighlightGreen,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(180.dp)
                                .background(Color(0xFF544FA3), shape = RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.TopStart
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .height(30.dp)
                                        .fillMaxWidth()
                                        .background(
                                            Color(0xFFC0BFE0).copy(alpha = 0.75f),
                                            shape = RoundedCornerShape(4.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 8.dp)
                                    ) {
                                        Text(
                                            "completed",
                                            color = Color(0xFF2B217F),
                                            fontSize = 12.sp,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            "2",
                                            color = Color(0xFF2B217F),
                                            fontSize = 12.sp,
                                            textAlign = TextAlign.End,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .height(100.dp)
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
                                        Text(
                                            "interval 3 of 4",
                                            color = Color(0xFF241673).copy(alpha = 0.5f),
                                            fontSize = 12.sp
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                "flow",
                                                color = Color(0xFFBF5FFF),
                                                fontSize = 12.sp
                                            )
                                            Text(
                                                "12:36",
                                                color = Color(0xFFB2A9A9),
                                                fontSize = 12.sp
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                "break",
                                                color = Color(0xFFBF5FFF),
                                                fontSize = 12.sp
                                            )
                                            Text(
                                                "5:00",
                                                color = Color(0xFFB2A9A9),
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .height(30.dp)
                                        .fillMaxWidth()
                                        .background(
                                            Color(0xFFC0BFE0),
                                            shape = RoundedCornerShape(4.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 8.dp)
                                    ) {
                                        Text(
                                            "remaining",
                                            color = Color(0xFF2B217F),
                                            fontSize = 12.sp,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            "1",
                                            color = Color(0xFF2B217F),
                                            fontSize = 12.sp,
                                            textAlign = TextAlign.End,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .width(344.dp)
                    .height(212.dp)
                    .background(Color(0xFFE6E5F2), shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
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
