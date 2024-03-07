package ru.heatrk.languageapp.auth.impl.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import ru.heatrk.languageapp.auth.api.ui.navigation.SIGN_UP_SCREEN_ROUTE_PATH
import ru.heatrk.languageapp.auth.impl.ui.sign_up.SignUpScreen
import ru.heatrk.languageapp.core.navigation.api.Route

object SignUpScreenRoute : Route() {
    override val path = SIGN_UP_SCREEN_ROUTE_PATH

    @Composable
    override fun AnimatedContentScope.Screen(navBackStackEntry: NavBackStackEntry) {
        SignUpScreen()
    }
}
