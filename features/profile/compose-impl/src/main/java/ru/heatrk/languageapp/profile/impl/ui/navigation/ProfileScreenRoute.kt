package ru.heatrk.languageapp.profile.impl.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import ru.heatrk.languageapp.core.navigation.api.Route
import ru.heatrk.languageapp.core.navigation.api.sharedViewModel
import ru.heatrk.languageapp.profile.impl.di.ProfileComponent
import ru.heatrk.languageapp.profile.impl.ui.screens.profile.ProfileScreen

internal object ProfileScreenRoute : Route.Screen(
    path = PROFILE_SCREEN_ROUTE
) {
    @Composable
    override fun AnimatedContentScope.Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry
    ) {
        ProfileScreen(
            viewModel = navBackStackEntry.sharedViewModel(
                navController = navController,
                factory = ProfileComponent.profileViewModelFactory,
            )
        )
    }
}

internal const val PROFILE_SCREEN_ROUTE = "profile"
