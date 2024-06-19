package com.example.tunetide

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.tunetide.ui.theme.TuneTideTheme
import com.example.tunetide.ui.HomePage

class MainActivity : ComponentActivity() {
    private lateinit var homePage: HomePage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homePage = HomePage(this)

        setContent {
            TuneTideTheme {
                homePage.layout()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        homePage.onDestroy();
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TuneTideTheme {
    }
}