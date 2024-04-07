package ru.heatrk.languageapp.profile.impl.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.heatrk.languageapp.common.utils.PainterResource
import ru.heatrk.languageapp.common.utils.ImagePainterSize
import ru.heatrk.languageapp.common.utils.StringResource
import ru.heatrk.languageapp.common.utils.extract
import ru.heatrk.languageapp.common.utils.painterRes
import ru.heatrk.languageapp.common.utils.strRes
import ru.heatrk.languageapp.core.design.composables.AppBar
import ru.heatrk.languageapp.core.design.composables.AppBarTitleGravity
import ru.heatrk.languageapp.core.design.composables.shimmerEffect
import ru.heatrk.languageapp.core.design.styles.AppTheme
import ru.heatrk.languageapp.profile.impl.R
import kotlin.math.roundToInt
import ru.heatrk.languageapp.core.design.R as DesignR

@Composable
fun ProfileAppBar(
    fullName: StringResource?,
    avatar: PainterResource?,
    modifier: Modifier = Modifier,
    onGoBackClick: () -> Unit,
) {
    ProfileHeaderLayout(
        avatarContent = {
            Image(
                painter = avatar
                    ?.extract(size = ImagePainterSize(ProfileAvatarSize.value.roundToInt()))
                    ?: painterResource(DesignR.drawable.ic_avatar_placeholder),
                contentDescription = stringResource(DesignR.string.accessibility_go_to_profile),
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .size(ProfileAvatarSize)
                    .clip(CircleShape)
            )
        },
        nameContent = {
            Text(
                text = fullName?.extract() ?: stringResource(DesignR.string.profile_name_unknown),
                color = AppTheme.colors.onPrimary,
                style = AppTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
            )
        },
        onGoBackClick = onGoBackClick,
        modifier = modifier,
    )
}

@Composable
fun ProfileAppBarShimmer(
    onGoBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shimmerBackgroundColor = AppTheme.colors.shimmerBackground
        .copy(alpha = 0.5f)

    val shimmerForegroundColor = AppTheme.colors.shimmerForeground

    ProfileHeaderLayout(
        avatarContent = {
            Box(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .size(ProfileAvatarSize)
                    .clip(CircleShape)
                    .shimmerEffect(
                        shimmerBackgroundColor = shimmerBackgroundColor,
                        shimmerForegroundColor = shimmerForegroundColor,
                    )
            )
        },
        nameContent = {
            Box(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .size(
                        width = 150.dp,
                        height = 22.dp,
                    )
                    .clip(AppTheme.shapes.medium)
                    .shimmerEffect(
                        shimmerBackgroundColor = shimmerBackgroundColor,
                        shimmerForegroundColor = shimmerForegroundColor,
                    )
            )
        },
        onGoBackClick = onGoBackClick,
        modifier = modifier
    )
}

@Composable
private fun ProfileHeaderLayout(
    avatarContent: @Composable () -> Unit,
    nameContent: @Composable () -> Unit,
    onGoBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(AppTheme.colors.primary)
    ) {
        AppBar(
            title = stringResource(R.string.profile_title),
            titleGravity = AppBarTitleGravity.CENTER,
            onGoBackClick = onGoBackClick,
        )

        avatarContent()

        Spacer(modifier = Modifier.height(5.dp))

        nameContent()

        Spacer(modifier = Modifier.height(20.dp))
    }
}

private val ProfileAvatarSize = 134.dp

@Composable
private fun ProfileHeaderPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            ProfileAppBarShimmer(
                onGoBackClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileAppBar(
                fullName = strRes("Ivan Ivanov"),
                avatar = painterRes(DesignR.drawable.ic_avatar_placeholder),
                onGoBackClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ProfileHeaderPreviewLight() {
    ProfileHeaderPreview()
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun ProfileHeaderPreviewDark() {
    ProfileHeaderPreview()
}
