package ru.heatrk.languageapp.auth.impl.domain

import ru.heatrk.languageapp.auth.api.domain.AuthRepository

class SignInUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ) {
        if (email.isBlank() || password.isBlank()) {
            throw InvalidSignInFieldsValuesException(
                emailError = if (email.isBlank()) {
                    InvalidSignInFieldsValuesException.Email.EMPTY
                } else {
                    null
                },
                passwordError = if (password.isBlank()) {
                    InvalidSignInFieldsValuesException.Password.EMPTY
                } else {
                    null
                },
            )
        }

        repository.signIn(
            email = email.trim(),
            password = password.trim()
        )
    }
}
