package ru.heatrk.languageapp.core.navigation.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.route(
    route: Route
) {
    when (route) {
        is Route.Graph -> {
            navigation(
                startDestination = route.startDestination.path,
                route = route.path,
                builder = route.builder,
            )
        }
        is Route.Screen -> {
            composable(
                route = route.path,
                arguments = route.namedNavArguments,
                content = { navBackStackEntry ->
                    with(route) {
                        Content(navBackStackEntry)
                    }
                },
            )
        }
    }
}

@Composable
fun NavHost(
    navController: NavHostController,
    startDestination: Route,
    modifier: Modifier = Modifier,
    builder: NavGraphBuilder.() -> Unit,
) {
    androidx.navigation.compose.NavHost(
        navController = navController,
        startDestination = startDestination.path,
        modifier = modifier,
        builder = builder,
    )
}
