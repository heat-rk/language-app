package ru.heatrk.languageapp.main.impl.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import ru.heatrk.languageapp.core.navigation.compose_impl.Route
import ru.heatrk.languageapp.main.api.ui.navigation.MAIN_SCREEN_ROUTE_PATH
import ru.heatrk.languageapp.main.impl.di.MainComponent
import ru.heatrk.languageapp.main.impl.ui.screens.main.MainScreen

object MainScreenRoute : Route.Screen(
    path = MAIN_SCREEN_ROUTE_PATH
) {
    @Composable
    override fun AnimatedContentScope.Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry
    ) {
        MainScreen(
            viewModel = viewModel(
                factory = MainComponent.mainViewModelFactory
            )
        )
    }
}
