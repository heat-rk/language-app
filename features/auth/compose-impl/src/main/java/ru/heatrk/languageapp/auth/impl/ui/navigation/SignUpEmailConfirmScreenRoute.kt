package ru.heatrk.languageapp.auth.impl.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import ru.heatrk.languageapp.auth.impl.ui.sign_up.SignUpViewModel
import ru.heatrk.languageapp.auth.impl.ui.sign_up.email_confirm.SignUpEmailConfirmScreen
import ru.heatrk.languageapp.core.navigation.api.Route

internal class SignUpEmailConfirmScreenRoute(
    private val signUpViewModelProvider: () -> SignUpViewModel,
) : Route.Screen(
    path = SIGN_UP_EMAIL_CONFIRM_SCREEN_ROUTE_PATH
) {
    @Composable
    override fun AnimatedContentScope.Content(navBackStackEntry: NavBackStackEntry) {
        SignUpEmailConfirmScreen(viewModel = signUpViewModelProvider())
    }
}

internal const val SIGN_UP_EMAIL_CONFIRM_SCREEN_ROUTE_PATH = "sign_up_email_confirm"
