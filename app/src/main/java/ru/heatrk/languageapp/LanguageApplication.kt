package ru.heatrk.languageapp

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.decode.ImageDecoderDecoder

class LanguageApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        _instance = this

        Coil.setImageLoader {
            ImageLoader.Builder(this)
                .components {
                    add(ImageDecoderDecoder.Factory())
                }
                .build()
        }
    }

    companion object {
        private var _instance: LanguageApplication? = null
        val instance get() = requireNotNull(_instance)
    }
}