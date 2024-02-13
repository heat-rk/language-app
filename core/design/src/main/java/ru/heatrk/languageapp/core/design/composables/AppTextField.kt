package ru.heatrk.languageapp.core.design.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.heatrk.languageapp.core.design.styles.AppTheme

@Composable
fun AppTextField(
    value: String,
    placeholder: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
) {
    val mainColor = AppTheme.colors.primary
    val transparentMainColor = mainColor.copy(alpha = 0.4f)

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        isError = isError,
        shape = AppTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedTextColor = mainColor,
            focusedTextColor = mainColor,
            unfocusedBorderColor = mainColor,
            focusedBorderColor = mainColor,
        ),
        placeholder = {
            Text(
                text = placeholder,
                color = transparentMainColor
            )
        },
        label = {
            Box(
                modifier = Modifier
                    .wrapContentSize()
            ) {
                Text(
                    text = label,
                    color = mainColor
                )
            }
        },
        modifier = modifier,
    )
}

@Composable
@Preview
private fun AppTextFieldPreview() {
    AppTheme {
        AppTextField(
            value = "",
            placeholder = "Надо что-то ввести",
            label = "Здесь ты пишешь что-то",
            onValueChange = { /* ... */ },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}
