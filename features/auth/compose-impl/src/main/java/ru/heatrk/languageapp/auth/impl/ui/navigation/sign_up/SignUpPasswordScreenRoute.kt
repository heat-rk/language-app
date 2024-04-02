package ru.heatrk.languageapp.auth.impl.ui.navigation.sign_up

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import ru.heatrk.languageapp.auth.impl.ui.screens.sign_up.SignUpViewModel
import ru.heatrk.languageapp.auth.impl.ui.screens.sign_up.password.SignUpPasswordScreen
import ru.heatrk.languageapp.core.navigation.api.Route

internal class SignUpPasswordScreenRoute(
    private val signUpViewModelProvider: () -> SignUpViewModel,
) : Route.Screen(
    path = SIGN_UP_PASSWORD_SCREEN_ROUTE_PATH
) {
    @Composable
    override fun AnimatedContentScope.Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry
    ) {
        SignUpPasswordScreen(viewModel = signUpViewModelProvider())
    }
}

internal const val SIGN_UP_PASSWORD_SCREEN_ROUTE_PATH = "sign_up_password"
