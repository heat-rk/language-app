package ru.heatrk.languageapp.core.navigation.api

import android.content.Intent

interface DeepLinkRouter {
    suspend fun handle(intent: Intent): Boolean
}
