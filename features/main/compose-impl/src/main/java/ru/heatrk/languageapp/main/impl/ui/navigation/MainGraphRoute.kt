package ru.heatrk.languageapp.main.impl.ui.navigation

import ru.heatrk.languageapp.core.navigation.compose_impl.Route
import ru.heatrk.languageapp.core.navigation.compose_impl.route
import ru.heatrk.languageapp.main.api.ui.navigation.MAIN_GRAPH_ROUTE_PATH
import ru.heatrk.languageapp.main.impl.di.MainComponent

object MainGraphRoute : Route.Graph(
    path = MAIN_GRAPH_ROUTE_PATH,
    startDestination = MAIN_SCREEN_ROUTE_PATH,
    builder = {
        val mainViewModelFactory = MainComponent.mainViewModelFactory

        route(MainScreenRoute(viewModelFactory = mainViewModelFactory))
    },
)
