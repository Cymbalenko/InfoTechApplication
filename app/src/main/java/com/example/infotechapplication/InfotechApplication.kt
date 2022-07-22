package com.example.infotechapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class InfotechApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance=this
    }
    companion object {
        var instance: InfotechApplication? = null
    }
}