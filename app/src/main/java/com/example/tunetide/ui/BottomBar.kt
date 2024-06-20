package com.example.tunetide.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tunetide.R
import com.example.tunetide.ui.theme.PurpleAccent
import com.example.tunetide.ui.theme.PurpleBackground

class BottomBar {
    @Composable
    fun layout() {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.bottom_bar_blank),
                contentDescription = "Bottom Bar",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                IconButton(onClick = { /* add navigation action */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar),
                        contentDescription = "Calendar", tint = PurpleBackground,
                        modifier = Modifier.size(30.dp)
                    )
                }
                IconButton(onClick = { /* add navigation action */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.home),
                        contentDescription = "Home", tint = PurpleAccent,
                        modifier = Modifier.size(30.dp)
                    )
                }
                IconButton(onClick = { /* add navigation action */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.timer),
                        contentDescription = "Timer", tint = PurpleBackground,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}