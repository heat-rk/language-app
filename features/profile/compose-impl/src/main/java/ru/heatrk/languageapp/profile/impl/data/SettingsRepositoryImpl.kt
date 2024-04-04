@file:Suppress("DEPRECATION")

package ru.heatrk.languageapp.profile.impl.data

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import kotlinx.coroutines.flow.Flow
import ru.heatrk.languageapp.profile.api.domain.ForcedTheme
import ru.heatrk.languageapp.profile.api.domain.Language
import ru.heatrk.languageapp.profile.api.domain.SettingsRepository
import java.util.Locale

internal class SettingsRepositoryImpl(
    private val settingsStorage: SettingsStorage,
    private val applicationContext: Context,
) : SettingsRepository {
    override suspend fun getAvailableLanguages(): List<Language> {
        return Language.entries
    }

    override suspend fun changeLanguage(language: Language) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            applicationContext.getSystemService(LocaleManager::class.java)
                .applicationLocales = LocaleList.forLanguageTags(language.tag)
        } else {
            val locale = Locale(language.tag)
            Locale.setDefault(locale)
            val resources = applicationContext.resources
            val configuration = resources.configuration
            configuration.setLocale(locale)
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }

        settingsStorage.saveLanguage(language)
    }

    override suspend fun isLanguageAlreadySelected(): Boolean {
        return settingsStorage.getSavedLanguage() != null
    }

    override fun getForcedThemeFlow(): Flow<ForcedTheme?> {
        return settingsStorage.getForcedThemeFlow()
    }

    override suspend fun forceTheme(theme: ForcedTheme) {
        settingsStorage.saveForcedTheme(theme)
    }
}
