package ru.heatrk.languageapp.core.data.serialization.di

import kotlinx.serialization.json.Json
import scout.definition.Registry

fun Registry.useSerializationBeans() {
    singleton<Json> {
        Json {
            encodeDefaults = true
            coerceInputValues = true
            ignoreUnknownKeys = true
        }
    }
}
