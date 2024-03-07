package ru.heatrk.languageapp.core.navigation.compose_test

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import ru.heatrk.languageapp.core.navigation.compose_impl.ComposeRouter

@Composable
fun rememberTestComposeRouter(
    navController: NavController
): ComposeRouter {
    val router = remember { ComposeRouter() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(navController, router) {
        router.observeNavigation(
            navController = navController,
            coroutineScope = coroutineScope,
        )
    }

    return router
}
