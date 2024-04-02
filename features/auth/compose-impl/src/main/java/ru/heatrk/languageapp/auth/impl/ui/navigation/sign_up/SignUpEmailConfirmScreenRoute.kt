package ru.heatrk.languageapp.auth.impl.ui.navigation.sign_up

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import ru.heatrk.languageapp.auth.impl.ui.screens.sign_up.SignUpViewModel
import ru.heatrk.languageapp.auth.impl.ui.screens.sign_up.email_confirm.SignUpEmailConfirmScreen
import ru.heatrk.languageapp.core.navigation.api.Route

internal class SignUpEmailConfirmScreenRoute(
    private val signUpViewModelProvider: () -> SignUpViewModel,
) : Route.Screen(
    path = SIGN_UP_EMAIL_CONFIRM_SCREEN_ROUTE_PATH
) {
    @Composable
    override fun AnimatedContentScope.Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry
    ) {
        SignUpEmailConfirmScreen(viewModel = signUpViewModelProvider())
    }
}

internal const val SIGN_UP_EMAIL_CONFIRM_SCREEN_ROUTE_PATH = "sign_up_email_confirm"
