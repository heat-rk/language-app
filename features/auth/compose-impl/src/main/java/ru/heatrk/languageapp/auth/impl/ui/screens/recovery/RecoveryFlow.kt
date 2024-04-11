package ru.heatrk.languageapp.auth.impl.ui.screens.recovery

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import ru.heatrk.languageapp.auth.impl.R
import ru.heatrk.languageapp.auth.impl.di.AuthComponent
import ru.heatrk.languageapp.auth.impl.ui.navigation.recovery.RECOVERY_FLOW_ROUTE_PATH
import ru.heatrk.languageapp.auth.impl.ui.navigation.recovery.RecoveryGraphRoute
import ru.heatrk.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowContract.Intent
import ru.heatrk.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowContract.SideEffect
import ru.heatrk.languageapp.common.utils.compose.ScreenSideEffectsFlowHandler
import ru.heatrk.languageapp.common.utils.compose.handleMessageSideEffect
import ru.heatrk.languageapp.core.design.composables.AppBar
import ru.heatrk.languageapp.core.design.composables.AppBarTitleGravity
import ru.heatrk.languageapp.core.design.composables.button.AppButton
import ru.heatrk.languageapp.core.design.composables.button.toButtonState
import ru.heatrk.languageapp.core.design.composables.scaffold.AppBarState
import ru.heatrk.languageapp.core.design.composables.scaffold.AppScaffoldControllerEffect
import ru.heatrk.languageapp.core.design.composables.scaffold.LocalAppScaffoldController
import ru.heatrk.languageapp.core.design.utils.smallDeviceMaxWidth
import ru.heatrk.languageapp.core.navigation.compose_impl.ComposeRouter
import ru.heatrk.languageapp.core.navigation.compose_impl.NavHost

@Composable
fun RecoveryFlow(viewModel: RecoveryFlowViewModel) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val appBarTitle = stringResource(R.string.password_recovery)
    val navController = rememberNavController()
    val buttonsController = rememberRecoveryButtonsController()
    val coroutineScope = rememberCoroutineScope()

    RecoveryFlowSideEffects(sideEffects = viewModel.container.sideEffectFlow)

    AppScaffoldControllerEffect(
        appBarState = AppBarState.Custom(key = RECOVERY_FLOW_ROUTE_PATH) {
            AppBar(
                title = appBarTitle,
                titleGravity = AppBarTitleGravity.CENTER,
                onGoBackClick = { viewModel.processIntent(Intent.OnGoBackClick) },
            )
        }
    )

    Column(
        modifier = Modifier
            .padding(vertical = 40.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.weight(1f))

        CompositionLocalProvider(LocalRecoveryButtonsController provides buttonsController) {
            NavHost(
                navController = navController,
                graph = RecoveryGraphRoute(
                    recoveryViewModelProvider = { viewModel }
                ),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        AppButton(
            text = buttonsController.text,
            buttonState = state.recoveringState.toButtonState(),
            onClick = buttonsController.onClick,
            modifier = Modifier
                .smallDeviceMaxWidth()
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
    }

    RecoveryLaunchedRouterEffect(
        coroutineScope = coroutineScope,
        navController = navController
    )
}

@Composable
private fun RecoveryLaunchedRouterEffect(
    coroutineScope: CoroutineScope,
    navController: NavController,
    router: ComposeRouter = AuthComponent.recoveryRouter,
) {
    DisposableEffect(coroutineScope, navController, router) {
        router.attachNavController(
            navController = navController,
            coroutineScope = coroutineScope
        )

        onDispose { router.detachNavController() }
    }
}

@Composable
private fun RecoveryFlowSideEffects(
    sideEffects: Flow<SideEffect>,
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = LocalAppScaffoldController.current.snackbarHostState

    ScreenSideEffectsFlowHandler(sideEffects = sideEffects) { sideEffect ->
        when (sideEffect) {
            is SideEffect.Message -> {
                handleMessageSideEffect(
                    message = sideEffect.text,
                    snackbarHostState = snackbarHostState,
                    context = context,
                )
            }

            is SideEffect.CloseKeyboard -> {
                keyboardController?.hide()
            }
        }
    }
}

internal class RecoveryButtonsController {
    var text by mutableStateOf("")
    var onClick by mutableStateOf({})
}

internal val LocalRecoveryButtonsController =
    staticCompositionLocalOf { RecoveryButtonsController() }

@Composable
internal fun rememberRecoveryButtonsController() =
    remember { RecoveryButtonsController() }

@Composable
internal fun RecoveryButtonsControllerEffect(
    text: String,
    onClick: () -> Unit,
) {
    val recoveryButtonsController = LocalRecoveryButtonsController.current

    LaunchedEffect(recoveryButtonsController) {
        recoveryButtonsController.text = text
        recoveryButtonsController.onClick = onClick
    }
}
