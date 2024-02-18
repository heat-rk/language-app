package ru.heatrk.languageapp.core.design.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.heatrk.languageapp.core.design.styles.AppTheme

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var buttonContentWidth by remember { mutableFloatStateOf(0f) }
    val buttonBlurColor = AppTheme.colors.onSecondary.copy(BUTTON_BLUR_ALPHA)
    val transparentColor = Color.Transparent

    val buttonBlurTopBrush = remember {
        Brush.verticalGradient(
            colors = listOf(
                buttonBlurColor,
                transparentColor,
            ),
            startY = 0f,
            endY = BUTTON_BLUR_SIZE,
        )
    }
    
    val buttonBlurLeftBrush = remember {
        Brush.linearGradient(
            colors = listOf(
                buttonBlurColor,
                transparentColor,
            ),
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = BUTTON_BLUR_SIZE, y = BUTTON_BLUR_SIZE)
        )
    }

    val buttonBlurRightBrush = remember(buttonContentWidth) {
        Brush.linearGradient(
            colors = listOf(
                buttonBlurColor,
                transparentColor,
            ),
            start = Offset(x = buttonContentWidth, y = 0f),
            end = Offset(x = buttonContentWidth - BUTTON_BLUR_SIZE, y = BUTTON_BLUR_SIZE)
        )
    }

    AppButton(
        text = text,
        backgroundBrushes = listOf(
            buttonBlurTopBrush,
            buttonBlurLeftBrush,
            buttonBlurRightBrush,
        ),
        onClick = onClick,
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                buttonContentWidth = coordinates.size.width.toFloat()
            },
    )
}

@Composable
private fun AppButton(
    text: String,
    backgroundBrushes: List<Brush>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val buttonColor = AppTheme.colors.secondary

    Button(
        onClick = onClick,
        shape = AppTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(),
        modifier = modifier
            .height(56.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(buttonColor)
                .run {
                    var newModifier = this

                    backgroundBrushes.forEach { brush ->
                        newModifier = newModifier.background(brush)
                    }

                    newModifier
                }
        ) {
            Text(
                text = text,
                style = AppTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

private const val BUTTON_BLUR_ALPHA = 0.2f
private const val BUTTON_BLUR_SIZE = 30f

@Composable
@Preview
private fun AppButtonPreview() {
    AppTheme {
        AppButton(
            text = "Нажми меня",
            onClick = { /* ... */ },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        )
    }
}
