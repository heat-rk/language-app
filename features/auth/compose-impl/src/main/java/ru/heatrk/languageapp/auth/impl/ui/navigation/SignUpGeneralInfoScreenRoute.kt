package ru.heatrk.languageapp.auth.impl.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import ru.heatrk.languageapp.auth.impl.ui.sign_up.SignUpViewModel
import ru.heatrk.languageapp.auth.impl.ui.sign_up.general_info.SignUpGeneralInfoScreen
import ru.heatrk.languageapp.core.navigation.api.Route

internal class SignUpGeneralInfoScreenRoute(
    private val signUpViewModelProvider: () -> SignUpViewModel,
) : Route.Screen(
    path = SIGN_UP_GENERAL_INFO_SCREEN_ROUTE_PATH
) {
    @Composable
    override fun AnimatedContentScope.Content(navBackStackEntry: NavBackStackEntry) {
        SignUpGeneralInfoScreen(viewModel = signUpViewModelProvider())
    }
}

internal const val SIGN_UP_GENERAL_INFO_SCREEN_ROUTE_PATH = "sign_up_general_info"
