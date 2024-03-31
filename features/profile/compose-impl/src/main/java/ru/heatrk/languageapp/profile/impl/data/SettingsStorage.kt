package ru.heatrk.languageapp.profile.impl.data

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.heatrk.languageapp.profile.api.domain.Language

class SettingsStorage(
    applicationContext: Context,
    private val storageDispatcher: CoroutineDispatcher,
) {
    private val preferences = applicationContext.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    suspend fun saveLanguage(language: Language) =
        withContext(storageDispatcher) {
            preferences.edit {
                putString(PREFS_LANGUAGE_KEY, language.name)
            }
        }

    suspend fun getSavedLanguage(): Language?  =
        withContext(storageDispatcher) {
            preferences.getString(PREFS_LANGUAGE_KEY, null)?.let { name ->
                Language.entries.find { language -> language.name == name }
            }
        }

    companion object {
        private const val PREFS_NAME = "settings_prefs"
        private const val PREFS_LANGUAGE_KEY = "language_key"
    }
}
