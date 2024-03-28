package ru.heatrk.languageapp.main.impl.ui.screens.main

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
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
import ru.heatrk.languageapp.main.impl.ui.composables.app_bar.MainAppBar
import ru.heatrk.languageapp.main.impl.ui.composables.app_bar.MainAppBarState
import ru.heatrk.languageapp.main.impl.ui.composables.app_bar.rememberDefaultAppBarScrollingBehaviour
import ru.heatrk.languageapp.main.impl.ui.screens.main.MainScreenContract.Intent
import ru.heatrk.languageapp.main.impl.ui.screens.main.MainScreenContract.SideEffect
import ru.heatrk.languageapp.main.impl.ui.screens.main.MainScreenContract.State

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
    val scrollableState = rememberLazyListState()
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

    LazyColumn(
        state = scrollableState,
        contentPadding = PaddingValues(24.dp),
        modifier = Modifier
            .nestedScroll(appBarScrollingBehaviour)
            .fillMaxSize()
    ) {
        // TODO
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

@Composable
@Preview(showBackground = true)
private fun MainScreenPreview() {
    AppRootContainer {
        MainScreen(
            state = State(),
            onIntent = {}
        )
    }
}
