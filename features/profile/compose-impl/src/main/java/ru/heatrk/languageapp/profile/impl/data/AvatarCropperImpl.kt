package ru.heatrk.languageapp.profile.impl.data

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.heatrk.languageapp.profile.impl.domain.AvatarCrop
import ru.heatrk.languageapp.profile.impl.domain.AvatarCropper
import java.io.ByteArrayOutputStream


class AvatarCropperImpl(
    private val cropperDispatcher: CoroutineDispatcher,
    private val applicationContext: Application,
) : AvatarCropper {
    override suspend fun crop(avatarUri: String, avatarCrop: AvatarCrop): AvatarCropper.Result =
        withContext(cropperDispatcher) {
            val sourceByteArray = applicationContext
                .contentResolver
                .openInputStream(Uri.parse(avatarUri))
                ?.use { stream -> stream.buffered().readBytes() }

            check(sourceByteArray != null) { "Avatar Uri read failed" }

            val bitmap = BitmapFactory
                .decodeByteArray(sourceByteArray, 0, sourceByteArray.size)

            val croppedBitmap = when (avatarCrop) {
                is AvatarCrop.Exactly -> {
                    Bitmap.createBitmap(
                        bitmap,
                        avatarCrop.left,
                        avatarCrop.top,
                        avatarCrop.width,
                        avatarCrop.height
                    )
                }
                AvatarCrop.Full -> {
                    bitmap
                }
            }

            val byteArray = ByteArrayOutputStream().use { stream ->
                croppedBitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, stream)
                stream.toByteArray()
            }

            bitmap.recycle()
            croppedBitmap.recycle()

            AvatarCropper.Result(
                bytes = byteArray,
                extension = "jpeg"
            )
        }

    companion object {
        private const val COMPRESS_QUALITY = 100
    }
}
