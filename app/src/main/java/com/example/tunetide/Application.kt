package com.example.tunetide

import android.app.Application
import android.util.Log

class Application : Application() {

    // AppContainer instance (the other classes use this for dependency injection)
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        Log.d("TuneTideApplication", "Initializing container")
        container = AppDataContainer(this)
    }
}