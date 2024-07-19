package com.example.tunetide.ui.timers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tunetide.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit

data class BoxWithImageItem(
    val title: Int,
    val subTitle: Int,
    val onClick: () -> Unit,
    val onEditClick: () -> Unit // Added onEditClick callback
)

@Composable
fun BoxWithImage(
    title: String,
    subTitle: String,
    onClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(Color.White)
            .height(100.dp), // Adjust the height as needed
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp), // Adjust padding as needed
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .weight(1f) // Ensure the text column takes up available space
            ) {
                Text(text = title, fontWeight = FontWeight.Bold)
                Text(text = subTitle, modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp))
            }
            Row {
                Box(
                    modifier = Modifier
                        .padding(20.dp)
                        .size(54.dp), // Adjust the size as needed
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier
                            .size(54.dp)
                            .clickable(onClick = onClick),
                        painter = painterResource(id = R.drawable.playbutton),
                        contentDescription = "Play Button"
                    )
                }
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit, // Using Material Icon instead
                        contentDescription = "Edit Button"
                    )
                }
            }
        }
    }
}
