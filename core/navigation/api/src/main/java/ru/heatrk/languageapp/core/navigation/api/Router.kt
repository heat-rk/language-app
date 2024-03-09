package ru.heatrk.languageapp.core.navigation.api

interface Router {
    val isFirstDestination: Boolean

    val currentRoute: String?

    suspend fun navigate(routePath: String, options: RoutingOptions = RoutingOptions())

    suspend fun navigateBack()
}
