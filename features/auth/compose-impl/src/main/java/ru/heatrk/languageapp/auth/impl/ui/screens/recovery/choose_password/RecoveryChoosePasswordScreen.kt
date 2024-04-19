package ru.heatrk.languageapp.auth.impl.ui.screens.recovery.choose_password

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.heatrk.languageapp.auth.impl.R
import ru.heatrk.languageapp.auth.impl.ui.composables.choose_password.ChoosePassword
import ru.heatrk.languageapp.auth.impl.ui.composables.choose_password.ChoosePasswordContract
import ru.heatrk.languageapp.auth.impl.ui.screens.recovery.RecoveryButtonsControllerEffect
import ru.heatrk.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowContract.Intent
import ru.heatrk.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowContract.State
import ru.heatrk.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowViewModel
import ru.heatrk.languageapp.common.utils.states.ProcessingState
import ru.heatrk.languageapp.core.design.composables.AppRootContainer
import ru.heatrk.languageapp.core.design.composables.ShimmerBox
import ru.heatrk.languageapp.core.design.styles.AppTheme
import ru.heatrk.languageapp.core.design.utils.COMPOSE_LARGE_DEVICE_SPEC
import ru.heatrk.languageapp.core.design.utils.smallDeviceMaxWidth

@Composable
fun RecoveryChoosePasswordScreen(viewModel: RecoveryFlowViewModel) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    RecoveryChoosePasswordScreen(
        state = state,
        onIntent = viewModel::processIntent
    )
}

@Composable
internal fun RecoveryChoosePasswordScreen(
    state: State,
    onIntent: (Intent) -> Unit,
) {
    RecoveryButtonsControllerEffect(
        text = stringResource(R.string.confirm_password),
        onClick = { onIntent(Intent.OnNewPasswordConfirmButtonClick) }
    )

    if (state.isRecoveryCodeHandled) {
        RecoveryChoosePasswordContent(
            state = state,
            onIntent = onIntent,
        )
    } else {
        RecoveryChoosePasswordShimmer()
    }
}

@Composable
internal fun RecoveryChoosePasswordShimmer() {
    Column(
        modifier = Modifier
            .smallDeviceMaxWidth()
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 24.dp)
    ) {
        ShimmerBox(
            modifier = Modifier
                .size(width = 100.dp, height = 22.dp)
                .clip(AppTheme.shapes.small)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        ShimmerBox(
            modifier = Modifier
                .size(width = 80.dp, height = 16.dp)
                .clip(AppTheme.shapes.small)
        )

        Spacer(modifier = Modifier.height(8.dp))

        ShimmerBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(AppTheme.shapes.small)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        ShimmerBox(
            modifier = Modifier
                .size(width = 120.dp, height = 16.dp)
                .clip(AppTheme.shapes.small)
        )

        Spacer(modifier = Modifier.height(8.dp))

        ShimmerBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(AppTheme.shapes.small)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
internal fun RecoveryChoosePasswordContent(
    state: State,
    onIntent: (Intent) -> Unit,
) {
    Column(
        modifier = Modifier
            .smallDeviceMaxWidth()
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 24.dp)
    ) {
        ChoosePassword(
            state = ChoosePasswordContract.State(
                password = state.password,
                confirmedPassword = state.confirmedPassword,
                passwordErrorMessage = state.passwordErrorMessage,
                confirmedPasswordErrorMessage = state.confirmedPasswordErrorMessage,
                isPasswordVisible = state.isPasswordVisible,
                isConfirmedPasswordVisible = state.isConfirmedPasswordVisible,
                isEnabled = state.recoveringState == ProcessingState.None,
            ),
            onIntent = { intent ->
                when (intent) {
                    is ChoosePasswordContract.Intent.OnPasswordChanged ->
                        onIntent(Intent.OnPasswordChanged(intent.text))
                    is ChoosePasswordContract.Intent.OnConfirmedPasswordChanged ->
                        onIntent(Intent.OnConfirmedPasswordChanged(intent.text))
                    ChoosePasswordContract.Intent.OnPasswordVisibilityToggleClick ->
                        onIntent(Intent.OnPasswordVisibilityToggleClick)
                    ChoosePasswordContract.Intent.OnConfirmedPasswordVisibilityToggleClick ->
                        onIntent(Intent.OnConfirmedPasswordVisibilityToggleClick)
                    ChoosePasswordContract.Intent.OnKeyboardDoneAction ->
                        onIntent(Intent.OnNewPasswordConfirmButtonClick)
                }
            }
        )
    }
}

private class RecoveryStateProvider : PreviewParameterProvider<State> {
    override val values = sequenceOf(
        State(),
        State(isRecoveryCodeHandled = true),
    )
}

@Composable
private fun RecoveryChoosePasswordScreenPreview(
    state: State
) {
    AppRootContainer { _, _ ->
        RecoveryChoosePasswordScreen(
            state = state,
            onIntent = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun RecoveryChoosePasswordScreenPreviewLight(
    @PreviewParameter(RecoveryStateProvider::class) state: State
) {
    RecoveryChoosePasswordScreenPreview(state = state)
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun RecoveryChoosePasswordScreenPreviewDark(
    @PreviewParameter(RecoveryStateProvider::class) state: State
) {
    RecoveryChoosePasswordScreenPreview(state = state)
}

@Composable
@Preview(
    showBackground = true,
    device = COMPOSE_LARGE_DEVICE_SPEC,
)
private fun RecoveryChoosePasswordScreenPreviewLightLarge(
    @PreviewParameter(RecoveryStateProvider::class) state: State
) {
    RecoveryChoosePasswordScreenPreview(state = state)
}

@Composable
@Preview(
    showBackground = true,
    device = COMPOSE_LARGE_DEVICE_SPEC,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
private fun RecoveryChoosePasswordScreenPreviewDarkLarge(
    @PreviewParameter(RecoveryStateProvider::class) state: State
) {
    RecoveryChoosePasswordScreenPreview(state = state)
}
