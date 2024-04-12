package ru.heatrk.languageapp.core.env.di

import org.koin.dsl.module
import ru.heatrk.languageapp.core.env.EnvironmentConfig
import ru.heatrk.languageapp.core.env.EnvironmentConfigAndroid

val androidEnvironmentConfigModule = module {
    single<EnvironmentConfig> { EnvironmentConfigAndroid() }
}
