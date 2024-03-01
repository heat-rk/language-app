package ru.heatrk.languageapp.presentation.navigation

import ru.heatrk.languageapp.core.navigation.api.Route
import ru.heatrk.languageapp.features.splash.di.SplashScreenRoute
import ru.heatrk.languageapp.auth.api.ui.navigation.SignInScreenRoute
import ru.heatrk.languageapp.core.navigation.compose_impl.ComposeRoute
import ru.heatrk.languageapp.main.api.MainScreenRoute
import ru.heatrk.languageapp.onboarding.api.ui.navigation.OnboardingScreenRoute

fun composeRoute(route: Route, withArgs: Boolean = true): String = when (route) {
    SplashScreenRoute -> {
        ComposeSplashScreen.Route.withArgsIfNeeded(withArgs)
    }
    OnboardingScreenRoute -> {
        ComposeOnboardingScreen.Route.withArgsIfNeeded(withArgs)
    }
    SignInScreenRoute -> {
        ComposeSignInScreen.Route.withArgsIfNeeded(withArgs)
    }
    MainScreenRoute -> {
        ComposeMainScreen.Route.withArgsIfNeeded(withArgs)
    }
    else -> {
        throw IllegalArgumentException("Unknown $route route. Check compose route handler and navigation graph!")
    }
}

private fun ComposeRoute.withArgsIfNeeded(
    withArgs: Boolean,
    args: Map<String, String> = emptyMap(),
) = if (withArgs) withArgs(args) else notation
