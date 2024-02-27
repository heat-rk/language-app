package ru.heatrk.languageapp.core.design.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.heatrk.languageapp.core.design.styles.AppTheme

@Composable
fun AppTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Unspecified,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
) {
    TextButton(
        onClick = onClick,
        shape = AppTheme.shapes.medium,
        colors = ButtonDefaults.textButtonColors(
            contentColor = AppTheme.colors.onBackground
        ),
        contentPadding = contentPadding,
        modifier = modifier
            .height(56.dp),
    ) {
        Text(
            text = text,
            style = AppTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal,
            color = textColor,
        )
    }
}

@Composable
@Preview(
    showBackground = true,
)
private fun AppTextButtonPreview() {
    AppTheme {
        AppTextButton(
            text = "Нажми меня",
            onClick = { /* ... */ },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        )
    }
}
