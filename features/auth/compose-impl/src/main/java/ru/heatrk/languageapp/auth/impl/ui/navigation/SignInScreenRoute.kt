package ru.heatrk.languageapp.auth.impl.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import ru.heatrk.languageapp.auth.impl.ui.sign_in.SignInScreen
import ru.heatrk.languageapp.core.navigation.api.Route

internal object SignInScreenRoute : Route.Screen(
    path =  SIGN_IN_SCREEN_ROUTE_PATH
) {
    @Composable
    override fun AnimatedContentScope.Content(navBackStackEntry: NavBackStackEntry) {
        SignInScreen()
    }
}

internal const val SIGN_IN_SCREEN_ROUTE_PATH = "sign_in"
