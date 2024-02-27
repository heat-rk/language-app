package ru.heatrk.languageapp.core.design.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.heatrk.languageapp.core.design.styles.AppTheme

@Composable
fun AppTextField(
    value: String,
    placeholder: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = label,
            style = AppTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = value,
            onValueChange = onValueChange,
            isError = isError,
            shape = AppTheme.shapes.medium,
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = AppTheme.colors.inputFieldBackground,
                focusedContainerColor = AppTheme.colors.inputFieldBackground,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
            placeholder = {
                Text(
                    text = placeholder,
                    color = AppTheme.colors.inputFieldHint
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun AppTextFieldPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .wrapContentSize()
        ) {
            AppTextField(
                value = "",
                placeholder = "Надо что-то ввести",
                label = "Здесь ты пишешь что-то",
                onValueChange = { /* ... */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(12.dp)
            )
        }
    }
}
