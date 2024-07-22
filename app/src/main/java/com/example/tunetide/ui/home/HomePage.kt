package com.example.tunetide.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tunetide.R
import com.example.tunetide.ui.AppViewModelProvider
import com.example.tunetide.ui.TuneTideBottomAppBar
import com.example.tunetide.ui.TuneTideTopAppBar
import com.example.tunetide.ui.navigation.NavigationDestination
import com.example.tunetide.ui.theme.Greyish
import com.example.tunetide.ui.theme.PurpleBackground
import com.example.tunetide.ui.theme.PurpleDark
import com.example.tunetide.ui.timer.TimerDetails
import kotlinx.coroutines.launch
import kotlin.math.max

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.home_page_name
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToSettings: () -> Unit,
    navigateToTimersList: () -> Unit,
    viewModel: HomePageViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Unwrap timer and playback
    val timer: TimerDetails = viewModel.timerUIState.collectAsState().value.timerDetails
    val playback: PlaybackDetails = viewModel.playbackUIState.collectAsState().value.playbackDetails

    Scaffold(
        topBar = {
            TuneTideTopAppBar()
        },
        bottomBar = {
            TuneTideBottomAppBar(
                currentScreen = R.string.home_screen,
                navigateToSettings = navigateToSettings,
                navigateToHome = {},
                navigateToTimersList = navigateToTimersList
            )
        },
        modifier = modifier
    ) { innerPadding ->
        HomeBody(
            viewModel,
            modifier = modifier,
            playback = playback,
            timer = timer,
            innerPadding)
    }
}

@Composable
fun HomeBody(
    viewModel: HomePageViewModel,
    modifier: Modifier = Modifier,
    playback: PlaybackDetails,
    timer: TimerDetails,
    contentPadding: PaddingValues = PaddingValues(16.dp)
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleBackground)
            .padding(contentPadding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TideFlow()
        TimerBody(
            viewModel = viewModel,
            modifier = modifier,
            playback = playback,
            timer = timer
        )
        Spacer(modifier = Modifier.height(16.dp))
        MusicPlayerBody(
            viewModel = viewModel(factory = AppViewModelProvider.Factory),
            modifier = modifier
        )
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
    modifier: Modifier,
    playback: PlaybackDetails,
    timer: TimerDetails,
) {
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
                TimerDisplay(
                    viewModel = viewModel,
                    modifier = modifier,
                    playback = playback,
                    timer = timer)
            }
            Spacer(modifier = Modifier.width(16.dp))
            TimerRightPanel(
                viewModel = viewModel,
                modifier = modifier.weight(1f),
                playback = playback,
                timer = timer)
        }
    }
}

@Composable
fun TimerRightPanel(
    viewModel: HomePageViewModel,
    modifier: Modifier,
    playback: PlaybackDetails,
    timer: TimerDetails,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
    ) {
        Row (
            modifier = modifier
        ) {
            /*
            CancelButton(
                viewModel = viewModel,
                modifier = modifier,
                playback = playback,
                timer = timer)

             */
            /*
            RestartButton(
                viewModel = viewModel,
                modifier = modifier,
                playback = playback,
                timer = timer)
             */
            PlayButton(
                viewModel = viewModel,
                modifier = modifier,
                playback = playback,
                timer = timer)

            ShuffleButton(
                viewModel = viewModel,
                modifier = modifier,
                playback = playback,
                timer = timer)
        }

        Box(
            modifier = Modifier
                .height(190.dp)
                .background(Color(0xFF544FA3), shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.TopStart,
        ) {
            InfoBody(
                viewModel = viewModel,
                modifier = modifier,
                playback = playback,
                timer = timer)
        }
    }

}

@Composable
fun TimerDisplay(
    viewModel: HomePageViewModel,
    modifier: Modifier,
    playback: PlaybackDetails,
    timer: TimerDetails,
) {
    Text(
        // LIVE TIMER
        text = viewModel.timeFormat(viewModel.currentTimerVal.collectAsState().value.toLong()),
        color = Color.White,
        fontSize = 36.sp,
        textAlign = TextAlign.Center
    )
}

@Composable
fun ShuffleButton(
    viewModel: HomePageViewModel,
    modifier: Modifier,
    playback: PlaybackDetails,
    timer: TimerDetails,
) {
    //var selected by remember { mutableStateOf(false) }
    //val color = if (selected) Color.Black else Color.Transparent

    val coroutineScope = rememberCoroutineScope()

    IconButton(onClick = {
        //selected = !selected
        viewModel.toggleShuffle() })
        //modifier.background(if (selected) Color.Black else Color.Transparent, shape = RoundedCornerShape(100.dp))
    {

        if (viewModel.isPlaying.collectAsState().value && timer.spotifyFlowMusicPlaylistId != -1) {
            Image(
                painter = painterResource(id = R.drawable.shufflebutton),
                contentDescription = "Shuffle Button",
                modifier = Modifier
                    .size(50.dp)
                    .padding(5.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.shuffle),
                contentDescription = "Shuffle symbol",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            )
        }
    }
}

@Composable
fun PlayButton(
    viewModel: HomePageViewModel,
    modifier: Modifier,
    playback: PlaybackDetails,
    timer: TimerDetails,
) {
    val coroutineScope = rememberCoroutineScope()

    IconButton(onClick = {
        if (viewModel.isPlaying.value) {
            coroutineScope.launch {
                viewModel.pause()
            }
        } else{
            coroutineScope.launch {
                viewModel.play()
            }
        }

    }) {
        if (viewModel.isPlaying.collectAsState().value) {
        Image(
            painter = painterResource(id = R.drawable.pausebutton),
            contentDescription = "Pause Button",
            modifier = Modifier
                .size(50.dp)
                .padding(5.dp)
        )
        } else {
            Image(
                painter = painterResource(id = R.drawable.playbutton),
                contentDescription = "Play Button",
                modifier = Modifier
                    .size(50.dp)
                    .padding(5.dp)
            )
        }
    }

}

@Composable
fun CancelButton(
    viewModel: HomePageViewModel,
    modifier: Modifier,
    playback: PlaybackDetails,
    timer: TimerDetails,
) {
    val coroutineScope = rememberCoroutineScope()

    IconButton(onClick = {
        coroutineScope.launch {
            viewModel.finish()
        }

    }) {
        Image(
            painter = painterResource(id = R.drawable.cancelbutton),
            contentDescription = "Cancel Button",
            modifier = Modifier.size(30.dp)
        )
    }

}

@Composable
fun RestartButton(
    viewModel: HomePageViewModel,
    modifier: Modifier,
    playback: PlaybackDetails,
    timer: TimerDetails,
) {
    val coroutineScope = rememberCoroutineScope()

    IconButton(onClick = {
        coroutineScope.launch {
            viewModel.restart()
        }
    }) {
        Image(
            painter = painterResource(id = R.drawable.restartbutton),
            contentDescription = "Restart Button",
            modifier = Modifier.size(30.dp)
        )
    }

}

@Composable
fun InfoBody(
    viewModel: HomePageViewModel,
    modifier: Modifier,
    playback: PlaybackDetails,
    timer: TimerDetails,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        if (playback.timerId == -1) {
            Text(
                "No Timer Playing",
                color = Color(Greyish.value),
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight(align = Alignment.CenterVertically),
            )
        }
        else {
            CompletedDisplay(
                viewModel = viewModel,
                modifier = modifier,
                playback = playback,
                timer = timer
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowBreakDisplay(
                viewModel = viewModel,
                modifier = modifier,
                playback = playback,
                timer = timer
            )
            Spacer(modifier = Modifier.height(8.dp))
            RemainingDisplay(
                viewModel = viewModel,
                modifier = modifier,
                playback = playback,
                timer = timer
            )

        }
    }

}

@Composable
fun CompletedDisplay(
    viewModel: HomePageViewModel,
    modifier: Modifier,
    playback: PlaybackDetails,
    timer: TimerDetails,
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
            if (timer.isInterval) {
                Text(
                    "completed",
                    color = Color(0xFF2B217F),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    max(0, playback.currentInterval - 1).toString(),
                    color = Color(0xFF2B217F),
                    fontSize = 12.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

}

@Composable
fun RemainingDisplay(
    viewModel: HomePageViewModel,
    modifier: Modifier,
    playback: PlaybackDetails,
    timer: TimerDetails,
) {

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
            if (timer.isInterval) {
                Text(
                    "remaining",
                    color = Color(0xFF2B217F),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    (timer.numIntervals - playback.currentInterval).toString(),
                    color = Color(0xFF2B217F),
                    fontSize = 12.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

}

@Composable
fun FlowBreakDisplay(
    viewModel: HomePageViewModel,
    modifier: Modifier,
    playback: PlaybackDetails,
    timer: TimerDetails,
) {

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
            if (timer.isInterval) {
                Text("Interval "
                        + playback.currentInterval.toString()
                        + " of " + timer.numIntervals,
                    color = Color(0xFF241673).copy(alpha = 0.5f),
                    fontSize = 12.sp
                )
            }
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
                    color = Color(0xFF821A93),
                    fontSize = 12.sp
                )
                if (playback.stateType == 0) {
                    // FLOW IS ON
                    Text(
                        // LIVE TIMER
                        viewModel.timeFormat(viewModel.currentTimerVal.collectAsState().value.toLong()),
                        color = Color(0xFF821A93),
                        fontSize = 12.sp
                    )

                }
                else if (playback.stateType == 1) {
                    // BREAK IS ON
                    Text(
                        viewModel.timeFormat(timer.flowMusicDurationSeconds.toLong()),
                        color = Color(0xFFBF5FFF),
                        fontSize = 12.sp
                    )

                }
                else {
                    // NONE
                    Text(
                        "00:00",
                        color = Color(0xFFBF5FFF),
                        fontSize = 12.sp
                    )

                }
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
                if (playback.stateType == 0) {
                    // FLOW IS ON
                    Text(
                        viewModel.timeFormat(timer.breakMusicDurationSeconds.toLong()),
                        color = Color(0xFFB2A9A9),
                        fontSize = 12.sp
                    )

                }
                else if (playback.stateType == 1) {
                    // BREAK IS ON
                    Text(
                        // LIVE TIMER
                        viewModel.timeFormat(viewModel.currentTimerVal.collectAsState().value.toLong()),
                        color = Color(0xFF4F5F71),
                        fontSize = 12.sp
                    )

                }
                else {
                    // NONE
                    Text(
                        "00:00",
                        color = Color(0xFFB2A9A9),
                        fontSize = 12.sp
                    )

                }
            }
        }
    }

}

@Composable
fun MusicPlayerBody(
    viewModel: HomePageViewModel,
    modifier: Modifier = Modifier
) {
    val currentSongName by viewModel.currentSongName.collectAsState()
    val currentPlaylistName by viewModel.currentPlaylistName.collectAsState()

    //IF WE ONLY WANT THE SONG NAME TO SHOW WHEN PLAYING:
    val isPlaying by viewModel.isPlaying.collectAsState()

    Box(
        modifier = Modifier
            .width(344.dp)
            .height(212.dp)
            .background(Color(0xFFE6E5F2), shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                //IF WE ONLY WANT THE SONG NAME TO SHOW WHEN PLAYING:
                text = if (isPlaying) {
                    currentSongName
                } else {
                    "Not Playing"
                },
                //text = currentSongName,
                color = PurpleDark,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = currentPlaylistName,
                color = PurpleDark,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { viewModel.skipSong() }) {
                Text(
                    text = "Next Song",
                    color = PurpleDark)
            }
        }
    }
}



