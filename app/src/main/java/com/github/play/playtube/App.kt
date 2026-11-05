package com.github.play.playtube

import android.app.Application
import android.util.Log
import com.yausername.youtubedl_android.YoutubeDL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // Inicializar en segundo plano para evitar que la app crashee al arrancar
        CoroutineScope(Dispatchers.IO).launch {
            try {
                YoutubeDL.getInstance().init(applicationContext)
                Log.d("PlayTube", "YoutubeDL inicializado con éxito.")
            } catch (e: Throwable) {
                Log.e("PlayTube", "Error inicializando YoutubeDL: ${e.message}", e)
            }
        }
    }
}