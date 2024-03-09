package ru.heatrk.languageapp.auth.impl.ui.navigation

import ru.heatrk.languageapp.auth.impl.ui.sign_up.SignUpViewModel
import ru.heatrk.languageapp.core.navigation.api.Route
import ru.heatrk.languageapp.core.navigation.api.route

internal class SignUpGraphRoute(
    private val signUpViewModelProvider: () -> SignUpViewModel,
) : Route.Graph(
    path = SIGN_UP_GRAPH_ROUTE_PATH,
    startDestination = SIGN_UP_GENERAL_INFO_SCREEN_ROUTE_PATH,
    builder = {
        route(SignUpGeneralInfoScreenRoute(signUpViewModelProvider))
        route(SignUpPasswordScreenRoute(signUpViewModelProvider))
        route(SignUpEmailConfirmScreenRoute(signUpViewModelProvider))
    }
)

internal const val SIGN_UP_GRAPH_ROUTE_PATH = "sign_up_graph"
