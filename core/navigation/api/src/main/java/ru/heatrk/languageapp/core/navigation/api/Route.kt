package ru.heatrk.languageapp.core.navigation.api

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry

abstract class Route {
    abstract val path: String

    open val namedNavArguments: List<NamedNavArgument> get() = emptyList()

    @Composable
    abstract fun AnimatedContentScope.Screen(navBackStackEntry: NavBackStackEntry)

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
}


