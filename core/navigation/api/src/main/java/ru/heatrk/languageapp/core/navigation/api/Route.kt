package ru.heatrk.languageapp.core.navigation.api

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder

sealed class Route(
    val path: String,
    val namedNavArguments: List<NamedNavArgument> = emptyList(),
) {
    fun pathWithArgs(args: Map<String, String> = emptyMap()): String =
        buildString {
            append(path)

            if (args.isNotEmpty()) {
                append('?')
                append(args.toQueryArgs())
            }
        }

    private fun Map<String, String>.toQueryArgs() =
        entries.joinToString(separator = "&") { "${it.key}=${it.value}" }

    abstract class Screen(
        path: String,
        namedNavArguments: List<NamedNavArgument> = emptyList(),
    ) : Route(path, namedNavArguments) {
        @Composable
        abstract fun AnimatedContentScope.Content(navBackStackEntry: NavBackStackEntry)
    }

    abstract class Graph(
        path: String,
        namedNavArguments: List<NamedNavArgument> = emptyList(),
        val startDestination: Screen,
        val builder: NavGraphBuilder.() -> Unit,
    ) : Route(path, namedNavArguments)
}


