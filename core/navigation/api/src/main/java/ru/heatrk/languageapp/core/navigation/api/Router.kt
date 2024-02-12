package ru.heatrk.languageapp.core.navigation.api

import kotlinx.coroutines.flow.Flow

interface Router {
    val actions: Flow<RoutingAction>

    suspend fun navigate(route: Route)

    suspend fun navigateBack()
}