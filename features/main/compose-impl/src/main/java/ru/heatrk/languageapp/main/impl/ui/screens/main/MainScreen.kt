package ru.heatrk.languageapp.main.impl.ui.screens.main

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.heatrk.languageapp.common.utils.Size
import ru.heatrk.languageapp.common.utils.extract
import ru.heatrk.languageapp.core.design.composables.AppRootContainer
import ru.heatrk.languageapp.core.design.composables.scaffold.AppBarState
import ru.heatrk.languageapp.core.design.composables.scaffold.AppScaffoldControllerEffect
import ru.heatrk.languageapp.core.design.composables.scaffold.LocalAppScaffoldController
import ru.heatrk.languageapp.core.design.composables.shimmerEffect
import ru.heatrk.languageapp.core.design.styles.AppTheme
import ru.heatrk.languageapp.main.impl.R
import ru.heatrk.languageapp.main.impl.ui.composables.app_bar.MainAppBar
import ru.heatrk.languageapp.main.impl.ui.composables.app_bar.MainAppBarState
import ru.heatrk.languageapp.main.impl.ui.composables.app_bar.rememberDefaultAppBarScrollingBehaviour
import ru.heatrk.languageapp.main.impl.ui.screens.main.MainScreenContract.Intent
import ru.heatrk.languageapp.main.impl.ui.screens.main.MainScreenContract.SideEffect
import ru.heatrk.languageapp.main.impl.ui.screens.main.MainScreenContract.State
import kotlin.math.roundToInt

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val sideEffects = viewModel.container.sideEffectFlow

    MainScreenSideEffects(
        sideEffects = sideEffects
    )

    MainScreen(
        state = state,
        onIntent = viewModel::processIntent
    )
}

@Composable
private fun MainScreen(
    state: State,
    onIntent: (Intent) -> Unit,
) {
    val scrollableState = rememberLazyGridState()
    val appBarScrollingBehaviour = rememberDefaultAppBarScrollingBehaviour(scrollableState)

    AppScaffoldControllerEffect(
        appBarState = AppBarState.Custom {
            MainAppBar(
                state = state.profileState.toAppBarState(),
                onAvatarClick = { onIntent(Intent.OnProfileClick) },
                scrollingBehaviour = appBarScrollingBehaviour,
            )
        }
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = scrollableState,
        horizontalArrangement = Arrangement.spacedBy(17.dp),
        contentPadding = PaddingValues(
            horizontal = 24.dp,
            vertical = 11.dp,
        ),
        modifier = Modifier
            .nestedScroll(appBarScrollingBehaviour)
            .fillMaxSize()
    ) {
        titleItem(titleRes = R.string.main_top_users)

        when (state.leaderboard) {
            State.Leaderboard.Loading -> {
                items(
                    count = LEADERBOARD_SHIMMER_ITEMS_COUNT,
                    span = { GridItemSpan(maxLineSpan) },
                ) { index ->
                    MainLeaderboardItemShimmer(
                        includeSpacer = index != LEADERBOARD_SHIMMER_ITEMS_COUNT - 1
                    )
                }
            }
            is State.Leaderboard.Loaded -> {
                itemsIndexed(
                    items = state.leaderboard.items,
                    span = { _, _, -> GridItemSpan(maxLineSpan) },
                    key = { _, leader -> leader.id }
                ) { index, leader ->
                    MainLeaderboardItem(
                        leader = leader,
                        includeSpacer = index != state.leaderboard.items.lastIndex
                    )
                }
            }
        }

        titleItem(
            titleRes = R.string.main_available_exercises,
            paddingTop = 11.dp
        )

        exercisesItems(onIntent)
    }
}

private fun LazyGridScope.exercisesItems(
    onIntent: (Intent) -> Unit
) {
    item {
        MainScreenExerciseItem(
            onClick = { onIntent(Intent.OnGuessAnimalButtonClick) },
            image = painterResource(R.drawable.animal),
            title = stringResource(R.string.main_guess_the_animal),
            backgroundColor = AppTheme.colors.exerciseGuessAnimal,
            includeSpacer = true,
        )
    }

    item {
        MainScreenExerciseItem(
            onClick = { onIntent(Intent.OnWordPracticeButtonClick) },
            image = painterResource(R.drawable.word),
            title = stringResource(R.string.main_word_practice),
            backgroundColor = AppTheme.colors.exerciseWordPractice,
            includeSpacer = true,
        )
    }

    item {
        MainScreenExerciseItem(
            onClick = { onIntent(Intent.OnAuditionButtonClick) },
            image = painterResource(R.drawable.audition),
            title = stringResource(R.string.main_audition),
            backgroundColor = AppTheme.colors.exerciseAudition,
        )
    }

    item {
        MainScreenExerciseItem(
            onClick = { onIntent(Intent.OnGameButtonClick) },
            image = painterResource(R.drawable.game),
            title = stringResource(R.string.main_game),
            backgroundColor = AppTheme.colors.exerciseGame,
        )
    }
}

private fun LazyGridScope.titleItem(
    @StringRes titleRes: Int,
    paddingTop: Dp = 0.dp,
    paddingBottom: Dp = 9.dp,
) {
    item(span = { GridItemSpan(maxLineSpan) },) {
        MainTitleItem(
            title = stringResource(id = titleRes),
            modifier = Modifier
                .padding(top = paddingTop, bottom = paddingBottom)
        )
    }
}

@Composable
private fun MainTitleItem(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = AppTheme.typography.titleMedium,
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        color = AppTheme.colors.onBackground,
        modifier = modifier,
    )
}

@Composable
private fun MainLeaderboardItem(
    leader: State.Leaderboard.Item,
    includeSpacer: Boolean,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(bottom = if (includeSpacer) 10.dp else 0.dp)
            .background(
                color = AppTheme.colors.surface,
                shape = AppTheme.shapes.extraLarge
            )
            .padding(16.dp)
    ) {
        Image(
            painter = leader.avatar
                ?.extract(size = Size(LeaderAvatarSize.value.roundToInt()))
                ?: painterResource(R.drawable.ic_avatar_placeholder),
            contentDescription = null,
            modifier = Modifier
                .size(LeaderAvatarSize)
        )

        Spacer(modifier = Modifier.width(24.dp))

        Text(
            text = leader.fullName ?: stringResource(R.string.main_leader_name_unknown),
            style = AppTheme.typography.bodyLarge,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            color = AppTheme.colors.onBackground,
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(R.string.main_points_formatted, leader.totalScore),
            style = AppTheme.typography.bodyLarge,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            color = AppTheme.colors.onBackground,
        )
    }
}

@Composable
private fun MainLeaderboardItemShimmer(
    includeSpacer: Boolean,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(bottom = if (includeSpacer) 10.dp else 0.dp)
            .clip(AppTheme.shapes.extraLarge)
            .shimmerEffect()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(LeaderAvatarSize)
                .background(
                    color = AppTheme.colors.shimmerForeground,
                    shape = AppTheme.shapes.small,
                )
        )

        Spacer(modifier = Modifier.width(24.dp))

        Box(
            modifier = Modifier
                .size(
                    width = 100.dp,
                    height = 17.dp
                )
                .background(
                    color = AppTheme.colors.shimmerForeground,
                    shape = AppTheme.shapes.small,
                )
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(
                    width = 70.dp,
                    height = 17.dp
                )
                .background(
                    color = AppTheme.colors.shimmerForeground,
                    shape = AppTheme.shapes.small,
                )
        )
    }
}

@Composable
private fun MainScreenExerciseItem(
    onClick: () -> Unit,
    image: Painter,
    title: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    includeSpacer: Boolean = false,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(bottom = if (includeSpacer) 17.dp else 0.dp)
            .clip(AppTheme.shapes.extraLarge)
            .background(backgroundColor)
            .clickable(onClick = onClick)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Image(
            painter = image,
            contentDescription = title,
            modifier = Modifier
                .size(ExerciseImageSize)
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = title,
            style = AppTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal,
            color = AppTheme.colors.onPrimary,
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun MainScreenSideEffects(
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

private fun State.Profile.toAppBarState() = when (this) {
    State.Profile.Loading ->
        MainAppBarState.Loading
    is State.Profile.Loaded ->
        MainAppBarState.Ok(
            firstName = firstName,
            avatar = avatar
        )
}

private val LeaderAvatarSize = 36.dp
private val ExerciseImageSize = 90.dp
private const val LEADERBOARD_SHIMMER_ITEMS_COUNT = 3

@Composable
private fun MainScreenPreview() {
    AppRootContainer {
        MainScreen(
            state = State(),
            onIntent = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun MainScreenPreviewLight() {
    MainScreenPreview()
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun MainScreenPreviewDark() {
    MainScreenPreview()
}
