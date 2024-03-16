package ru.heatrk.languageapp.core.navigation.api

interface Router {
    val previousRoute: String?

    val currentRoute: String?

    suspend fun navigate(routePath: String, options: List<RoutingOption> = emptyList())

    suspend fun navigateBack()
}
