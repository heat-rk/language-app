package ru.heatrk.languageapp.auth.test

import ru.heatrk.languageapp.auth.impl.domain.google.AuthGoogleNonce
import ru.heatrk.languageapp.auth.impl.domain.google.AuthGoogleNonceProvider

object AuthGoogleNonceProviderStub : AuthGoogleNonceProvider {
    override suspend fun provideNonce() = AuthGoogleNonce("", "")
}
