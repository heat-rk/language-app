package ru.heatrk.languageapp.core.data.http_client.di

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import scout.definition.Registry

fun Registry.useHttpClientBeans() {
    singleton<HttpClient> {
        HttpClient {
            // config ....
            expectSuccess = true

            // plugins ....
            install(Logging) {
                logger = object: Logger {
                    override fun log(message: String) {
                        Log.d("HTTP Client", message)
                    }
                }
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(get())
            }
        }
    }
}
