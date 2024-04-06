package ru.heatrk.languageapp.profile.impl.ui.screens.profile

import androidx.annotation.StringRes
import ru.heatrk.languageapp.profile.impl.R

enum class AvatarSourceButton(@StringRes val textRes: Int) {
    Gallery(R.string.profile_change_image_gallery),
    Camera(R.string.profile_change_image_camera);
}
