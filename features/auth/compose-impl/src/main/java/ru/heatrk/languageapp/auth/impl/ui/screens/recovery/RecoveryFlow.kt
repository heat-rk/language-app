package ru.heatrk.languageapp.auth.impl.ui.screens.recovery

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.heatrk.languageapp.auth.impl.R
import ru.heatrk.languageapp.auth.impl.di.AuthComponent
import ru.heatrk.languageapp.auth.impl.ui.navigation.recovery.RecoveryGraphRoute
import ru.heatrk.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowContract.Intent
import ru.heatrk.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowContract.SideEffect
import ru.heatrk.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowContract.State
import ru.heatrk.languageapp.common.utils.extract
import ru.heatrk.languageapp.core.design.composables.AppBarTitleGravity
import ru.heatrk.languageapp.core.design.composables.button.AppButton
import ru.heatrk.languageapp.core.design.composables.button.AppButtonState
import ru.heatrk.languageapp.core.design.composables.scaffold.AppBarState
import ru.heatrk.languageapp.core.design.composables.scaffold.AppScaffoldControllerEffect
import ru.heatrk.languageapp.core.design.composables.scaffold.LocalAppScaffoldController
import ru.heatrk.languageapp.core.navigation.api.NavHost
import ru.heatrk.languageapp.core.navigation.compose_impl.ComposeRouter

@Composable
fun RecoveryFlow(viewModel: RecoveryFlowViewModel) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val appBarTitle = stringResource(R.string.password_recovery)
    val navController = rememberNavController()
    val buttonsController = rememberRecoveryButtonsController()
    val coroutineScope = rememberCoroutineScope()

    RecoveryFlowSideEffects(sideEffects = viewModel.container.sideEffectFlow)

    AppScaffoldControllerEffect(
        appBarState = AppBarState.Shown(
            title = appBarTitle,
            titleGravity = AppBarTitleGravity.CENTER,
            onGoBackClick = { viewModel.processIntent(Intent.OnGoBackClick) },
        )
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

    LaunchedEffect(sideEffects, context) {
        sideEffects
            .onEach { sideEffect ->
                when (sideEffect) {
                    is SideEffect.Message -> {
                        handleMessageSideEffect(
                            sideEffect = sideEffect,
                            snackbarHostState = snackbarHostState,
                            context = context,
                        )
                    }

                    is SideEffect.CloseKeyboard -> {
                        keyboardController?.hide()
                    }
                }
            }
            .launchIn(this)
    }
}

private suspend fun handleMessageSideEffect(
    sideEffect: SideEffect.Message,
    snackbarHostState: SnackbarHostState,
    context: Context,
) {
    val message = sideEffect.text.extract(context)
        ?: return

    snackbarHostState.showSnackbar(message)
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

private fun State.Recovering.toButtonState() = when (this) {
    State.Recovering.None -> AppButtonState.Idle
    State.Recovering.InProgress -> AppButtonState.Loading
    State.Recovering.Success -> AppButtonState.Success
    State.Recovering.Error -> AppButtonState.Error
}
