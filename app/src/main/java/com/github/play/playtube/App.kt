package com.github.play.playtube

import android.app.Application
import android.util.Log
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException
import org.schabi.newpipe.extractor.NewPipe
import org.schabi.newpipe.extractor.downloader.Downloader

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Inicializar yt-dlp (motor de Seal)
        try {
            YoutubeDL.getInstance().init(this)
        } catch (e: YoutubeDLException) {
            Log.e("PlayTube", "Error al iniciar yt-dlp", e)
        }
    }
}