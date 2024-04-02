package ru.heatrk.languageapp.core.design.composables.scaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import ru.heatrk.languageapp.core.design.composables.AppBar
import ru.heatrk.languageapp.core.design.composables.AppBarActionItem
import ru.heatrk.languageapp.core.design.composables.AppBarTitleGravity
import ru.heatrk.languageapp.core.design.composables.AppSnackbarHost
import ru.heatrk.languageapp.core.design.composables.animation.SlideInVerticallyAnimatedContent
import ru.heatrk.languageapp.core.design.styles.AppTheme

@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    appBarState: AppBarState = AppBarState.Hidden,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    content: @Composable BoxScope.() -> Unit,
) {
    val isInInspectionMode = LocalInspectionMode.current

    Scaffold(
        containerColor = AppTheme.colors.background,
        snackbarHost = {
            AppSnackbarHost(hostState = snackbarHostState)
        },
        topBar = appScaffoldTopBar(
            isInInspectionMode = isInInspectionMode,
            appBarState = appBarState,
        ),
        modifier = modifier
            .fillMaxSize()
    ) { scaffoldPaddingValues ->
        Box(
            content = content,
            modifier = Modifier
                .padding(scaffoldPaddingValues)
                .fillMaxSize(),
        )
    }
}

@Composable
private fun appScaffoldTopBar(
    isInInspectionMode: Boolean,
    appBarState: AppBarState,
) = if (isInInspectionMode) {
    when (appBarState) {
        AppBarState.Hidden -> {
            EmptyComposable
        }
        is AppBarState.Default -> {
            {
                AppBar(
                    title = appBarState.title,
                    titleGravity = appBarState.titleGravity,
                    actions = appBarState.actions,
                    onGoBackClick = appBarState.onGoBackClick,
                )
            }
        }
        is AppBarState.Custom -> {
            appBarState.content
        }
    }
} else {
    {
        SlideInVerticallyAnimatedContent(
            targetState = appBarState,
            contentKey = AppBarState::key,
            label = "AppBarStateAnimation"
        ) { state ->
            when (state) {
                AppBarState.Hidden -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                is AppBarState.Default -> {
                    AppBar(
                        title = state.title,
                        titleGravity = state.titleGravity,
                        actions = state.actions,
                        onGoBackClick = state.onGoBackClick,
                    )
                }
                is AppBarState.Custom -> {
                    state.content()
                }
            }
        }
    }
}

sealed interface AppBarState {

    val key: String

    data object Hidden : AppBarState {
        override val key = this::class.java.simpleName
    }

    data class Default(
        val title: String,
        val titleGravity: AppBarTitleGravity = AppBarTitleGravity.START,
        val actions: List<AppBarActionItem> = emptyList(),
        val onGoBackClick: (() -> Unit)? = null,
    ) : AppBarState {
        override val key = this::class.java.simpleName
    }

    data class Custom(
        override val key: String,
        val content: @Composable () -> Unit
    ) : AppBarState
}

private val EmptyComposable: @Composable () -> Unit = {}

@Composable
@Preview(showBackground = true)
private fun AppScaffoldPreview() {
    AppTheme {
        AppScaffold(
            appBarState = AppBarState.Default(
                title = "Toolbar Title",
                titleGravity = AppBarTitleGravity.CENTER,
                onGoBackClick = {}
            )
        ) {

        }
    }
}
