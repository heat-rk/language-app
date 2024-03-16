package ru.heatrk.languageapp.auth.api.domain

interface AuthRepository {
    suspend fun signIn(
        email: String,
        password: String,
    )

    suspend fun signInWithGoogle(
        idToken: String,
        email: String,
        firstName: String,
        lastName: String,
        rawNonce: String
    )

    suspend fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
    )

    suspend fun resetPassword(email: String)

    suspend fun applyRecoveryCode(code: String)

    suspend fun changePassword(password: String)
}
