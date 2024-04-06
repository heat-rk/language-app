package ru.heatrk.languageapp.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import ru.heatrk.languageapp.common.utils.initializeViewModel
import ru.heatrk.languageapp.core.design.composables.AppRootContainer
import ru.heatrk.languageapp.core.design.styles.AppTheme
import ru.heatrk.languageapp.core.design.styles.AppUiMode
import ru.heatrk.languageapp.core.design.styles.LocalAppUiMode
import ru.heatrk.languageapp.core.navigation.compose_impl.ComposeRouter
import ru.heatrk.languageapp.di.AppComponent
import ru.heatrk.languageapp.presentation.navigation.AppNavHost
import ru.heatrk.languageapp.profile.api.domain.ForcedTheme

class RootActivity : ComponentActivity() {

    private val initializationViewModel: InitializationViewModel by viewModels(
        factoryProducer = { AppComponent.getRootViewModelFactory(intent) }
    )

    private val themeViewModel: ThemeViewModel by viewModels(
        factoryProducer = { AppComponent.getThemeViewModelFactory() }
    )

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        initializationViewModel.onNewIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        initPassiveViewModels()

        setContent {
            val forcedTheme by themeViewModel.forcedTheme.collectAsStateWithLifecycle()
            val isSystemInDarkTheme = isSystemInDarkTheme()

            val appUiMode = remember(forcedTheme) {
                when {
                    forcedTheme == ForcedTheme.DARK -> AppUiMode.DARK
                    forcedTheme == ForcedTheme.LIGHT -> AppUiMode.LIGHT
                    isSystemInDarkTheme -> AppUiMode.DARK
                    else -> AppUiMode.LIGHT
                }
            }

            CompositionLocalProvider(LocalAppUiMode provides appUiMode) {
                AppRootContainer { isDarkTheme, systemBarsColors ->
                    val isInitializationFinished by initializationViewModel
                        .isInitializationFinished
                        .collectAsStateWithLifecycle()

                    val navController = rememberNavController()

                    SystemBarsThemeEffect(
                        isDarkTheme = isDarkTheme,
                        forceSplashTheme = !isInitializationFinished,
                        statusBarColor = systemBarsColors.statusBar(),
                        navigationBarColor = systemBarsColors.navigationBar(),
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
    }

    private fun initPassiveViewModels() {
        initializeViewModel<AuthEventListenerViewModel>(
            factoryProducer = { AppComponent.getAuthEventListenerViewModelFactory() }
        )
    }

    @Composable
    private fun LaunchedRouterEffect(
        navController: NavController,
        router: ComposeRouter = AppComponent.router,
    ) {
        val coroutineScope = rememberCoroutineScope()

        BackHandler(enabled = router.previousRoute != null) {
            if (!router.onRoutingBackReceived()) {
                coroutineScope.launch { router.navigateBack() }
            }
        }

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
        forceSplashTheme: Boolean,
        statusBarColor: Color,
        navigationBarColor: Color,
    ) {
        val statusBarColorArgb = statusBarColor.toArgb()
        val navigationBarColorArgb = navigationBarColor.toArgb()
        val splashSystemBarsColorArgb = AppTheme.colors.primary.toArgb()

        LaunchedEffect(
            isDarkTheme,
            forceSplashTheme,
            statusBarColorArgb,
            navigationBarColorArgb,
        ) {
            when {
                forceSplashTheme -> {
                    enableEdgeToEdge(
                        statusBarStyle = SystemBarStyle.dark(
                            scrim = splashSystemBarsColorArgb
                        ),
                        navigationBarStyle = SystemBarStyle.dark(
                            scrim = splashSystemBarsColorArgb
                        ),
                    )
                }

                isDarkTheme -> {
                    enableEdgeToEdge(
                        statusBarStyle = SystemBarStyle.dark(
                            scrim = statusBarColorArgb
                        ),
                        navigationBarStyle = SystemBarStyle.dark(
                            scrim = navigationBarColorArgb
                        ),
                    )
                }

                else -> {
                    enableEdgeToEdge(
                        statusBarStyle = SystemBarStyle.dark(
                            scrim = statusBarColorArgb
                        ),
                        navigationBarStyle = SystemBarStyle.dark(
                            scrim = navigationBarColorArgb
                        ),
                    )
                }
            }
        }
    }
}
