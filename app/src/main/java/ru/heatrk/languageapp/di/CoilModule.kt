package ru.heatrk.languageapp.di

import android.app.Application
import coil.Coil
import coil.ImageLoader
import org.koin.dsl.module

val coilModule = module {
    single<ImageLoader> {
        Coil.imageLoader(
            context = get<Application>()
        )
    }
}
