package ru.heatrk.languageapp.profile.impl.ui.screens.avatar_crop

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.heatrk.languageapp.common.utils.extract
import ru.heatrk.languageapp.common.utils.painterRes
import ru.heatrk.languageapp.core.design.composables.AppBar
import ru.heatrk.languageapp.core.design.composables.AppRootContainer
import ru.heatrk.languageapp.core.design.composables.button.AppButton
import ru.heatrk.languageapp.core.design.composables.button.toButtonState
import ru.heatrk.languageapp.core.design.composables.scaffold.AppBarState
import ru.heatrk.languageapp.core.design.composables.scaffold.AppScaffoldControllerEffect
import ru.heatrk.languageapp.core.design.composables.scaffold.AppSystemBarsColors
import ru.heatrk.languageapp.core.design.composables.scaffold.LocalAppScaffoldController
import ru.heatrk.languageapp.core.design.styles.AppTheme
import ru.heatrk.languageapp.profile.impl.R
import ru.heatrk.languageapp.profile.impl.ui.navigation.AVATAR_CROP_SCREEN_ROUTE_PATH
import ru.heatrk.languageapp.profile.impl.ui.screens.avatar_crop.AvatarCropContract.Intent
import ru.heatrk.languageapp.profile.impl.ui.screens.avatar_crop.AvatarCropContract.SideEffect
import ru.heatrk.languageapp.profile.impl.ui.screens.avatar_crop.AvatarCropContract.State
import ru.heatrk.languageapp.profile.impl.ui.screens.avatar_crop.image_cropper.SquareImageCropper
import ru.heatrk.languageapp.core.design.R as DesignR

@Composable
internal fun AvatarCropScreen(viewModel: AvatarCropViewModel) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val sideEffects = viewModel.container.sideEffectFlow

    ScreenSideEffects(sideEffects = sideEffects)

    AvatarCropScreen(
        state = state,
        onIntent = viewModel::processIntent
    )
}

@Composable
private fun AvatarCropScreen(
    state: State,
    onIntent: (Intent) -> Unit,
) {
    AppScaffoldControllerEffect(
        appBarState = AppBarState.Custom(key = AVATAR_CROP_SCREEN_ROUTE_PATH) {
            AppBar(
                title = stringResource(R.string.avatar_crop_title),
                onGoBackClick = { onIntent(Intent.OnGoBackClick) },
            )
        },
        appSystemBarsColors = AppSystemBarsColors.Default.copy(
            key = AVATAR_CROP_SCREEN_ROUTE_PATH,
            navigationBar = AppTheme.colors.imageCropperBackground
        )
    )

    Box(
        modifier = Modifier
            .background(AppTheme.colors.imageCropperBackground)
            .fillMaxSize()
    ) {
        SquareImageCropper(
            painter = state.avatar.extract(),
            croppingBoxPadding = 48.dp,
            onCropChanged = { offset, size ->
                onIntent(Intent.OnAvatarCropChanged(offset, size))
            },
            modifier = Modifier
                .fillMaxSize()
        )

        Text(
            text = stringResource(R.string.avatar_crop_description),
            style = AppTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            color = AppTheme.colors.onImageCropperBackground,
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.TopCenter),
        )

        AppButton(
            text = stringResource(DesignR.string.choose),
            buttonState = state.savingState.toButtonState(),
            onClick = { onIntent(Intent.OnSaveClick) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun ScreenSideEffects(
    sideEffects: Flow<SideEffect>,
) {
    val context = LocalContext.current
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

@Composable
private fun AvatarCropScreenPreview() {
    AppRootContainer { _, _ ->
        AvatarCropScreen(
            state = State(
                avatar = painterRes(DesignR.drawable.ic_avatar_placeholder)
            ),
            onIntent = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun AvatarCropScreenPreviewLight() {
    AvatarCropScreenPreview()
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun AvatarCropScreenPreviewDark() {
    AvatarCropScreenPreview()
}
