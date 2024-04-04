package ru.heatrk.languageapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import ru.heatrk.languageapp.profile.api.domain.SettingsRepository

class ThemeViewModel(
    settingsRepository: SettingsRepository
) : ViewModel() {
    val forcedTheme = settingsRepository.getForcedThemeFlow()
        .stateIn(
            initialValue = null,
            started = SharingStarted.Eagerly,
            scope = viewModelScope
        )
}
