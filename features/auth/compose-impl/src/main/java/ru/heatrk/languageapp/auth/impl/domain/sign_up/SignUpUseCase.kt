package ru.heatrk.languageapp.auth.impl.domain.sign_up

import ru.heatrk.languageapp.auth.api.domain.AuthRepository

class SignUpUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmedPassword: String,
    ) {
        validate(
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
            confirmedPassword = confirmedPassword,
        )

        repository.signUp(
            firstName = firstName.trim(),
            lastName = lastName.trim(),
            email = email.trim(),
            password = password.trim(),
        )
    }

    fun validate(
        firstName: String = "",
        lastName: String = "",
        email: String = "",
        password: String = "",
        confirmedPassword: String = "",
        fieldsToValidate: List<Field> = Field.entries,
    ) {
        val errorBuilder = ErrorBuilder()

        if (Field.FIRST_NAME in fieldsToValidate && firstName.isBlank()) {
            errorBuilder.transform { error ->
                error.copy(firstNameError = InvalidSignUpFieldsValuesException.Name.EMPTY)
            }
        }

        if (Field.LAST_NAME in fieldsToValidate && lastName.isBlank()) {
            errorBuilder.transform { error ->
                error.copy(lastNameError = InvalidSignUpFieldsValuesException.Name.EMPTY)
            }
        }

        if (Field.EMAIL in fieldsToValidate && !email.matches(Regex(EMAIL_REGEX_PATTERN))) {
            errorBuilder.transform { error ->
                error.copy(emailError = InvalidSignUpFieldsValuesException.Email.INVALID_FORMAT)
            }
        }

        if (Field.PASSWORD in fieldsToValidate && password.trim().length < MIN_PASSWORD_LENGTH) {
            errorBuilder.transform { error ->
                error.copy(passwordError = InvalidSignUpFieldsValuesException.Password.MIN_LENGTH)
            }
        }

        if (Field.CONFIRMED_PASSWORD in fieldsToValidate && confirmedPassword.trim() != password.trim()) {
            errorBuilder.transform { error ->
                error.copy(confirmedPasswordError = InvalidSignUpFieldsValuesException.ConfirmedPassword.MISMATCH)
            }
        }

        errorBuilder.validationError?.let { error ->
            throw error
        }
    }

    private class ErrorBuilder {
        var validationError: InvalidSignUpFieldsValuesException? = null
            private set

        fun transform(
            transformer: (InvalidSignUpFieldsValuesException) -> InvalidSignUpFieldsValuesException
        ) {
            validationError = transformer(validationError ?: InvalidSignUpFieldsValuesException())
        }
    }

    enum class Field {
        FIRST_NAME,
        LAST_NAME,
        EMAIL,
        PASSWORD,
        CONFIRMED_PASSWORD;
    }

    companion object {
        private const val EMAIL_REGEX_PATTERN = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")

        private const val MIN_PASSWORD_LENGTH = 6
    }
}
