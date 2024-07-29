package com.example.tunetide.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tunetide.R
import com.example.tunetide.ui.TuneTideBottomAppBar
import com.example.tunetide.ui.TuneTideTopAppBar
import com.example.tunetide.ui.navigation.NavigationDestination
import com.example.tunetide.ui.theme.PurpleBackground

//class SettingsActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MaterialTheme {
//                SettingsScreen { finish() }  // Pass the finish function to handle back navigation
//            }
//        }
//    }
//}

object SettingsDestination : NavigationDestination {
    override val route = "settings"
    override val titleRes = R.string.settings_page_name
}

@Composable
fun SettingsScreen(
    navigateToHome: () -> Unit,
    navigateToTimersList: () -> Unit,
    navigateToLocalFiles: () -> Unit
) {
    var isDarkModeEnabled by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TuneTideTopAppBar()
        },
        bottomBar = {
            TuneTideBottomAppBar(
                currentScreen = R.string.settings_screen,
                navigateToSettings = {},
                navigateToHome = navigateToHome,
                navigateToTimersList = navigateToTimersList
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleBackground)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable(onClick = navigateToLocalFiles),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.manage_local_music_files),
                    fontSize = 18.sp,
                    color = Color(0xFF333333),
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable { /* TODO: Implement Schedule Settings */ },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.schedule_settings),
                    fontSize = 18.sp,
                    color = Color(0xFF333333),
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = null
                )
            }
        }
    }
}
