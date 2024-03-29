package ru.heatrk.languageapp.main.impl.ui.screens.main

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.heatrk.languageapp.common.utils.extract
import ru.heatrk.languageapp.core.design.composables.AppRootContainer
import ru.heatrk.languageapp.core.design.composables.scaffold.AppBarState
import ru.heatrk.languageapp.core.design.composables.scaffold.AppScaffoldControllerEffect
import ru.heatrk.languageapp.core.design.composables.scaffold.LocalAppScaffoldController
import ru.heatrk.languageapp.core.design.styles.AppTheme
import ru.heatrk.languageapp.main.impl.R
import ru.heatrk.languageapp.main.impl.ui.composables.app_bar.MainAppBar
import ru.heatrk.languageapp.main.impl.ui.composables.app_bar.MainAppBarState
import ru.heatrk.languageapp.main.impl.ui.composables.app_bar.rememberDefaultAppBarScrollingBehaviour
import ru.heatrk.languageapp.main.impl.ui.screens.main.MainScreenContract.Intent
import ru.heatrk.languageapp.main.impl.ui.screens.main.MainScreenContract.SideEffect
import ru.heatrk.languageapp.main.impl.ui.screens.main.MainScreenContract.State
import ru.heatrk.languageapp.main.impl.ui.screens.main.composables.MainExerciseItem
import ru.heatrk.languageapp.main.impl.ui.screens.main.composables.MainLeaderboardItem
import ru.heatrk.languageapp.main.impl.ui.screens.main.composables.MainLeaderboardItemShimmer
import ru.heatrk.languageapp.main.impl.ui.screens.main.composables.MainTitleItem

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
        MainExerciseItem(
            onClick = { onIntent(Intent.OnGuessAnimalButtonClick) },
            image = painterResource(R.drawable.animal),
            title = stringResource(R.string.main_guess_the_animal),
            backgroundColor = AppTheme.colors.exerciseGuessAnimal,
            includeSpacer = true,
        )
    }

    item {
        MainExerciseItem(
            onClick = { onIntent(Intent.OnWordPracticeButtonClick) },
            image = painterResource(R.drawable.word),
            title = stringResource(R.string.main_word_practice),
            backgroundColor = AppTheme.colors.exerciseWordPractice,
            includeSpacer = true,
        )
    }

    item {
        MainExerciseItem(
            onClick = { onIntent(Intent.OnAuditionButtonClick) },
            image = painterResource(R.drawable.audition),
            title = stringResource(R.string.main_audition),
            backgroundColor = AppTheme.colors.exerciseAudition,
        )
    }

    item {
        MainExerciseItem(
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
