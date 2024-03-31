package ru.heatrk.languageapp.profile.impl.ui.screens.select_language

import ru.heatrk.languageapp.common.utils.StringResource

internal data class LanguageItem(
    val id: String,
    val name: StringResource,
    val isSelected: Boolean = false,
)
