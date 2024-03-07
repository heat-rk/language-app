package ru.heatrk.languageapp.core.navigation.api

interface Router {
    suspend fun navigate(routePath: String, options: RoutingOptions = RoutingOptions())

    suspend fun navigateBack()
}
