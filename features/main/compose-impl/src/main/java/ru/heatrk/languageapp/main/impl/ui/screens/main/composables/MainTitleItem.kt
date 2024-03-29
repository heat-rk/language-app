package ru.heatrk.languageapp.main.impl.ui.screens.main.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.heatrk.languageapp.core.design.styles.AppTheme

@Composable
fun MainTitleItem(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = AppTheme.typography.titleMedium,
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        color = AppTheme.colors.onBackground,
        modifier = modifier,
    )
}
