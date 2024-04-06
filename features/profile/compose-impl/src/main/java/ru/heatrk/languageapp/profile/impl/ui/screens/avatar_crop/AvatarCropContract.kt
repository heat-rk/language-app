package ru.heatrk.languageapp.profile.impl.ui.screens.avatar_crop

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import ru.heatrk.languageapp.common.utils.PainterResource
import ru.heatrk.languageapp.common.utils.StringResource

internal object AvatarCropContract {
    data class State(
        val avatar: PainterResource,
        val isSaving: Boolean = false,
    )

    sealed interface Intent {
        data object OnGoBackClick : Intent
        data object OnSaveClick : Intent

        data class OnAvatarCropChanged(
            val offset: IntOffset,
            val size: IntSize
        ) : Intent
    }

    sealed interface SideEffect {
        data class Message(val text: StringResource) : SideEffect
    }
}
