package ru.heatrk.languageapp.presentation.navigation

import ru.heatrk.languageapp.core.navigation.api.Route
import ru.heatrk.languageapp.feature.splash.api.SplashScreenRoute

fun composeRoute(route: Route): String = when (route) {
    SplashScreenRoute -> {
        ComposeSplashScreen.Route.withArgs()
    }
    else -> {
        throw IllegalArgumentException("Unknown $route route. Check compose route handler and navigation graph!")
    }
}
