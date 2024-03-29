package ru.heatrk.languageapp.main.impl.ui.screens.main.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.heatrk.languageapp.core.design.styles.AppTheme
import ru.heatrk.languageapp.main.impl.R

@Composable
fun MainExerciseItem(
    onClick: () -> Unit,
    image: Painter,
    title: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    includeSpacer: Boolean = false,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(bottom = if (includeSpacer) 17.dp else 0.dp)
            .clip(AppTheme.shapes.extraLarge)
            .background(backgroundColor)
            .clickable(onClick = onClick)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Image(
            painter = image,
            contentDescription = title,
            modifier = Modifier
                .size(ExerciseImageSize)
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = title,
            style = AppTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal,
            color = AppTheme.colors.onPrimary,
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(10.dp))
    }
}

private val ExerciseImageSize = 90.dp

@Composable
@Preview(showBackground = true)
private fun MainExerciseItemPreview() {
    AppTheme {
        MainExerciseItem(
            onClick = {},
            image = painterResource(R.drawable.word),
            title = stringResource(R.string.main_word_practice),
            backgroundColor = AppTheme.colors.exerciseWordPractice,
            includeSpacer = true,
        )
    }
}
