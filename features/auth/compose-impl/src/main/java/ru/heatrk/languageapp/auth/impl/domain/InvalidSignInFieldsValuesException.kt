package ru.heatrk.languageapp.auth.impl.domain

class InvalidSignInFieldsValuesException(
    val emailError: Email? = null,
    val passwordError: Password? = null,
) : IllegalArgumentException("Invalid sign in fields values: email = $emailError; password = $passwordError") {
    enum class Email {
        EMPTY
    }

    enum class Password {
        EMPTY
    }
}
