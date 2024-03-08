package ru.heatrk.languageapp.main.impl.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import ru.heatrk.languageapp.core.navigation.api.Route
import ru.heatrk.languageapp.main.api.MAIN_SCREEN_ROUTE_PATH
import ru.heatrk.languageapp.main.impl.di.MainComponent
import ru.heatrk.languageapp.main.impl.ui.MainScreen

object MainScreenRoute : Route.Screen(
    path = MAIN_SCREEN_ROUTE_PATH
) {
    @Composable
    override fun AnimatedContentScope.Content(navBackStackEntry: NavBackStackEntry) {
        MainScreen(
            viewModel = viewModel(
                factory = MainComponent.mainViewModelFactory
            )
        )
    }
}
