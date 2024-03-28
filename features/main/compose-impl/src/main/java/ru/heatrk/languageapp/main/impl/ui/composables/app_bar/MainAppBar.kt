package ru.heatrk.languageapp.main.impl.ui.composables.app_bar

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ru.heatrk.languageapp.common.utils.PainterResource
import ru.heatrk.languageapp.common.utils.extract
import ru.heatrk.languageapp.common.utils.painterRes
import ru.heatrk.languageapp.core.design.composables.shimmerEffect
import ru.heatrk.languageapp.core.design.styles.AppTheme
import ru.heatrk.languageapp.main.impl.R
import kotlin.math.roundToInt

@Composable
fun MainAppBar(
    state: MainAppBarState,
    modifier: Modifier = Modifier,
    scrollingBehaviour: MainAppBarNestedScrollingConnection =
        MainAppBarDefaultNestedScrollingConnection(LocalDensity.current),
) {
    when (state) {
        MainAppBarState.Error ->
            MainAppBarError(
                scrollingBehaviour = scrollingBehaviour,
                modifier = modifier,
            )
        MainAppBarState.Loading ->
            MainAppBarLoading(
                scrollingBehaviour = scrollingBehaviour,
                modifier = modifier,
            )
        is MainAppBarState.Ok ->
            MainAppBarOk(
                state = state,
                scrollingBehaviour = scrollingBehaviour,
                modifier = modifier,
            )
    }
}

@Composable
private fun MainAppBarOk(
    state: MainAppBarState.Ok,
    modifier: Modifier = Modifier,
    scrollingBehaviour: MainAppBarNestedScrollingConnection,
) {
    MainAppBarLayout(
        avatarContent = {
            Image(
                painter = state.avatar.extract(),
                contentDescription = stringResource(R.string.accessibility_go_to_profile),
                modifier = Modifier
                    .size(54.dp)
            )
        },
        titleContent = {
            Text(
                text = stringResource(R.string.main_app_bar_title_named, state.firstName),
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
    scrollingBehaviour: MainAppBarNestedScrollingConnection,
) {
    val shimmerBackgroundColor = AppTheme.colors.shimmerBackground
        .copy(alpha = 0.5f)
    
    val shimmerForegroundColor = AppTheme.colors.shimmerForeground

    MainAppBarLayout(
        avatarContent = {
            Box(
                modifier = Modifier
                    .requiredSize(54.dp)
                    .clip(CircleShape)
                    .shimmerEffect(
                        shimmerBackgroundColor = shimmerBackgroundColor,
                        shimmerForegroundColor = shimmerForegroundColor
                    )
            )
        },
        titleContent = {
            Box(
                modifier = Modifier
                    .requiredSize(width = 100.dp, height = 22.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .shimmerEffect(
                        shimmerBackgroundColor = shimmerBackgroundColor,
                        shimmerForegroundColor = shimmerForegroundColor
                    )
            )
        },
        descriptionContent = {
            Box(
                modifier = Modifier
                    .requiredSize(width = 200.dp, height = 17.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect(
                        shimmerBackgroundColor = shimmerBackgroundColor,
                        shimmerForegroundColor = shimmerForegroundColor
                    )
            )
        },
        scrollingBehaviour = scrollingBehaviour,
        modifier = modifier,
    )
}

@Composable
private fun MainAppBarError(
    modifier: Modifier = Modifier,
    scrollingBehaviour: MainAppBarNestedScrollingConnection,
) {
    MainAppBarLayout(
        avatarContent = {
            Image(
                painter = painterResource(R.drawable.ic_avatar_placeholder),
                contentDescription = stringResource(R.string.accessibility_go_to_profile),
                modifier = Modifier
                    .size(54.dp)
            )
        },
        titleContent = {
            Text(
                text = stringResource(R.string.main_app_bar_title),
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
private fun MainAppBarLayout(
    modifier: Modifier = Modifier,
    scrollingBehaviour: MainAppBarNestedScrollingConnection,
    avatarContent: @Composable () -> Unit,
    titleContent: @Composable () -> Unit,
    descriptionContent: @Composable () -> Unit,
) {
    val scrollProgress by scrollingBehaviour.getAppBarProgressState()

    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = modifier
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                layout(placeable.width, (placeable.height * scrollProgress).roundToInt()) {
                    placeable.placeRelative(
                        x = 0,
                        y = (placeable.height * (scrollProgress - 1)).roundToInt()
                    )
                }
            }
            .alpha(scrollProgress)
            .fillMaxWidth()
            .height(MainAppBarHeight)
            .background(AppTheme.colors.primary)
            .padding(
                horizontal = 24.dp,
                vertical = 10.dp,
            )
    ) {
        avatarContent()

        Spacer(modifier = Modifier.height(5.dp))

        titleContent()

        Spacer(modifier = Modifier.height(5.dp))

        descriptionContent()
    }
}

sealed interface MainAppBarState {
    data object Loading : MainAppBarState

    data object Error : MainAppBarState

    data class Ok(
        val firstName: String,
        val avatar: PainterResource
    ) : MainAppBarState
}

val MainAppBarHeight = 175.dp

private class MainAppBarPreviewStateProvider : PreviewParameterProvider<MainAppBarState> {
    override val values = sequenceOf(
        MainAppBarState.Loading,
        MainAppBarState.Error,
        MainAppBarState.Ok(
            firstName = "Emil",
            avatar = painterRes(R.drawable.ic_avatar_placeholder),
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
