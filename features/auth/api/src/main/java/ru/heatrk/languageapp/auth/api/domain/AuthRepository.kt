package ru.heatrk.languageapp.auth.api.domain

interface AuthRepository {
    suspend fun signIn(
        email: String,
        password: String,
    )

    suspend fun signInWithGoogle(
        idToken: String,
        rawNonce: String
    )
}
