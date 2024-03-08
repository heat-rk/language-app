package ru.heatrk.languageapp.auth.impl.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import ru.heatrk.languageapp.auth.impl.ui.sign_up.SignUpScreen
import ru.heatrk.languageapp.core.navigation.api.Route

internal class SignUpScreenRoute(
    private val signUpViewModelFactory: ViewModelProvider.Factory
) : Route.Screen(
    path = SIGN_UP_SCREEN_ROUTE_PATH
) {
    @Composable
    override fun AnimatedContentScope.Content(navBackStackEntry: NavBackStackEntry) {
        SignUpScreen(viewModel = viewModel(factory = signUpViewModelFactory))
    }
}

internal const val SIGN_UP_SCREEN_ROUTE_PATH = "sign_up"
