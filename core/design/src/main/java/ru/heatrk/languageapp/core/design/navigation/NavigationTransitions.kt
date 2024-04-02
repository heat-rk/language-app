package ru.heatrk.languageapp.core.design.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry

private typealias NavigationTransitionScope = AnimatedContentTransitionScope<NavBackStackEntry>

object NavigationTransitions  {
    val enter: NavigationTransitionScope.() -> EnterTransition =
        { slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth * 2 }) }

    val exit: NavigationTransitionScope.() -> ExitTransition =
        { slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth }) }

    val popEnter: NavigationTransitionScope.() -> EnterTransition =
        { slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }) }

    val popExit: NavigationTransitionScope.() -> ExitTransition =
        { slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth * 2 }) }
}
