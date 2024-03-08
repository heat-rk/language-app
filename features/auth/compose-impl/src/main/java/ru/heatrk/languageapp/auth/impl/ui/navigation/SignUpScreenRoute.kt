package ru.heatrk.languageapp.auth.impl.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import ru.heatrk.languageapp.auth.impl.ui.sign_up.SignUpScreen
import ru.heatrk.languageapp.core.navigation.api.Route

internal object SignUpScreenRoute : Route.Screen(
    path = SIGN_UP_SCREEN_ROUTE_PATH
) {
    @Composable
    override fun AnimatedContentScope.Content(navBackStackEntry: NavBackStackEntry) {
        SignUpScreen()
    }
}

internal const val SIGN_UP_SCREEN_ROUTE_PATH = "sign_up"
