package ru.heatrk.languageapp.core.design.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.heatrk.languageapp.core.design.styles.AppTheme

@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    appBarState: AppBarState = AppBarState.Hidden,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    contentPadding: PaddingValues = PaddingValues(24.dp),
    content: @Composable BoxScope.() -> Unit,
) {
    Scaffold(
        containerColor = AppTheme.colors.background,
        snackbarHost = {
            AppSnackbarHost(hostState = snackbarHostState)
        },
        topBar = when (appBarState) {
            AppBarState.Hidden -> {
                EmptyComposable
            }
            is AppBarState.Shown -> {
                {
                    AppBar(
                        title = appBarState.title,
                        titleGravity = appBarState.titleGravity,
                        actions = appBarState.actions,
                        onGoBackClick = appBarState.onGoBackClick,
                    )
                }
            }
        },
        modifier = modifier
            .fillMaxSize()
    ) { scaffoldPaddingValues ->
        Box(
            content = content,
            modifier = Modifier
                .padding(scaffoldPaddingValues)
                .padding(contentPadding)
                .fillMaxSize(),
        )
    }
}

sealed interface AppBarState {
    data object Hidden : AppBarState

    data class Shown(
        val title: String,
        val titleGravity: AppBarTitleGravity = AppBarTitleGravity.START,
        val actions: List<AppBarActionItem> = emptyList(),
        val onGoBackClick: (() -> Unit)? = null,
    ) : AppBarState
}

private val EmptyComposable: @Composable () -> Unit = {}

@Composable
@Preview(showBackground = true)
private fun AppScaffoldPreview() {
    AppScaffold(
        appBarState = AppBarState.Shown(
            title = "Toolbar Title",
            titleGravity = AppBarTitleGravity.CENTER,
            onGoBackClick = {}
        )
    ) {

    }
}
