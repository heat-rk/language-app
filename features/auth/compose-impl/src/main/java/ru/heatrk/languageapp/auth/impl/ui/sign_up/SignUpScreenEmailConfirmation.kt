package ru.heatrk.languageapp.auth.impl.ui.sign_up

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.heatrk.languageapp.auth.api.ui.navigation.SIGN_UP_SCREEN_TEST_TAG
import ru.heatrk.languageapp.auth.impl.R
import ru.heatrk.languageapp.auth.impl.ui.sign_up.SignUpScreenContract.Intent
import ru.heatrk.languageapp.core.design.composables.AppRootContainer
import ru.heatrk.languageapp.core.design.composables.button.AppButton
import ru.heatrk.languageapp.core.design.styles.AppTheme
import ru.heatrk.languageapp.core.design.R as DesignR

@Composable
fun SignUpScreenEmailConfirmation(
    onIntent: (Intent) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .testTag(SIGN_UP_SCREEN_TEST_TAG)
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Image(
            imageVector = ImageVector.vectorResource(DesignR.drawable.ic_round_check_24),
            contentDescription = null,
            colorFilter = ColorFilter.tint(AppTheme.colors.successColor),
            modifier = Modifier
                .size(64.dp),
        )
        
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = stringResource(R.string.signup_waiting_for_email_confirm),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            style = AppTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.signup_email_confirm_description),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            color = AppTheme.colors.textBody,
            style = AppTheme.typography.titleMedium,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.weight(1f))

        AppButton(
            text = stringResource(DesignR.string.finish_positive),
            onClick = { onIntent(Intent.OnFinishButtonClick) },
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
private fun SignUpScreenEmailConfirmationPreview() {
    AppRootContainer {
        SignUpScreenEmailConfirmation(
            onIntent = {}
        )
    }
}

@Composable
@Preview(
    showBackground = true,
)
private fun SignUpScreenEmailConfirmationPreviewLight() {
    SignUpScreenEmailConfirmationPreview()
}

@Composable
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
private fun SignUpScreenEmailConfirmationPreviewDark() {
    SignUpScreenEmailConfirmationPreview()
}
