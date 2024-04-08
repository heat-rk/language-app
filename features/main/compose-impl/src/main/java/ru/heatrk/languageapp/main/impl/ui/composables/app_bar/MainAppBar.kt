package ru.heatrk.languageapp.main.impl.ui.composables.app_bar

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ru.heatrk.languageapp.common.utils.PainterResource
import ru.heatrk.languageapp.common.utils.painterRes
import ru.heatrk.languageapp.core.design.composables.AppPainterWrapper
import ru.heatrk.languageapp.core.design.composables.animation.FadeInAnimatedContent
import ru.heatrk.languageapp.core.design.composables.shimmerEffect
import ru.heatrk.languageapp.core.design.styles.AppTheme
import ru.heatrk.languageapp.main.impl.R
import ru.heatrk.languageapp.core.design.R as DesignR

@Composable
fun MainAppBar(
    state: MainAppBarState,
    modifier: Modifier = Modifier,
    onAvatarClick: () -> Unit = {},
    scrollingBehaviour: MainAppBarNestedScrollConnection =
        MainAppBarDefaultNestedScrollConnection(LocalDensity.current),
) {
    when (state) {
        MainAppBarState.Loading ->
            MainAppBarLoading(
                scrollingBehaviour = scrollingBehaviour,
                modifier = modifier,
            )
        is MainAppBarState.Ok ->
            MainAppBarOk(
                state = state,
                onAvatarClick = onAvatarClick,
                scrollingBehaviour = scrollingBehaviour,
                modifier = modifier,
            )
    }
}

@Composable
private fun MainAppBarOk(
    state: MainAppBarState.Ok,
    onAvatarClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrollingBehaviour: MainAppBarNestedScrollConnection,
) {
    MainAppBarLayout(
        avatarContent = {
            AppPainterWrapper(
                painterResource = state.avatar
                    ?: painterRes(DesignR.drawable.ic_avatar_placeholder),
                loadingContent = {
                    Box(
                        modifier = Modifier
                            .requiredSize(AvatarSize)
                            .clip(CircleShape)
                            .shimmerEffect(
                                shimmerBackgroundColor = ShimmerBackgroundColor,
                                shimmerForegroundColor = ShimmerForegroundColor,
                            )
                    )
                },
                successContent = { painter ->
                    Image(
                        painter = painter,
                        contentDescription = stringResource(DesignR.string.accessibility_go_to_profile),
                        modifier = Modifier
                            .size(AvatarSize)
                            .clip(CircleShape)
                            .clickable(onClick = onAvatarClick)
                    )
                }
            )
        },
        titleContent = {
            Text(
                text = if (state.firstName.isNullOrBlank()) {
                    stringResource(R.string.main_app_bar_title)
                } else {
                    stringResource(R.string.main_app_bar_title_named, state.firstName)
                },
                color = AppTheme.colors.onPrimary,
                style = AppTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium
            )
        },
        descriptionContent = {
            Text(
                text = stringResource(R.string.main_app_bar_description),
                color = AppTheme.colors.textBody,
                style = AppTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
        },
        scrollingBehaviour = scrollingBehaviour,
        modifier = modifier,
    )
}

@Composable
private fun MainAppBarLoading(
    modifier: Modifier = Modifier,
    scrollingBehaviour: MainAppBarNestedScrollConnection,
) {
    MainAppBarLayout(
        avatarContent = {
            Box(
                modifier = Modifier
                    .requiredSize(AvatarSize)
                    .clip(CircleShape)
                    .shimmerEffect(
                        shimmerBackgroundColor = ShimmerBackgroundColor,
                        shimmerForegroundColor = ShimmerForegroundColor,
                    )
            )
        },
        titleContent = {
            Box(
                modifier = Modifier
                    .requiredSize(width = 100.dp, height = 22.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .shimmerEffect(
                        shimmerBackgroundColor = ShimmerBackgroundColor,
                        shimmerForegroundColor = ShimmerForegroundColor,
                    )
            )
        },
        descriptionContent = {
            Box(
                modifier = Modifier
                    .requiredSize(width = 200.dp, height = 17.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect(
                        shimmerBackgroundColor = ShimmerBackgroundColor,
                        shimmerForegroundColor = ShimmerForegroundColor,
                    )
            )
        },
        scrollingBehaviour = scrollingBehaviour,
        modifier = modifier,
    )
}

@Composable
private fun MainAppBarLayout(
    scrollingBehaviour: MainAppBarNestedScrollConnection,
    avatarContent: @Composable () -> Unit,
    titleContent: @Composable () -> Unit,
    descriptionContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollProgress by scrollingBehaviour.getAppBarProgressState()

    val appBarHeight by remember {
        derivedStateOf {
            MainAppBarCollapsedHeight + (MainAppBarExpandedHeight - MainAppBarCollapsedHeight) * scrollProgress
        }
    }

    Box(
        contentAlignment = Alignment.BottomStart,
        modifier = modifier
            .fillMaxWidth()
            .height(appBarHeight)
            .background(AppTheme.colors.primary)
            .padding(
                horizontal = 24.dp,
                vertical = 10.dp,
            )
    ) {
        FadeInAnimatedContent(
            label = "AppBarHeightAnimation",
            targetState = if (scrollProgress > 0.7) {
                MainAppBarHeightState.Expanded
            } else {
                MainAppBarHeightState.Collapsed
            }
        ) { heightState ->
            when (heightState) {
                MainAppBarHeightState.Expanded -> {
                    MainAppBarExpandedContent(
                        avatarContent = avatarContent,
                        titleContent = titleContent,
                        descriptionContent = descriptionContent
                    )
                }
                MainAppBarHeightState.Collapsed -> {
                    MainAppBarCollapsedContent(
                        avatarContent = avatarContent,
                        titleContent = titleContent
                    )
                }
            }
        }
    }
}

@Composable
private fun MainAppBarExpandedContent(
    avatarContent: @Composable () -> Unit,
    titleContent: @Composable () -> Unit,
    descriptionContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(AppTheme.colors.primary)
    ) {
        avatarContent()

        Spacer(modifier = Modifier.height(5.dp))

        titleContent()

        Spacer(modifier = Modifier.height(5.dp))

        descriptionContent()
    }
}

@Composable
private fun MainAppBarCollapsedContent(
    avatarContent: @Composable () -> Unit,
    titleContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(AppTheme.colors.primary)
    ) {
        avatarContent()

        Spacer(modifier = Modifier.width(16.dp))

        titleContent()
    }
}

private enum class MainAppBarHeightState {
    Expanded,
    Collapsed;
}

sealed interface MainAppBarState {
    data object Loading : MainAppBarState

    data class Ok(
        val firstName: String?,
        val avatar: PainterResource?
    ) : MainAppBarState
}

val ShimmerBackgroundColor
    @Composable
    get() = AppTheme.colors.shimmerBackground.copy(alpha = 0.5f)

val ShimmerForegroundColor
    @Composable
    get() = AppTheme.colors.shimmerForeground

val MainAppBarCollapsedHeight = 102.dp
val MainAppBarExpandedHeight = 175.dp
val AvatarSize = 54.dp

private class MainAppBarPreviewStateProvider : PreviewParameterProvider<MainAppBarState> {
    override val values = sequenceOf(
        MainAppBarState.Loading,
        MainAppBarState.Ok(
            firstName = "Emil",
            avatar = painterRes(DesignR.drawable.ic_avatar_placeholder),
        )
    )
}

@Composable
private fun MainAppBarPreview(
    state: MainAppBarState
) {
    AppTheme {
        MainAppBar(state = state)
    }
}

@Composable
@Preview(showBackground = true)
private fun MainAppBarPreviewLight(
    @PreviewParameter(MainAppBarPreviewStateProvider::class) state: MainAppBarState
) {
    MainAppBarPreview(state = state)
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun MainAppBarPreviewDark(
    @PreviewParameter(MainAppBarPreviewStateProvider::class) state: MainAppBarState
) {
    MainAppBarPreview(state = state)
}
