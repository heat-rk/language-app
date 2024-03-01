package ru.heatrk.languageapp.auth.impl.data.google

import android.content.Context
import androidx.credentials.CredentialManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.heatrk.languageapp.auth.impl.domain.google.AuthGoogleNonce
import ru.heatrk.languageapp.auth.impl.domain.google.AuthGoogleNonceProvider
import java.security.MessageDigest
import java.util.UUID

class AuthGoogleCredentialsManager(
    private val dispatcher: CoroutineDispatcher,
    applicationContext: Context,
) : AuthGoogleNonceProvider {

    val credentialManager = CredentialManager.create(applicationContext)

    override suspend fun provideNonce() = withContext(dispatcher) {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val messageDigest = MessageDigest.getInstance(GOOGLE_NONCE_ENCODING_TYPE)
        val digest = messageDigest.digest(bytes)
        val encodedNonce = digest.fold("") { accumulator, next -> accumulator + "%02x".format(next) }

        AuthGoogleNonce(
            raw = rawNonce,
            encoded = encodedNonce
        )
    }

    companion object {
        private const val GOOGLE_NONCE_ENCODING_TYPE = "SHA-256"
    }
}
