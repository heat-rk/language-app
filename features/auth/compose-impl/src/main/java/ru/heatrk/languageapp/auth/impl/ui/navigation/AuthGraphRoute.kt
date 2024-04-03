package ru.heatrk.languageapp.auth.impl.ui.navigation

import ru.heatrk.languageapp.auth.api.ui.navigation.AUTH_GRAPH_ROUTE_PATH
import ru.heatrk.languageapp.auth.impl.di.AuthComponent
import ru.heatrk.languageapp.auth.impl.ui.navigation.recovery.RecoveryFlowRoute
import ru.heatrk.languageapp.auth.impl.ui.navigation.sign_in.SIGN_IN_SCREEN_ROUTE_PATH
import ru.heatrk.languageapp.auth.impl.ui.navigation.sign_in.SignInScreenRoute
import ru.heatrk.languageapp.auth.impl.ui.navigation.sign_up.SignUpFlowRoute
import ru.heatrk.languageapp.core.navigation.compose_impl.Route
import ru.heatrk.languageapp.core.navigation.compose_impl.route

object AuthGraphRoute : Route.Graph(
    path = AUTH_GRAPH_ROUTE_PATH,
    startDestination = SIGN_IN_SCREEN_ROUTE_PATH,
    builder = {
        val signInViewModelFactory = AuthComponent.signInViewModelFactory
        val signUpViewModelFactory = AuthComponent.signUpViewModelFactory
        val recoveryViewModelFactory = AuthComponent.recoveryViewModelFactory

        route(SignInScreenRoute(signInViewModelFactory = signInViewModelFactory))
        route(SignUpFlowRoute(signUpViewModelFactory = signUpViewModelFactory))
        route(RecoveryFlowRoute(recoveryViewModelFactory = recoveryViewModelFactory))
    }
)
