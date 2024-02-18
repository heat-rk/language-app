package ru.heatrk.languageapp.onboarding.impl.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.heatrk.languageapp.common.utils.StringResource
import ru.heatrk.languageapp.common.utils.VectorResource
import ru.heatrk.languageapp.common.utils.extract
import ru.heatrk.languageapp.common.utils.strRes
import ru.heatrk.languageapp.common.utils.testTag
import ru.heatrk.languageapp.common.utils.vectorRes
import ru.heatrk.languageapp.core.design.composables.AppButton
import ru.heatrk.languageapp.core.design.composables.AppTextButton
import ru.heatrk.languageapp.core.design.composables.DotsProgressIndicator
import ru.heatrk.languageapp.core.design.composables.animation.FadeInAnimatedContent
import ru.heatrk.languageapp.core.design.composables.animation.RightToLeftAnimatedContent
import ru.heatrk.languageapp.core.design.composables.shimmerEffect
import ru.heatrk.languageapp.core.design.styles.AppTheme
import ru.heatrk.languageapp.onboarding.api.ui.navigation.ONBOARDING_SCREEN_TEST_TAG
import ru.heatrk.languageapp.onboarding.impl.R
import ru.heatrk.languageapp.onboarding.impl.di.OnboardingComponent
import ru.heatrk.languageapp.onboarding.impl.ui.OnboardingContract.State
import ru.heatrk.languageapp.onboarding.impl.ui.OnboardingContract.Intent

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = viewModel(
        factory = OnboardingComponent.onboardingViewModelFactory
    ),
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    OnboardingScreen(
        state = state,
        onIntent = viewModel::processIntent,
        modifier = Modifier
            .testTag(ONBOARDING_SCREEN_TEST_TAG)
    )
}

@Composable
private fun OnboardingScreen(
    state: State,
    onIntent: (Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is State.Loading -> {
            OnboardingScreenLoading(
                modifier = modifier,
            )
        }
        is State.Loaded -> {
            OnboardingScreenLoaded(
                state = state,
                onIntent = onIntent,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun OnboardingScreenLoading(
    modifier: Modifier = Modifier,
) {
    OnboardingScreenLayout(
        imageContent = {
            Box(
                modifier = modifier
                    .size(220.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )
        },
        progressContent = {
            Box(
                modifier = Modifier
                    .size(width = 40.dp, height = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )
        },
        titleContent = {
            Box(
                modifier = Modifier
                    .size(width = 263.dp, height = 28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )
        },
        descriptionContent = {
            Box(
                modifier = Modifier
                    .size(width = 263.dp, height = 20.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )
        },
        primaryButtonContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )
        },
        secondaryButtonContent = {
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(width = 111.dp, height = 20.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .shimmerEffect()
            )
        },
        modifier = Modifier
            .testTag(ONBOARDING_SHIMMER_TEST_TAG)
    )
}

@Composable
private fun OnboardingScreenLoaded(
    state: State.Loaded,
    onIntent: (Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    OnboardingScreenLayout(
        imageContent = {
            OnboardingImage(vectorResource = state.image)
        },
        progressContent = {
            DotsProgressIndicator(
                progress = state.progress,
                total = state.total
            )
        },
        titleContent = {
            OnboardingTitle(title = state.title)
        },
        descriptionContent = {
            OnboardingDescription(description = state.description)
        },
        primaryButtonContent = {
            OnboardingPrimaryButton(
                progress = state.progress,
                total = state.total,
                onClick = { onIntent(Intent.Next) }
            )
        },
        secondaryButtonContent = {
            AppTextButton(
                text = stringResource(R.string.onboarding_skip),
                onClick = { onIntent(Intent.Skip) },
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        modifier = modifier
    )
}

@Composable
private fun OnboardingImage(
    vectorResource: VectorResource
) {
    RightToLeftAnimatedContent(
        targetState = vectorResource,
        label = "Onboarding:Image",
        modifier = Modifier
            .fillMaxSize()
    ) { image ->
        Image(
            imageVector = image.extract(),
            contentDescription = null,
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .testTag(image.testTag())
        )
    }
}

@Composable
private fun OnboardingTitle(
    title: StringResource
) {
    FadeInAnimatedContent(
        targetState = title.extract() ?: "",
        label = "Onboarding:Title",
    ) { text ->
        Text(
            text = text,
            style = AppTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
            color = AppTheme.colors.onBackground,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun OnboardingDescription(
    description: StringResource
) {
    FadeInAnimatedContent(
        targetState = description.extract() ?: "",
        label = "Onboarding:Description",
    ) { text ->
        Text(
            text = text,
            style = AppTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal,
            color = AppTheme.colors.textBody,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun OnboardingPrimaryButton(
    progress: Int,
    total: Int,
    onClick: () -> Unit,
) {
    FadeInAnimatedContent(
        targetState = progress,
        label = "Onboarding:Button",
    ) { progress ->
        AppButton(
            text = when (progress) {
                total - 1 -> stringResource(R.string.onboarding_log_in)
                0 -> stringResource(R.string.onboarding_next)
                else -> stringResource(R.string.onboarding_more)
            },
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
    }
}

@Composable
private fun OnboardingScreenLayout(
    imageContent: @Composable BoxScope.() -> Unit,
    progressContent: @Composable ColumnScope.() -> Unit,
    titleContent: @Composable ColumnScope.() -> Unit,
    descriptionContent: @Composable ColumnScope.() -> Unit,
    primaryButtonContent: @Composable ColumnScope.() -> Unit,
    secondaryButtonContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            content = imageContent,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        progressContent()

        Spacer(modifier = Modifier.height(40.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .wrapContentWidth()
                .weight(weight = 0.3f)
        ) {
            titleContent()

            Spacer(modifier = Modifier.height(8.dp))

            descriptionContent()
        }

        primaryButtonContent()

        secondaryButtonContent()

        Spacer(modifier = Modifier.height(34.dp))
    }
}

class OnboardingScreenPreviewStateProvider : PreviewParameterProvider<State> {
    override val values = sequenceOf(
        State.Loading,
        State.Loaded(
            image = vectorRes(R.drawable.onboarding_conversation_based_learning),
            title = strRes(R.string.onboarding_conversation_based_learning_title),
            description = strRes(R.string.onboarding_conversation_based_learning_description),
            progress = 0,
            total = 3,
        )
    )
}

internal const val ONBOARDING_SHIMMER_TEST_TAG = "Onboarding:Shimmer"

@Composable
@Preview(
    showBackground = true,
)
private fun OnboardingScreenPreview(
    @PreviewParameter(OnboardingScreenPreviewStateProvider::class) state: State
) {
    AppTheme {
        OnboardingScreen(
            state = state,
            onIntent = {}
        )
    }
}
