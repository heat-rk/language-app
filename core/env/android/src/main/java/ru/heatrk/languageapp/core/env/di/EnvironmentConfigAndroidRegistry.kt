package ru.heatrk.languageapp.core.env.di

import ru.heatrk.languageapp.core.env.EnvironmentConfig
import ru.heatrk.languageapp.core.env.EnvironmentConfigAndroid
import scout.definition.Registry

fun Registry.useAndroidEnvironmentConfig() {
    singleton<EnvironmentConfig> { EnvironmentConfigAndroid() }
}
