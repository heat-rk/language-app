package ru.heatrk.languageapp.main.impl.ui.composables.app_bar

import androidx.annotation.FloatRange
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity

@Immutable
abstract class MainAppBarNestedScrollConnection : NestedScrollConnection {
    var appBarScrollingDistance: Float = 0f

    @Composable
    @FloatRange(from = 0.0, to = 1.0)
    abstract fun getAppBarProgressState(): State<Float>
}

class MainAppBarDefaultNestedScrollConnection : MainAppBarNestedScrollConnection() {
    @Composable
    override fun getAppBarProgressState(): State<Float> {
        return remember { mutableFloatStateOf(1f) }
    }
}


class MainAppBarSnapNestedScrollConnection(
    private val scrollableState: ScrollableState,
) : MainAppBarNestedScrollConnection() {
    private var scrollOffset by mutableFloatStateOf(0f)

    @Composable
    @FloatRange(from = 0.0, to = 1.0)
    override fun getAppBarProgressState(): State<Float> {
        val scrollProgress = remember(scrollOffset) {
            (scrollOffset + appBarScrollingDistance) / appBarScrollingDistance
        }

        return animateFloatAsState(
            targetValue = if (scrollableState.isScrollInProgress) {
                scrollProgress
            } else {
                if (scrollProgress > 0.5f) 1f else 0f
            },
            label = "DefaultMainAppBarScrollAnimation"
        )
    }

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = available.y.toInt()
        val newOffset = scrollOffset + delta
        val previousOffset = scrollOffset
        scrollOffset = newOffset.coerceIn(-appBarScrollingDistance, 0f)
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
