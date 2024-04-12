package ru.heatrk.languageapp.di

import android.app.Application
import org.koin.dsl.module
import ru.heatrk.languageapp.LanguageApplication

val appModule = module {
    single<Application> { LanguageApplication.instance }
}
