package ru.heatrk.languageapp.profile.impl.ui.navigation

import ru.heatrk.languageapp.core.navigation.compose_impl.Route
import ru.heatrk.languageapp.core.navigation.compose_impl.route
import ru.heatrk.languageapp.profile.api.ui.navigation.PROFILE_GRAPH_ROUTE

object ProfileGraphRoute : Route.Graph(
    path = PROFILE_GRAPH_ROUTE,
    startDestination = PROFILE_SCREEN_ROUTE,
    builder = {
        route(ProfileScreenRoute)
    },
)
