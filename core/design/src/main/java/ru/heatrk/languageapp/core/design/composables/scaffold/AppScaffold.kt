package ru.heatrk.languageapp.core.design.composables.scaffold

import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.heatrk.languageapp.core.design.composables.AppBar
import ru.heatrk.languageapp.core.design.composables.AppBarActionItem
import ru.heatrk.languageapp.core.design.composables.AppBarTitleGravity
import ru.heatrk.languageapp.core.design.composables.AppSnackbarHost
import ru.heatrk.languageapp.core.design.composables.animation.SlideInVerticallyAnimatedContent
import ru.heatrk.languageapp.core.design.styles.AppTheme
import kotlin.math.min

@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    appBarContainerColor: Color = AppTheme.colors.primary,
    appBarContentColor: Color = AppTheme.colors.onPrimary,
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
        topBar = if (isInInspectionMode) {
            // Workaround to properly top bar showing in preview
            appScaffoldTopBarInspectionMode(
                containerColor = appBarContainerColor,
                contentColor = appBarContentColor,
                appBarState = appBarState,
            )
        } else {
            {
                AppScaffoldTopBar(
                    containerColor = appBarContainerColor,
                    contentColor = appBarContentColor,
                    appBarState = appBarState,
                )
            }
        },
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
private fun AppScaffoldTopBar(
    containerColor: Color,
    contentColor: Color,
    appBarState: AppBarState,
) {
    val density = LocalDensity.current

    var previousContainerColor by remember { mutableStateOf(containerColor) }
    val backgroundColor = previousContainerColor
    previousContainerColor = containerColor

    var currentTopBarBottom by remember { mutableFloatStateOf(0f) }
    var previousTopBarBottom by remember {mutableFloatStateOf(0f) }
    var backgroundHeightPx by remember { mutableFloatStateOf(0f) }
    val backgroundHeight = remember(backgroundHeightPx) { with(density) { backgroundHeightPx.toDp() } }

    LaunchedEffect(currentTopBarBottom) {
        if (currentTopBarBottom != previousTopBarBottom) {
            backgroundHeightPx = min(previousTopBarBottom, currentTopBarBottom)
            previousTopBarBottom = currentTopBarBottom
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(backgroundHeight)
            .background(backgroundColor)
    )

    SlideInVerticallyAnimatedContent(
        targetState = appBarState,
        easing = LinearEasing,
        contentKey = AppBarState::key,
        label = "AppBarStateAnimation",
    ) { state ->
        when (state) {
            AppBarState.Hidden -> {
                Box(modifier = Modifier.fillMaxWidth())
            }
            is AppBarState.Default -> {
                AppBar(
                    title = state.title,
                    titleGravity = state.titleGravity,
                    actions = state.actions,
                    containerColor = containerColor,
                    contentColor = contentColor,
                    onGoBackClick = state.onGoBackClick,
                    modifier = Modifier
                        .onGloballyPositioned { coordinates ->
                            currentTopBarBottom = coordinates.boundsInParent().bottom
                        }
                )
            }
            is AppBarState.Custom -> {
                Box(
                    modifier = Modifier
                        .onGloballyPositioned { coordinates ->
                            currentTopBarBottom = coordinates.boundsInParent().bottom
                        }
                ) {
                    state.content()
                }
            }
        }
    }
}

private fun appScaffoldTopBarInspectionMode(
    containerColor: Color,
    contentColor: Color,
    appBarState: AppBarState,
): @Composable () -> Unit  = {
    when (appBarState) {
        AppBarState.Hidden -> Unit
        is AppBarState.Default -> {
            AppBar(
                title = appBarState.title,
                titleGravity = appBarState.titleGravity,
                actions = appBarState.actions,
                onGoBackClick = appBarState.onGoBackClick,
                containerColor = containerColor,
                contentColor = contentColor,
            )
        }
        is AppBarState.Custom -> {
            Box(
                modifier = Modifier
                    .background(containerColor)
            ) {
                appBarState.content()
            }
        }
    }
}

@Immutable
sealed interface AppBarState {

    val key: String

    data object Hidden : AppBarState {
        override val key: String = this::class.java.simpleName
    }

    data class Default(
        val title: String,
        val titleGravity: AppBarTitleGravity = AppBarTitleGravity.START,
        val actions: ImmutableList<AppBarActionItem> = persistentListOf(),
        val onGoBackClick: (() -> Unit)? = null,
    ) : AppBarState {
        override val key: String = this::class.java.simpleName
    }

    data class Custom(
        override val key: String,
        val content: @Composable () -> Unit
    ) : AppBarState
}

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
        ) {}
    }
}
