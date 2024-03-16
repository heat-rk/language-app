package ru.heatrk.languageapp.auth.impl.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.providers.builtin.IDToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import ru.heatrk.languageapp.auth.api.domain.AuthRepository
import ru.heatrk.languageapp.auth.impl.BuildConfig

class AuthRepositoryImpl(
    private val supabaseClient: SupabaseClient,
    private val supabaseDispatcher: CoroutineDispatcher,
) : AuthRepository {
    override suspend fun signIn(
        email: String,
        password: String
    ) = withContext(supabaseDispatcher) {
        supabaseClient.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    override suspend fun signInWithGoogle(
        idToken: String,
        email: String,
        firstName: String,
        lastName: String,
        rawNonce: String,
    ): Unit = withContext(supabaseDispatcher) {
        supabaseClient.auth.signInWith(IDToken) {
            this.provider = Google
            this.idToken = idToken
            this.nonce = rawNonce
        }

        supabaseClient.auth.modifyUser {
            this.data = jsonOf(firstName, lastName)
        }
    }

    override suspend fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Unit = withContext(supabaseDispatcher) {
        supabaseClient.auth.signUpWith(Email) {
            this.email = email
            this.password = password
            this.data = jsonOf(firstName, lastName)
        }
    }

    override suspend fun resetPassword(email: String): Unit =
        withContext(supabaseDispatcher) {
            supabaseClient.auth.resetPasswordForEmail(
                email = email,
                redirectUrl = buildString {
                    append(BuildConfig.SUPABASE_REDIRECT_SCHEME)
                    append("://")
                    append(BuildConfig.SUPABASE_REDIRECT_HOST)
                    append("/")
                    append(RECOVERY_CONFIRM_URL_PATH)
                }
            )
        }

    override suspend fun applyRecoveryCode(code: String): Unit =
        withContext(supabaseDispatcher) {
            supabaseClient.auth.exchangeCodeForSession(code)
        }

    override suspend fun changePassword(password: String): Unit =
        withContext(supabaseDispatcher) {
            supabaseClient.auth.modifyUser {
                this.password = password
            }
        }

    private fun jsonOf(firstName: String, lastName: String) =
        JsonObject(
            mapOf(
                "first_name" to JsonPrimitive(firstName),
                "last_name" to JsonPrimitive(lastName),
            )
        )

    companion object {
        const val RECOVERY_CONFIRM_URL_PATH = "recovery_confirm"
    }
}
