package ru.heatrk.languageapp.presentation.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.heatrk.languageapp.core.navigation.compose_impl.ComposeRoute
import ru.heatrk.languageapp.core.navigation.compose_impl.NavHost
import ru.heatrk.languageapp.core.navigation.compose_impl.composable
import ru.heatrk.languageapp.feature.splash.impl.ui.SplashScreen

object ComposeSplashScreen {
    object Route : ComposeRoute() {
        @Composable
        override fun AnimatedContentScope.Content(navBackStackEntry: NavBackStackEntry) {
            SplashScreen()
        }
    }
}

private fun NavGraphBuilder.buildGraph() {
    composable(ComposeSplashScreen.Route)
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = ComposeSplashScreen.Route,
        builder = NavGraphBuilder::buildGraph,
        modifier = modifier,
    )
}
