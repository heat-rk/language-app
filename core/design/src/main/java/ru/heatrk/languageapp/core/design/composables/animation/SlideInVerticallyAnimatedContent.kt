package ru.heatrk.languageapp.core.design.composables.animation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <S> SlideInVerticallyAnimatedContent(
    targetState: S,
    modifier: Modifier = Modifier,
    label: String = "AnimatedContent",
    easing: Easing = FastOutSlowInEasing,
    contentKey: (targetState: S) -> Any? = { it },
    content: @Composable AnimatedContentScope.(targetState: S) -> Unit,
) {
    AnimatedContent(
        targetState = targetState,
        contentKey = contentKey,
        label = label,
        transitionSpec = {
            ContentTransform(
                targetContentEnter = slideInVertically(
                    animationSpec = tween(
                        durationMillis = SLIDE_IN_ANIMATION_DURATION_MILLIS,
                        easing = easing,
                    )
                ),
                initialContentExit = slideOutVertically(
                    animationSpec = tween(
                        durationMillis = SLIDE_IN_ANIMATION_DURATION_MILLIS,
                        easing = easing,
                    )
                )
            )
        },
        content = content,
        modifier = modifier,
    )
}

private const val SLIDE_IN_ANIMATION_DURATION_MILLIS = 200
