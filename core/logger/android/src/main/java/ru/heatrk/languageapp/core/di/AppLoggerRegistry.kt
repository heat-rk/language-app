package ru.heatrk.languageapp.core.di

import ru.heatrk.languageapp.core.AppLogger
import ru.heatrk.languageapp.core.AppLoggerAndroid
import scout.definition.Registry

fun Registry.useAndroidLoggerBeans() {
    singleton<AppLogger> { AppLoggerAndroid() }
}
