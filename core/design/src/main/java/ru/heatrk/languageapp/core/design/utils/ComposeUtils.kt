package ru.heatrk.languageapp.core.design.utils

import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

const val SMALL_DEVICE_MAX_WIDTH_DP = 400

const val COMPOSE_LARGE_DEVICE_SPEC =
    "spec:width=500dp, " +
            "height=800dp, " +
            "orientation=portrait"

@Composable
fun isLargeScreen(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp > SMALL_DEVICE_MAX_WIDTH_DP
}

fun Modifier.supportLargeScreen() =
    then(
        Modifier
            .requiredWidthIn(max = SMALL_DEVICE_MAX_WIDTH_DP.dp)
    )
