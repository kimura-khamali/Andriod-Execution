package com.agnes.multimedia

import android.app.Application
import android.content.Context

class MyApp : Application() {
    companion object{
        lateinit var appContext: Context
    }
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
}
