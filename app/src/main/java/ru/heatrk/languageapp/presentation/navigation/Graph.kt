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
import ru.heatrk.languageapp.features.splash.ui.SplashScreen
import ru.heatrk.languageapp.auth.impl.ui.sign_in.SignInScreen
import ru.heatrk.languageapp.auth.impl.ui.sign_up.SignUpScreen
import ru.heatrk.languageapp.main.impl.ui.MainScreen
import ru.heatrk.languageapp.onboarding.impl.ui.OnboardingScreen

object ComposeSplashScreen {
    object Route : ComposeRoute("splash") {
        @Composable
        override fun AnimatedContentScope.Content(navBackStackEntry: NavBackStackEntry) {
            SplashScreen()
        }
    }
}

object ComposeOnboardingScreen {
    object Route : ComposeRoute("onboarding") {
        @Composable
        override fun AnimatedContentScope.Content(navBackStackEntry: NavBackStackEntry) {
            OnboardingScreen()
        }
    }
}

object ComposeSignInScreen {
    object Route : ComposeRoute("sign_in") {
        @Composable
        override fun AnimatedContentScope.Content(navBackStackEntry: NavBackStackEntry) {
            SignInScreen()
        }
    }
}

object ComposeSignUpScreen {
    object Route : ComposeRoute("sign_up") {
        @Composable
        override fun AnimatedContentScope.Content(navBackStackEntry: NavBackStackEntry) {
            SignUpScreen()
        }
    }
}

object ComposeMainScreen {
    object Route : ComposeRoute("main") {
        @Composable
        override fun AnimatedContentScope.Content(navBackStackEntry: NavBackStackEntry) {
            MainScreen()
        }
    }
}

private fun NavGraphBuilder.buildGraph() {
    composable(ComposeSplashScreen.Route)
    composable(ComposeOnboardingScreen.Route)
    composable(ComposeSignInScreen.Route)
    composable(ComposeSignUpScreen.Route)
    composable(ComposeMainScreen.Route)
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
