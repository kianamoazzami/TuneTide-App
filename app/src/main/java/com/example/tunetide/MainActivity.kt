package com.example.tunetide

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.tunetide.ui.TuneTideApp
import com.example.tunetide.ui.theme.TuneTideTheme


class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            TuneTideTheme {
                Log.d("TuneTideMainActivity", "calling app")
                TuneTideApp()
            }
        }

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TuneTideTheme {
    }
}