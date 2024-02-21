package ru.heatrk.languageapp.core.design.composables

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
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
    val currentDensity = LocalDensity.current
    var buttonWidth by remember { mutableFloatStateOf(0f) }
    val buttonColor = AppTheme.colors.secondary
    val buttonShape = AppTheme.shapes.medium
    val buttonBlurColor = AppTheme.colors.onSecondary.copy(BUTTON_BLUR_ALPHA)
    val buttonBlurSize = remember(currentDensity) {
        with(currentDensity) { BUTTON_BLUR_SIZE_DP.dp.toPx() }
    }

    val blurPaint = remember {
        Paint().apply {
            val frameworkPaint = asFrameworkPaint()
            frameworkPaint.maskFilter = BlurMaskFilter(buttonBlurSize, BlurMaskFilter.Blur.NORMAL)
            frameworkPaint.style = android.graphics.Paint.Style.STROKE
            frameworkPaint.color = buttonBlurColor.toArgb()
            frameworkPaint.strokeWidth = buttonBlurSize
        }
    }

    val blurPath = remember { Path() }

    val buttonTopStartRadius = remember(currentDensity) {
        buttonShape.topStart.toPx(Size.Unspecified, currentDensity)
    }

    val buttonTopEndRadius = remember(currentDensity) {
        buttonShape.topEnd.toPx(Size.Unspecified, currentDensity)
    }

    LaunchedEffect(
        blurPath, buttonWidth, buttonBlurSize,
        buttonTopStartRadius, buttonTopEndRadius
    ) {
        with(blurPath) {
            moveTo(x = 0f, y = buttonTopStartRadius)

            cubicTo(
                x1 = buttonTopStartRadius * 0.1f, y1 = buttonBlurSize + buttonTopStartRadius * 0.3f,
                x2 = buttonTopStartRadius * 0.3f, y2 = buttonBlurSize + buttonTopStartRadius * 0.1f,
                x3 = buttonTopStartRadius, y3 = buttonBlurSize,
            )

            lineTo(x = buttonWidth - buttonTopEndRadius, y = buttonBlurSize)

            cubicTo(
                x1 = buttonWidth - buttonTopEndRadius * 0.3f, y1 = buttonBlurSize + buttonTopEndRadius * 0.1f,
                x2 = buttonWidth - buttonTopEndRadius * 0.1f, y2 = buttonBlurSize + buttonTopEndRadius * 0.3f,
                x3 = buttonWidth, y3 = buttonBlurSize + buttonTopEndRadius,
            )
        }
    }

    AppButton(
        text = text,
        onClick = onClick,
        buttonShape = buttonShape,
        buttonColor = buttonColor,
        blurPath = blurPath,
        blurPaint = blurPaint,
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                buttonWidth = coordinates.size.width.toFloat()
            }
    )
}

@Composable
private fun AppButton(
    text: String,
    buttonShape: Shape,
    buttonColor: Color,
    blurPath: Path,
    blurPaint: Paint,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        shape = buttonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor
        ),
        contentPadding = PaddingValues(),
        modifier = modifier
            .height(56.dp)
            .clip(buttonShape)
            .drawWithContent {
                drawContent()

                drawIntoCanvas { canvas ->
                    canvas.drawPath(
                        blurPath,
                        blurPaint
                    )
                }
            }
    ) {
        Text(
            text = text,
            style = AppTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
        )
    }
}

private const val BUTTON_BLUR_ALPHA = 0.2f
private const val BUTTON_BLUR_SIZE_DP = 4

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
