package com.github.play.playtube

import android.app.Application
import android.util.Log
import com.yausername.youtubedl_android.YoutubeDL

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            YoutubeDL.getInstance().init(applicationContext)
            Log.d("PlayTube", "YoutubeDL inicializado correctamente")
        } catch (e: Exception) {
            Log.e("PlayTube", "Error al inicializar YoutubeDL", e)
        }
    }
}