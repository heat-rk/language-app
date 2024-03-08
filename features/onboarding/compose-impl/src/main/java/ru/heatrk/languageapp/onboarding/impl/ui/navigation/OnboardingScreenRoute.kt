package ru.heatrk.languageapp.onboarding.impl.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import ru.heatrk.languageapp.core.navigation.api.Route
import ru.heatrk.languageapp.onboarding.api.ui.navigation.ONBOARDING_SCREEN_ROUTE_PATH
import ru.heatrk.languageapp.onboarding.impl.ui.OnboardingScreen

object OnboardingScreenRoute : Route.Screen(
    path = ONBOARDING_SCREEN_ROUTE_PATH
) {
    @Composable
    override fun AnimatedContentScope.Content(navBackStackEntry: NavBackStackEntry) {
        OnboardingScreen()
    }
}
