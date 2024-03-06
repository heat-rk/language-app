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
        rawNonce: String,
    ) = withContext(supabaseDispatcher) {
        supabaseClient.auth.signInWith(IDToken) {
            this.provider = Google
            this.idToken = idToken
            this.nonce = rawNonce
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

            this.data = JsonObject(
                mapOf(
                    "first_name" to JsonPrimitive(firstName),
                    "last_name" to JsonPrimitive(lastName),
                )
            )
        }
    }
}
