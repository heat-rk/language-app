package ru.heatrk.languageapp.core.navigation.api

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController

sealed class Route(
    val path: String,
) {
    abstract class Screen(
        path: String,
        val namedNavArguments: List<NamedNavArgument> = emptyList(),
    ) : Route(path) {

        val pathWithParams = buildString {
            append(path)

            if (namedNavArguments.isNotEmpty()) {
                append('?')

                namedNavArguments.forEach { param ->
                    append("${param.name}={${param.name}}")
                }
            }
        }

        @Composable
        abstract fun AnimatedContentScope.Content(
            navController: NavController,
            navBackStackEntry: NavBackStackEntry
        )
    }

    abstract class Graph(
        path: String,
        val startDestination: String,
        val builder: NavGraphBuilder.() -> Unit,
    ) : Route(path)
}


