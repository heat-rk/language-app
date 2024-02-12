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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.heatrk.languageapp.core.design.styles.AppTheme
import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.core.navigation.api.RoutingAction
import ru.heatrk.languageapp.di.AppComponent
import ru.heatrk.languageapp.presentation.navigation.AppNavHost
import ru.heatrk.languageapp.presentation.navigation.composeRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setContent {
            AppTheme { isDarkTheme ->
                val navController = rememberNavController()

                SystemBarsThemeEffect(isDarkTheme = isDarkTheme)
                LaunchedRouterEffect(navController = navController)

                AppNavHost(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background),
                )
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
    private fun SystemBarsThemeEffect(isDarkTheme: Boolean) {
        val statusBarColor = AppTheme.colors.primary.toArgb()
        val navigationBarColor = AppTheme.colors.background.toArgb()

        LaunchedEffect(isDarkTheme) {
            if (isDarkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.dark(
                        scrim = statusBarColor
                    ),
                    navigationBarStyle = SystemBarStyle.dark(
                        scrim = navigationBarColor
                    ),
                )
            } else {
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