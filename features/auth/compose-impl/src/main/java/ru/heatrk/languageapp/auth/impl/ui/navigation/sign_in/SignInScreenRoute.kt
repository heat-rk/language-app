package ru.heatrk.languageapp.auth.impl.ui.navigation.sign_in

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import ru.heatrk.languageapp.auth.impl.ui.screens.sign_in.SignInScreen
import ru.heatrk.languageapp.core.navigation.compose_impl.Route

internal class SignInScreenRoute(
    private val signInViewModelFactory: ViewModelProvider.Factory
) : Route.Screen(
    path =  SIGN_IN_SCREEN_ROUTE_PATH
) {
    @Composable
    override fun AnimatedContentScope.Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry
    ) {
        SignInScreen(viewModel = viewModel(factory = signInViewModelFactory))
    }
}

internal const val SIGN_IN_SCREEN_ROUTE_PATH = "sign_in"
