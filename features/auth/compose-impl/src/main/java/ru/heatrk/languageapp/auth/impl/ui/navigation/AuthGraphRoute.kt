package ru.heatrk.languageapp.auth.impl.ui.navigation

import ru.heatrk.languageapp.auth.api.ui.navigation.AUTH_GRAPH_ROUTE_PATH
import ru.heatrk.languageapp.auth.impl.di.AuthComponent
import ru.heatrk.languageapp.core.navigation.api.Route
import ru.heatrk.languageapp.core.navigation.api.route

object AuthGraphRoute : Route.Graph(
    path = AUTH_GRAPH_ROUTE_PATH,
    startDestination = SIGN_IN_SCREEN_ROUTE_PATH,
    builder = {
        val signInViewModelFactory = AuthComponent.signInViewModelFactory
        val signUpViewModelFactory = AuthComponent.signUpViewModelFactory

        route(SignInScreenRoute(signInViewModelFactory = signInViewModelFactory))
        route(SignUpFlowRoute(signUpViewModelFactory = signUpViewModelFactory))
    }
)
