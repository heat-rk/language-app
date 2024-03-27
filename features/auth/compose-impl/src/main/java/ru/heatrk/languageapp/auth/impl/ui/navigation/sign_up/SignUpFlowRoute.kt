package ru.heatrk.languageapp.auth.impl.ui.navigation.sign_up

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import ru.heatrk.languageapp.auth.impl.ui.screens.sign_up.SignUpFlow
import ru.heatrk.languageapp.core.navigation.api.Route

internal class SignUpFlowRoute(
    private val signUpViewModelFactory: ViewModelProvider.Factory
) : Route.Screen(
    path = SIGN_UP_SCREEN_ROUTE_PATH
) {
    @Composable
    override fun AnimatedContentScope.Content(navBackStackEntry: NavBackStackEntry) {
        SignUpFlow(viewModel = viewModel(factory = signUpViewModelFactory))
    }
}

internal const val SIGN_UP_SCREEN_ROUTE_PATH = "sign_up"