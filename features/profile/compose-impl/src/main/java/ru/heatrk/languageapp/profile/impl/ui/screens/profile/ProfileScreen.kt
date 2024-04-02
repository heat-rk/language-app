package ru.heatrk.languageapp.profile.impl.ui.screens.profile

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.heatrk.languageapp.common.utils.painterRes
import ru.heatrk.languageapp.core.design.composables.AppBarTitleGravity
import ru.heatrk.languageapp.core.design.composables.AppRootContainer
import ru.heatrk.languageapp.core.design.composables.button.AppButton
import ru.heatrk.languageapp.core.design.composables.button.AppButtonDefaults
import ru.heatrk.languageapp.core.design.composables.scaffold.AppBarState
import ru.heatrk.languageapp.core.design.composables.scaffold.AppScaffoldControllerEffect
import ru.heatrk.languageapp.core.design.styles.AppTheme
import ru.heatrk.languageapp.core.design.styles.isNightMode
import ru.heatrk.languageapp.profile.impl.R
import ru.heatrk.languageapp.profile.impl.ui.composables.ProfileHeader
import ru.heatrk.languageapp.profile.impl.ui.composables.ProfileHeaderShimmer
import ru.heatrk.languageapp.core.design.R as DesignR
import ru.heatrk.languageapp.profile.impl.ui.screens.profile.ProfileContract.State
import ru.heatrk.languageapp.profile.impl.ui.screens.profile.ProfileContract.Intent

@Composable
internal fun ProfileScreen(viewModel: ProfileViewModel) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    ProfileScreen(
        state = state,
        onIntent = viewModel::processIntent
    )
}

@Composable
private fun ProfileScreen(
    state: State,
    onIntent: (Intent) -> Unit,
) {
    AppScaffoldControllerEffect(
        appBarState = AppBarState.Default(
            title = stringResource(R.string.profile_title),
            titleGravity = AppBarTitleGravity.CENTER,
            onGoBackClick = { onIntent(Intent.OnGoBackClick) },
        )
    )

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (state) {
            is State.Loaded -> {
                ProfileHeader(
                    fullName = state.fullName,
                    avatar = state.avatar,
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            }
            State.Loading -> {
                ProfileHeaderShimmer(
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            }
        }

        ProfileSettingsBlock(
            onIntent = onIntent,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}

@Composable
private fun ProfileSettingsBlock(
    onIntent: (Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .padding(24.dp)
    ) {
        AppButton(
            text = stringResource(
                if (isNightMode()) {
                    R.string.profile_switch_to_light
                } else {
                    R.string.profile_switch_to_dark
                }
            ),
            onClick = { onIntent(Intent.OnSwitchUiModeButtonClick) },
            modifier = Modifier
                .fillMaxWidth()
        )

        AppButton(
            text = stringResource(R.string.profile_change_mother_language),
            onClick = { onIntent(Intent.OnChangeLanguageButtonClick) },
            modifier = Modifier
                .fillMaxWidth()
        )

        AppButton(
            text = stringResource(R.string.profile_change_image),
            onClick = { onIntent(Intent.OnChangeAvatarButtonClick) },
            modifier = Modifier
                .fillMaxWidth()
        )

        AppButton(
            text = stringResource(R.string.profile_change_logout),
            buttonColors = AppButtonDefaults.colors(
                idleColor = AppTheme.colors.error
            ),
            onClick = { onIntent(Intent.OnLogoutButtonClick) },
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

private class ProfileScreenPreviewStateProvider : PreviewParameterProvider<State> {
    override val values = sequenceOf(
        State.Loading,
        State.Loaded(
            fullName = "Ivan Ivanov",
            avatar = painterRes(DesignR.drawable.ic_avatar_placeholder),
        )
    )
}

@Composable
private fun ProfileScreenPreview(state: State) {
    AppRootContainer {
        ProfileScreen(
            state = state,
            onIntent = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ProfileScreenPreviewLight(
    @PreviewParameter(ProfileScreenPreviewStateProvider::class) state: State
) {
    ProfileScreenPreview(state)
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun ProfileScreenPreviewDark(
    @PreviewParameter(ProfileScreenPreviewStateProvider::class) state: State
) {
    ProfileScreenPreview(state)
}
