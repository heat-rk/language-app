package ru.heatrk.languageapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.heatrk.languageapp.core.design.composables.FadeInAnimatedContent
import ru.heatrk.languageapp.core.design.styles.AppTheme
import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.core.navigation.api.RoutingAction
import ru.heatrk.languageapp.di.AppComponent
import ru.heatrk.languageapp.presentation.navigation.AppNavHost
import ru.heatrk.languageapp.presentation.navigation.composeRoute
import ru.heatrk.languageapp.presentation.splash.SplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setContent {
            AppTheme { isDarkTheme ->
                val viewModel: MainViewModel = viewModel()
                val isInitializationFinished by viewModel.isInitializationFinished.collectAsStateWithLifecycle()
                val navController = rememberNavController()

                LaunchedRouterEffect(navController = navController)

                SystemBarsThemeEffect(
                    isDarkTheme = isDarkTheme,
                    forceSplashTheme = !isInitializationFinished
                )

                FadeInAnimatedContent(
                    targetState = isInitializationFinished,
                    label = "SplashScreenAnimation"
                ) { shouldShowApplicationScreens ->
                    if (shouldShowApplicationScreens) {
                        AppNavHost(
                            navController = navController,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(AppTheme.colors.background),
                        )
                    } else {
                        SplashScreen()
                    }
                }
            }
        }
    }

    @Composable
    private fun LaunchedRouterEffect(
        navController: NavController,
        router: Router = AppComponent.router,
    ) {
        LaunchedEffect(navController, router) {
            router.actions
                .onEach { action ->
                    when (action) {
                        RoutingAction.NavigateBack -> {
                            navController.popBackStack()
                        }
                        is RoutingAction.NavigateTo -> {
                            navController.navigate(composeRoute(action.route))
                        }
                    }
                }
                .launchIn(this)
        }
    }

    @Composable
    private fun SystemBarsThemeEffect(
        isDarkTheme: Boolean,
        forceSplashTheme: Boolean
    ) {
        val statusBarColor = AppTheme.colors.primary.toArgb()
        val navigationBarColor = AppTheme.colors.background.toArgb()
        val splashSystemBarsColor = AppTheme.colors.primary.toArgb()

        LaunchedEffect(isDarkTheme, forceSplashTheme) {
            when {
                forceSplashTheme -> {
                    enableEdgeToEdge(
                        statusBarStyle = SystemBarStyle.dark(
                            scrim = splashSystemBarsColor
                        ),
                        navigationBarStyle = SystemBarStyle.dark(
                            scrim = splashSystemBarsColor
                        ),
                    )
                }

                isDarkTheme -> {
                    enableEdgeToEdge(
                        statusBarStyle = SystemBarStyle.dark(
                            scrim = statusBarColor
                        ),
                        navigationBarStyle = SystemBarStyle.dark(
                            scrim = navigationBarColor
                        ),
                    )
                }

                else -> {
                    enableEdgeToEdge(
                        statusBarStyle = SystemBarStyle.dark(
                            scrim = statusBarColor
                        ),
                        navigationBarStyle = SystemBarStyle.dark(
                            scrim = navigationBarColor
                        ),
                    )
                }
            }
        }
    }
}
