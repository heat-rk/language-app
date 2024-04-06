package ru.heatrk.languageapp.profile.impl.domain

interface AvatarCropper {
    suspend fun crop(avatarUri: String, avatarCrop: AvatarCrop): Result

    data class Result(
        val bytes: ByteArray,
        val extension: String,
    )
}
