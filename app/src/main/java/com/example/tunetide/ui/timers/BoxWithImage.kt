package com.example.tunetide.ui.timers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tunetide.R
import com.example.tunetide.ui.theme.PurpleAccent
import com.example.tunetide.ui.theme.PurpleDark
import com.example.tunetide.ui.theme.PurpleLight2

data class BoxWithImageItem(
    val title: Int,
    val subTitle: Int,
    val onClick: () -> Unit,
    val onEditClick: () -> Unit
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
            .background(PurpleLight2)
            .height(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .weight(1f)
            ) {
                Text(text = title, color = PurpleDark, fontWeight = FontWeight.Bold)
                Text(text = subTitle, color = PurpleAccent, modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp))
            }
            Row {
                Box(
                    modifier = Modifier
                        .padding(20.dp)
                        .size(54.dp),
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
                        imageVector = Icons.Default.Edit,
                        tint = PurpleAccent,
                        contentDescription = "Edit Button"
                    )
                }
            }
        }
    }
}
