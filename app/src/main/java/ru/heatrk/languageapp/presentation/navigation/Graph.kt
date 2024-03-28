package ru.heatrk.languageapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.heatrk.languageapp.auth.impl.ui.navigation.AuthGraphRoute
import ru.heatrk.languageapp.core.navigation.api.NavHost
import ru.heatrk.languageapp.core.navigation.api.route
import ru.heatrk.languageapp.features.splash.impl.ui.navigation.SplashScreenRoute
import ru.heatrk.languageapp.main.impl.ui.navigation.MainGraphRoute
import ru.heatrk.languageapp.onboarding.impl.ui.navigation.OnboardingScreenRoute

private fun NavGraphBuilder.buildGraph() {
    route(SplashScreenRoute)
    route(OnboardingScreenRoute)
    route(AuthGraphRoute)
    route(MainGraphRoute)
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = SplashScreenRoute,
        builder = NavGraphBuilder::buildGraph,
        modifier = modifier,
    )
}
