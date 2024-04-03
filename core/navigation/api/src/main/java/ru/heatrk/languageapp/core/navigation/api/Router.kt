package ru.heatrk.languageapp.core.navigation.api

interface Router {
    val previousRoute: String?

    val currentRoute: String?

    fun registerRoutingBackReceiver(receiver: RoutingBackReceiver)

    fun unregisterRoutingBackReceiver(receiver: RoutingBackReceiver)

    suspend fun navigate(
        routePath: String,
        options: List<RoutingOption> = emptyList(),
        arguments: Map<String, Any> = emptyMap(),
    )

    suspend fun navigateBack()
}
