package com.example.tunetide.ui.home

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tunetide.R
import com.example.tunetide.ui.TuneTideBottomAppBar
import com.example.tunetide.ui.theme.PurpleBackground
import kotlinx.coroutines.delay
import java.util.Locale
import com.example.tunetide.ui.AppViewModelProvider
import com.example.tunetide.ui.SettingsActivity
import com.example.tunetide.ui.navigation.NavigationDestination
import androidx.compose.runtime.rememberCoroutineScope

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.home_page_name
}

private fun timeFormat(timeMillis: Long): String {
    val minutes = (timeMillis / 1000) / 60
    val seconds = (timeMillis / 1000) % 60
    return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
}

@Composable
fun TuneTideTopAppBar(
    onSettingsClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "TuneTide",
                color = Color(0xFF544FA3)
            )
        },
        navigationIcon = {
            IconButton(onClick = { onSettingsClick() }) {
                Image(
                    painter = painterResource(id = R.drawable.settings),
                    contentDescription = stringResource(id = R.string.settings)
                )
            }
        },
        actions = {
            IconButton(onClick = { onSettingsClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = "Settings"
                )
            }
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary
    )
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomePageViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current
    Log.d("HomePage/HomeScreen", "about to collect states")

    val coroutineScope = rememberCoroutineScope()

    val theTimerValue: Long = 30000
    var timerValue = theTimerValue
    var currentTimeMillis by remember { mutableStateOf(timerValue) }
    var isRunning by remember { mutableStateOf(false) }
    val timerText = remember { mutableStateOf(timeFormat(timerValue)) }

    LaunchedEffect(isRunning) {
        while (isRunning && currentTimeMillis > 0) {
            delay(1000)
            currentTimeMillis -= 1000
            timerText.value = timeFormat(currentTimeMillis)
        }
        if (currentTimeMillis <= 0) {
            viewModel.startNextInterval()
        }
    }

    Log.d("HomePage/HomeScreen", "about to scaffold top and bottom bar")
    Scaffold(
        topBar = {
            TuneTideTopAppBar(
                onSettingsClick = {
                    val intent = Intent(context, SettingsActivity::class.java)
                    context.startActivity(intent)
                }
            )
        },
        bottomBar = {
            TuneTideBottomAppBar()
        },
        modifier = modifier
    ) { innerPadding ->
        HomeBody(
            viewModel,
            modifier = modifier,
            contentPadding = innerPadding
        )
    }
}

@Composable
fun HomeBody(
    viewModel: HomePageViewModel,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
) {
    Log.d("HomePage/HomeBody", "about to create main layout")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleBackground)
            .padding(contentPadding),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TideFlow()
        TimerBody(
            viewModel = viewModel(factory = AppViewModelProvider.Factory),
            modifier = modifier)
        Spacer(modifier = Modifier.height(16.dp))
        MusicPlayerBody(viewModel = viewModel(factory = AppViewModelProvider.Factory),
            modifier = modifier)
    }
}

@Composable
fun TideFlow() {
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
}

@Composable
fun TimerBody(
    viewModel: HomePageViewModel,
    modifier: Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    Log.d("HomePage/TimerBody", "about layout timer body")

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
                    text = "30",
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
                    // Playback control logic goes here
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.pausebutton),
                        contentDescription = "Pause Button",
                        modifier = Modifier.size(30.dp)
                    )
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
                                    "1",
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
                                    "3 of 4",
                                    color = Color(0xFF241673).copy(alpha = 0.5f),
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            Color(0xFFE0BFDF),
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                        .padding(horizontal = 8.dp)
                                ) {
                                    Text(
                                        "flow",
                                        color = Color(0xFFBF5FFF),
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        "12:36",
                                        color = Color(0xFF821A93),
                                        fontSize = 12.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            Color(0xFFBFCEE0),
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                        .padding(horizontal = 8.dp)
                                ) {
                                    Text(
                                        "break",
                                        color = Color(0xFF4F5F71),
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
                                    "3",
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
}

@Composable
fun MusicPlayerBody(
    viewModel: HomePageViewModel,
    modifier: Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .width(344.dp)
            .height(212.dp)
            .background(Color(0xFFE6E5F2), shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        // Placeholder for music player UI
    }
}
