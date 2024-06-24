package com.example.tunetide

import android.app.Application

class TuneTideApplication : Application() {

    // AppContainer instance (the other classes use this for dependency injection)
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}