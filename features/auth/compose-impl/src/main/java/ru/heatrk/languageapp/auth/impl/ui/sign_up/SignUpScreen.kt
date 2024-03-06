package ru.heatrk.languageapp.auth.impl.ui.sign_up

import android.content.Context
import android.content.res.Configuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.heatrk.languageapp.auth.impl.R
import ru.heatrk.languageapp.auth.impl.di.AuthComponent
import ru.heatrk.languageapp.auth.impl.ui.sign_up.SignUpScreenContract.Intent
import ru.heatrk.languageapp.auth.impl.ui.sign_up.SignUpScreenContract.SideEffect
import ru.heatrk.languageapp.auth.impl.ui.sign_up.SignUpScreenContract.State
import ru.heatrk.languageapp.common.utils.extract
import ru.heatrk.languageapp.core.design.composables.AppBarTitleGravity
import ru.heatrk.languageapp.core.design.composables.AppRootContainer
import ru.heatrk.languageapp.core.design.composables.animation.RightToLeftAnimatedContent
import ru.heatrk.languageapp.core.design.composables.scaffold.AppBarState
import ru.heatrk.languageapp.core.design.composables.scaffold.AppScaffoldControllerEffect
import ru.heatrk.languageapp.core.design.composables.scaffold.LocalAppScaffoldController

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = viewModel(
        factory = AuthComponent.signUpViewModelFactory
    )
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    SignUpScreenSideEffects(
        sideEffects = viewModel.container.sideEffectFlow
    )

    SignUpScreen(
        state = state,
        onIntent = viewModel::processIntent,
    )
}

@Composable
private fun SignUpScreen(
    state: State,
    onIntent: (Intent) -> Unit,
) {
    val appBarTitle = stringResource(R.string.signup)

    AppScaffoldControllerEffect(
        appBarState = AppBarState.Shown(
            title = appBarTitle,
            titleGravity = AppBarTitleGravity.CENTER,
            onGoBackClick = { onIntent(Intent.OnGoBackClick) },
        )
    )

    RightToLeftAnimatedContent(
        targetState = state,
        label = "SignUpStateAnimation",
        contentKey = { animatedState -> animatedState::class }
    ) { animatedState ->
        when (animatedState) {
            is State.InputData -> {
                SignUpScreenInputData(
                    state = animatedState,
                    onIntent = onIntent
                )
            }
            State.EmailConfirmation -> {
                SignUpScreenEmailConfirmation(
                    onIntent = onIntent
                )
            }
        }
    }
}



@Composable
private fun SignUpScreenSideEffects(
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

class SignUpStatePreviewParameterProvider : PreviewParameterProvider<State> {
    override val values = sequenceOf(
        State.InputData(inputDataState = State.InputData.State.GENERAL),
        State.InputData(inputDataState = State.InputData.State.PASSWORD),
        State.EmailConfirmation,
    )
}

@Composable
private fun SignUpScreenPreview(
    state: State
) {
    AppRootContainer {
        SignUpScreen(
            state = state,
            onIntent = {}
        )
    }
}

@Composable
@Preview(
    showBackground = true,
)
private fun SignUpScreenPreviewLight(
    @PreviewParameter(SignUpStatePreviewParameterProvider::class)
    state: State
) {
    SignUpScreenPreview(state = state)
}

@Composable
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
private fun SignUpScreenPreviewDark(
    @PreviewParameter(SignUpStatePreviewParameterProvider::class)
    state: State
) {
    SignUpScreenPreview(state = state)
}
