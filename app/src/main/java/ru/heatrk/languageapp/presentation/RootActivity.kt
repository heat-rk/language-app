package ru.heatrk.languageapp.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.heatrk.languageapp.core.design.composables.AppRootContainer
import ru.heatrk.languageapp.core.design.styles.AppTheme
import ru.heatrk.languageapp.core.navigation.compose_impl.ComposeRouter
import ru.heatrk.languageapp.di.AppComponent
import ru.heatrk.languageapp.presentation.navigation.AppNavHost

class RootActivity : ComponentActivity() {

    private val viewModel: RootViewModel by viewModels(
        factoryProducer = { AppComponent.getRootViewModelFactory(intent) }
    )

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        viewModel.onNewIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setContent {
            AppRootContainer { isDarkTheme ->
                val isInitializationFinished by viewModel.isInitializationFinished.collectAsStateWithLifecycle()

                val navController = rememberNavController()

                SystemBarsThemeEffect(
                    isDarkTheme = isDarkTheme,
                    forceSplashTheme = !isInitializationFinished
                )

                AppNavHost(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background),
                )

                LaunchedRouterEffect(navController = navController)
            }
        }
    }

    @Composable
    private fun LaunchedRouterEffect(
        navController: NavController,
        router: ComposeRouter = AppComponent.router,
    ) {
        val coroutineScope = rememberCoroutineScope()

        DisposableEffect(navController, router) {
            router.attachNavController(
                navController = navController,
                coroutineScope = coroutineScope
            )

            onDispose { router.detachNavController() }
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
