package com.example.tunetide.ui.home

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tunetide.R
import com.example.tunetide.ui.TuneTideBottomAppBar
import com.example.tunetide.ui.TuneTideTopAppBar
import com.example.tunetide.ui.theme.PurpleBackground
import kotlinx.coroutines.delay
import java.util.Locale
import com.example.tunetide.ui.AppViewModelProvider
import com.example.tunetide.ui.navigation.NavigationDestination
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import com.example.tunetide.database.Playback
import com.example.tunetide.database.Timer
import com.example.tunetide.ui.theme.Greyish
import com.example.tunetide.ui.timer.TimerUIState
import com.example.tunetide.ui.timer.toTimer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.max

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.home_page_name
}

@Composable
fun HomeScreenTimer(
    modifier: Modifier = Modifier,
    viewModel: HomePageViewModel,
    playback: PlaybackUIState,
    timer: Timer,
) {

    // TEMP IN -> RUNTIME ERROR
    var timerValue = 30000

    // TEMP OUT -> RUNTIME ERROR IN DATABASE ACCESS
    //var timerValue = viewModel.getStartingTimerValue().toLong()
    var currentTimeMillis by remember { mutableStateOf(timerValue) }
    viewModel.setCurrentTime(currentTimeMillis.toInt())

    // TODO @MIA @KATHERINE @NOUR figure out updating database (seconds remaining) when app close
    //      too costly / inefficent to update every second
    // ^ Will do after database access errors resolved
    LaunchedEffect(playback.isPlaying) {
        while (playback.isPlaying && currentTimeMillis > 0) {
            delay(1000)
            currentTimeMillis -= 1000
            viewModel.setCurrentTime(currentTimeMillis.toInt())
        }
        if (currentTimeMillis <= 0) {
            if (timer.isInterval) {
                viewModel.startNextInterval()
            }
            viewModel.setCurrentTime(currentTimeMillis.toInt())
        }
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomePageViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val timer: Timer = viewModel.timerUIState.collectAsState().value.timerDetails.toTimer()
    val playback: PlaybackUIState = viewModel.playbackUIState.collectAsState().value
    HomeScreenTimer(modifier, viewModel, playback, timer)
    Scaffold(
        topBar = {
            TuneTideTopAppBar()
        },
        bottomBar = {
            TuneTideBottomAppBar()
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
    playback: PlaybackUIState,
    timer: Timer,
    contentPadding: PaddingValues = PaddingValues(16.dp),
) {
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
            viewModel = viewModel,
            modifier = modifier,
            playback = playback,
            timer = timer)
        Spacer(modifier = Modifier.height(16.dp))
        MusicPlayerBody(viewModel = viewModel(factory = AppViewModelProvider.Factory),
            modifier = modifier) // TODO @KIANA @ERICA inject musicUIState here
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
    playback: PlaybackUIState,
    timer: Timer,
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
                modifier = modifier.weight(1f, false),
                playback = playback,
                timer = timer)
        }
    }
}

@Composable
fun TimerRightPanel(
    viewModel: HomePageViewModel,
    modifier: Modifier,
    playback: PlaybackUIState,
    timer: Timer,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        PlayButton(viewModel = viewModel,
            modifier = modifier,
            playback = playback,
            timer = timer)

        Box(
            modifier = Modifier
                .weight(1f)
                .height(180.dp)
                .background(Color(0xFF544FA3), shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.TopStart
        ) {
            InfoBody(viewModel = viewModel,
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
    playback: PlaybackUIState,
    timer: Timer,
) {
    Text(
        text = viewModel.timeFormat(playback.currentIntervalRemainingSeconds.toLong()),
        color = Color.White,
        fontSize = 48.sp,
        textAlign = TextAlign.Center
    )
}

@Composable
fun PlayButton(
    viewModel: HomePageViewModel,
    modifier: Modifier,
    playback: PlaybackUIState,
    timer: Timer,
) {
    val coroutineScope = rememberCoroutineScope()

    IconButton(onClick = {
        if (playback.isPlaying) {
            coroutineScope.launch {
                // TEMP OUT -> RUNTIME ERROR
                //viewModel.pause()
                viewModel.changePlayingStatus(false)
            }
        } else{
            coroutineScope.launch {
                // TEMP OUT -> RUNTIME ERROR
                //viewModel.play()
                viewModel.changePlayingStatus(true)
            }
        }

    }) {
        if (playback.isPlaying) {
        Image(
            painter = painterResource(id = R.drawable.pausebutton),
            contentDescription = "Pause Button",
            modifier = Modifier.size(30.dp)
        )
        } else {
            Image(
                painter = painterResource(id = R.drawable.playbutton),
                contentDescription = "Play Button",
                modifier = Modifier.size(30.dp)
            )
        }
    }

}

@Composable
fun InfoBody(
    viewModel: HomePageViewModel,
    modifier: Modifier,
    playback: PlaybackUIState,
    timer: Timer,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        if (timer.isInterval) {
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
        else {
            Text(
                "Standard Timer",
                color = Color(Greyish.value),
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight(align = Alignment.CenterVertically),
            )
        }
    }

}

@Composable
fun CompletedDisplay(
    viewModel: HomePageViewModel,
    modifier: Modifier,
    playback: PlaybackUIState,
    timer: Timer,
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
                max(0, playback.currentInterval - 1).toString(),
                color = Color(0xFF2B217F),
                fontSize = 12.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
    }

}

@Composable
fun RemainingDisplay(
    viewModel: HomePageViewModel,
    modifier: Modifier,
    playback: PlaybackUIState,
    timer: Timer,
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

@Composable
fun FlowBreakDisplay(
    viewModel: HomePageViewModel,
    modifier: Modifier,
    playback: PlaybackUIState,
    timer: Timer,
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
            Text("Interval "
                    + playback.currentInterval.toString()
                    + " of " + timer.numIntervals,
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
                    color = Color(0xFF821A93),
                    fontSize = 12.sp
                )
                if (playback.stateType == 0) {
                    // FLOW IS ON
                    Text(
                        viewModel.timeFormat(playback.currentIntervalRemainingSeconds.toLong()),
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
                        viewModel.timeFormat(playback.currentIntervalRemainingSeconds.toLong()),
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
    modifier: Modifier) {
    // TODO @KIANA will be injected another way (more top level/singleton)? (not sure how)
    /*
    var mp3Player: MP3Player = MP3Player(context)
    */
    Box(
        modifier = Modifier
            .width(344.dp)
            .height(212.dp)
            .background(Color(0xFFE6E5F2), shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        // TODO @KIANA (see above for how to inject singleton mp3player)
        // Placeholder for music player UI
        // mp3Player.layout()
    }
}

// TODO @KIANA this will be in a viewModel class
/*
fun onDestroy() {
    mp3Player.onDestroy()
}
*/



