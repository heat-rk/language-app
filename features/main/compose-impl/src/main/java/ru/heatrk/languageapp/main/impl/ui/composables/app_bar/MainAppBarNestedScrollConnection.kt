package ru.heatrk.languageapp.main.impl.ui.composables.app_bar

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity

@Stable
abstract class MainAppBarNestedScrollConnection : NestedScrollConnection {
    var scrollingDistance: Float = 0f
    abstract val scrollOffset: Float
    abstract val scrollProgress: Float
    abstract val isScrolling: Boolean
}

class MainAppBarDefaultNestedScrollConnection : MainAppBarNestedScrollConnection() {
    override val scrollOffset = 0f
    override val scrollProgress = 1f
    override val isScrolling = false
}


class MainAppBarSnapNestedScrollConnection(
    private val scrollableState: ScrollableState,
) : MainAppBarNestedScrollConnection() {
    override var scrollOffset by mutableFloatStateOf(0f)
    override var scrollProgress by mutableFloatStateOf(1f)
    override val isScrolling get() = scrollableState.isScrollInProgress

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = available.y.toInt()

        val newOffset = scrollOffset + delta
        val previousOffset = scrollOffset

        scrollOffset = newOffset.coerceIn(-scrollingDistance, 0f)

        scrollProgress = if (scrollingDistance > 0f) {
            (scrollOffset + scrollingDistance) / scrollingDistance
        } else {
            0f
        }

        val consumed = scrollOffset - previousOffset

        return Offset(0f, consumed)
    }
}


@Composable
fun rememberAppBarSnapScrollConnection(
    scrollableState: ScrollableState
): MainAppBarNestedScrollConnection {
    val density = LocalDensity.current

    return remember(density) {
        MainAppBarSnapNestedScrollConnection(
            scrollableState = scrollableState,
        )
    }
}
