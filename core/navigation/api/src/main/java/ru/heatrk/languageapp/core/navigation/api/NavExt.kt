package ru.heatrk.languageapp.core.navigation.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.route(route: Route) {
    when (route) {
        is Route.Graph -> {
            androidBuilder.navigation(
                startDestination = route.startDestination,
                route = route.path,
                builder = { applyLibraryBuilder(navController, route.builder) },
            )
        }
        is Route.Screen -> {
            androidBuilder.composable(
                route = route.pathWithParams,
                arguments = route.namedNavArguments,
                content = { navBackStackEntry ->
                    with(route) {
                        Content(navController, navBackStackEntry)
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
        builder = { applyLibraryBuilder(navController, builder) },
    )
}

@Composable
fun NavHost(
    navController: NavHostController,
    graph: Route.Graph,
    modifier: Modifier = Modifier,
) {
    androidx.navigation.compose.NavHost(
        navController = navController,
        graph = androidx.navigation.NavGraphBuilder(
            provider = navController.navigatorProvider,
            route = graph.path,
            startDestination = graph.startDestination,
        ).apply { applyLibraryBuilder(navController, graph.builder) }.build(),
        modifier = modifier,
    )
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController,
    factory: ViewModelProvider.Factory? = null,
): T {
    val navGraphRoute = destination.parent?.route
        ?: return viewModel(factory = factory)

    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }

    return viewModel(viewModelStoreOwner = parentEntry, factory = factory)
}
