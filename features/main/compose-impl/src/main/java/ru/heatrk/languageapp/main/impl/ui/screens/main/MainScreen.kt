package ru.heatrk.languageapp.main.impl.ui.screens.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.heatrk.languageapp.core.design.composables.AppRootContainer
import ru.heatrk.languageapp.core.design.composables.scaffold.AppBarState
import ru.heatrk.languageapp.core.design.composables.scaffold.AppScaffoldControllerEffect
import ru.heatrk.languageapp.main.impl.ui.composables.app_bar.MainAppBar
import ru.heatrk.languageapp.main.impl.ui.composables.app_bar.MainAppBarState
import ru.heatrk.languageapp.main.impl.ui.composables.app_bar.rememberDefaultAppBarScrollingBehaviour
import ru.heatrk.languageapp.main.impl.ui.screens.main.MainScreenContract.State

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    MainScreen(
        state = state
    )
}

@Composable
private fun MainScreen(
    state: State
) {
    val scrollableState = rememberLazyListState()
    val appBarScrollingBehaviour = rememberDefaultAppBarScrollingBehaviour(scrollableState)

    AppScaffoldControllerEffect(
        appBarState = AppBarState.Custom {
            MainAppBar(
                state = state.profileState.toAppBarState(),
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

private fun State.Profile.toAppBarState() = when (this) {
    State.Profile.Error ->
        MainAppBarState.Error
    State.Profile.Loading ->
        MainAppBarState.Loading
    is State.Profile.Ok ->
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
            state = State()
        )
    }
}
