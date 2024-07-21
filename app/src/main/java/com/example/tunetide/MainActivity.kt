package com.example.tunetide

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.tunetide.ui.TuneTideApp
import com.example.tunetide.ui.theme.TuneTideTheme

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("MainActivity", "Permission granted")
            permissionGranted = true
        } else {
            Log.d("MainActivity", "Permission denied")
        }
    }

    private var permissionGranted by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (checkSelfPermission(Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            permissionGranted = true
        }

        setContent {
            TuneTideTheme {
                if (permissionGranted) {
                    Log.d("MainActivity", "calling app")
                    TuneTideApp()
                } else {
                    Log.d("MainActivity", "Waiting for permission")
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CODE = 123
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TuneTideTheme {
    }
}