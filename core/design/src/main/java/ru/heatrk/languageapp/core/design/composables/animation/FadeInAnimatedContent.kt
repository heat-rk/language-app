package ru.heatrk.languageapp.core.design.composables.animation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <S> FadeInAnimatedContent(
    targetState: S,
    modifier: Modifier = Modifier,
    fadeOutTargetAlpha: Float = 0f,
    label: String = "AnimatedContent",
    contentKey: (targetState: S) -> Any? = { it },
    content: @Composable AnimatedContentScope.(targetState: S) -> Unit,
) {
    AnimatedContent(
        targetState = targetState,
        contentKey = contentKey,
        label = label,
        transitionSpec = {
            fadeIn() togetherWith
                    fadeOut(
                        animationSpec = tween(FADE_IN_ANIMATION_DURATION_MILLIS),
                        targetAlpha = fadeOutTargetAlpha
                    )
        },
        content = content,
        modifier = modifier,
    )
}

private const val FADE_IN_ANIMATION_DURATION_MILLIS = 200
