package com.example.tunetide.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tunetide.R
import com.example.tunetide.ui.theme.PurpleBackground

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SettingsScreen { finish() }  // Pass the finish function to handle back navigation
            }
        }
    }
}

@Composable
fun SettingsScreen(onBackClick: () -> Unit) {
    var isDarkModeEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleBackground)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back_arrow),
                contentDescription = stringResource(id = R.string.back),
                modifier = Modifier.clickable { onBackClick() }  // Handle back arrow click
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "tunetide",
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF2B217F)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { isDarkModeEnabled = !isDarkModeEnabled },  // Toggle dark mode
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.dark_mode),
                fontSize = 18.sp,
                color = Color(0xFF333333),
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = isDarkModeEnabled,
                onCheckedChange = { isDarkModeEnabled = it }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { /* TODO: Implement Connect Spotify */ },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.connect_spotify),
                fontSize = 18.sp,
                color = Color(0xFF333333),
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_spotify),
                contentDescription = stringResource(id = R.string.spotify)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { /* TODO: Implement Manage Local Music Files */ },
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
