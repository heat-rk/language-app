package ru.heatrk.languageapp.auth.impl.data

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AuthStorage(
    private val storageDispatcher: CoroutineDispatcher,
    private val applicationContext: Context,
) {
    private val preferences = applicationContext.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    suspend fun saveTokens(tokens: Tokens): Unit =
        withContext(storageDispatcher) {
            saveAccessToken(tokens.accessToken)
            saveRefreshToken(tokens.refreshToken)
        }

    suspend fun getTokens(): Tokens =
        withContext(storageDispatcher) {
            Tokens(
                accessToken = getAccessToken(),
                refreshToken = getRefreshToken()
            )
        }

    suspend fun saveRefreshToken(token: String?): Unit =
        withContext(storageDispatcher) {
            preferences.edit {
                putString(PREFS_REFRESH_TOKEN_KEY, token)
            }
        }

    suspend fun getRefreshToken(): String? =
        withContext(storageDispatcher) {
            preferences.getString(PREFS_REFRESH_TOKEN_KEY, null)
        }

    suspend fun saveAccessToken(token: String?): Unit =
        withContext(storageDispatcher) {
            preferences.edit {
                putString(PREFS_ACCESS_TOKEN_KEY, token)
            }
        }

    suspend fun getAccessToken(): String? =
        withContext(storageDispatcher) {
            preferences.getString(PREFS_ACCESS_TOKEN_KEY, null)
        }

    data class Tokens(
        val accessToken: String?,
        val refreshToken: String?
    )

    companion object {
        private const val PREFS_NAME = "auth_prefs"
        private const val PREFS_ACCESS_TOKEN_KEY = "access_token_key"
        private const val PREFS_REFRESH_TOKEN_KEY = "refresh_token_key"
    }
}
