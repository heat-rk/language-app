package ru.heatrk.languageapp.presentation.navigation

import ru.heatrk.languageapp.core.navigation.api.Route
import ru.heatrk.languageapp.features.splash.di.SplashScreenRoute
import ru.heatrk.languageapp.login.api.LoginScreenRoute
import ru.heatrk.languageapp.onboarding.api.ui.navigation.OnboardingScreenRoute

fun composeRoute(route: Route): String = when (route) {
    SplashScreenRoute -> {
        ComposeSplashScreen.Route.withArgs()
    }
    OnboardingScreenRoute -> {
        ComposeOnboardingScreen.Route.withArgs()
    }
    LoginScreenRoute -> {
        ComposeLoginScreen.Route.withArgs()
    }
    else -> {
        throw IllegalArgumentException("Unknown $route route. Check compose route handler and navigation graph!")
    }
}
