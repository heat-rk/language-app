package ru.heatrk.languageapp.core.navigation.api

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

data class NavGraphBuilder(
    val androidBuilder: NavGraphBuilder,
    val navController: NavController,
)

fun NavGraphBuilder.applyLibraryBuilder(
    navController: NavController,
    builder: ru.heatrk.languageapp.core.navigation.api.NavGraphBuilder.() -> Unit,
) {
    builder(
        NavGraphBuilder(
            androidBuilder = this,
            navController = navController
        )
    )
}
