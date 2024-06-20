package com.example.tunetide.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tunetide.R
import com.example.tunetide.ui.theme.PurpleAccent
import com.example.tunetide.ui.theme.PurpleBackground

class TopBar {
    @Composable
    fun layout() {
        TopAppBar(
            backgroundColor = PurpleBackground,
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            IconButton(onClick = { /* add navigation action */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.settings),
                    contentDescription = "Settings", tint = PurpleAccent,
                    modifier = Modifier.size(30.dp)
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
                    contentDescription = "Queue", tint = PurpleAccent,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}