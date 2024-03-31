package ru.heatrk.languageapp.profile.api.domain

interface SettingsRepository {
    suspend fun getAvailableLanguages(): List<Language>
    suspend fun changeLanguage(language: Language)
    suspend fun isLanguageAlreadySelected(): Boolean
}
