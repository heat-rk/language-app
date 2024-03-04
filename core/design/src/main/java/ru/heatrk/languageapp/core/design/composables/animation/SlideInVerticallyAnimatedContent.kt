package ru.heatrk.languageapp.core.design.composables.animation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <S> SlideInVerticallyAnimatedContent(
    targetState: S,
    modifier: Modifier = Modifier,
    label: String = "AnimatedContent",
    contentKey: (targetState: S) -> Any? = { it },
    content: @Composable AnimatedContentScope.(targetState: S) -> Unit,
) {
    AnimatedContent(
        targetState = targetState,
        contentKey = contentKey,
        label = label,
        transitionSpec = {
            slideInVertically() togetherWith
                    slideOutVertically(animationSpec = tween(SLIDE_IN_ANIMATION_DURATION_MILLIS))
        },
        content = content,
        modifier = modifier,
    )
}

private const val SLIDE_IN_ANIMATION_DURATION_MILLIS = 200
