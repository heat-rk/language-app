package ru.heatrk.languageapp.core.di

import org.koin.dsl.module
import ru.heatrk.languageapp.core.AppLogger
import ru.heatrk.languageapp.core.AppLoggerAndroid

val androidLoggerModule = module {
    single<AppLogger> { AppLoggerAndroid() }
}
