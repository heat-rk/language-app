package ru.heatrk.languageapp.profile.impl.ui.screens.avatar_crop

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import ru.heatrk.languageapp.common.utils.launchSafe
import ru.heatrk.languageapp.common.utils.painterRes
import ru.heatrk.languageapp.common.utils.states.ProcessingState
import ru.heatrk.languageapp.common.utils.strRes
import ru.heatrk.languageapp.core.design.utils.withReturnToNone
import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.profile.impl.domain.AvatarCrop
import ru.heatrk.languageapp.profile.impl.domain.CroppedAvatarUploadUseCase
import ru.heatrk.languageapp.profile.impl.ui.screens.avatar_crop.AvatarCropContract.Intent
import ru.heatrk.languageapp.profile.impl.ui.screens.avatar_crop.AvatarCropContract.SideEffect
import ru.heatrk.languageapp.profile.impl.ui.screens.avatar_crop.AvatarCropContract.State
import ru.heatrk.languageapp.core.design.R as DesignR

private typealias IntentBody = SimpleSyntax<State, SideEffect>

internal class AvatarCropViewModel(
    private val photoUri: String,
    private val  router: Router,
    private val croppedAvatarUpload: CroppedAvatarUploadUseCase,
) : ViewModel(), ContainerHost<State, SideEffect> {
    override val container = container<State, SideEffect>(
        initialState = State(
            avatar = painterRes(photoUri)
        )
    )

    private var avatarCrop: AvatarCrop = AvatarCrop.Full

    fun processIntent(intent: Intent) = intent {
        when (intent) {
            is Intent.OnAvatarCropChanged ->
                onAvatarCropChanged(intent.offset, intent.size)
            Intent.OnGoBackClick ->
                onGoBackClick()
            Intent.OnSaveClick ->
                onSaveClick()
        }
    }

    private suspend fun onGoBackClick() {
        router.navigateBack()
    }

    private fun onAvatarCropChanged(offset: IntOffset, size: IntSize) {
        avatarCrop = AvatarCrop.Exactly(
            left = offset.x,
            top = offset.y,
            width = size.width,
            height = size.height,
        )
    }

    private suspend fun IntentBody.onSaveClick() {
        viewModelScope.launchSafe(
            block = {
                reduce { state.copy(savingState = ProcessingState.InProgress) }

                croppedAvatarUpload(
                    avatarUri = photoUri,
                    avatarCrop = avatarCrop,
                )

                ProcessingState.Success.withReturnToNone { recoveringState ->
                    reduce { state.copy(savingState = recoveringState) }
                }

                router.navigateBack()
            },
            onError = {
                postSideEffect(SideEffect.Message(strRes(DesignR.string.error_smth_went_wrong)))

                ProcessingState.Error.withReturnToNone { recoveringState ->
                    reduce { state.copy(savingState = recoveringState) }
                }
            }
        )
    }
}
