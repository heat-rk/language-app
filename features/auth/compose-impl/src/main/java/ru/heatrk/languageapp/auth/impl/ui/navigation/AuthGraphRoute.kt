package ru.heatrk.languageapp.auth.impl.ui.navigation

import ru.heatrk.languageapp.auth.api.ui.navigation.AUTH_GRAPH_ROUTE_PATH
import ru.heatrk.languageapp.core.navigation.api.Route
import ru.heatrk.languageapp.core.navigation.api.route

object AuthGraphRoute : Route.Graph(
    path = AUTH_GRAPH_ROUTE_PATH,
    startDestination = SignInScreenRoute,
    builder = {
        route(SignInScreenRoute)
        route(SignUpScreenRoute)
    }
)
