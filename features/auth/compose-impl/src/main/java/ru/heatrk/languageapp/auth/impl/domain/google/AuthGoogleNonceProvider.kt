package ru.heatrk.languageapp.auth.impl.domain.google

interface AuthGoogleNonceProvider {
    suspend fun provideNonce(): AuthGoogleNonce
}
