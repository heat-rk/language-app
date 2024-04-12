package ru.heatrk.languageapp.auth.api.domain.google

interface AuthGoogleNonceProvider {
    suspend fun provideNonce(): AuthGoogleNonce
}
