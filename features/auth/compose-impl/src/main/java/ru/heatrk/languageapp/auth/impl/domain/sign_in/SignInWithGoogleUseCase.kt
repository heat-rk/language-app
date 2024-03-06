package ru.heatrk.languageapp.auth.impl.domain.sign_in

import ru.heatrk.languageapp.auth.api.domain.AuthRepository

class SignInWithGoogleUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        rawNonce: String,
        idToken: String,
        email: String,
        firstName: String,
        lastName: String,
    ) {
        repository.signInWithGoogle(
            rawNonce = rawNonce,
            idToken = idToken,
            email = email,
            firstName = firstName,
            lastName = lastName,
        )
    }
}
