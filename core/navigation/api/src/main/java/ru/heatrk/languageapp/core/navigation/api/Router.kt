package ru.heatrk.languageapp.core.navigation.api

import kotlinx.coroutines.flow.Flow

interface Router {
    val actions: Flow<RoutingAction>

    suspend fun navigate(route: Route, options: RoutingOptions = RoutingOptions())

    suspend fun navigateBack()
}
