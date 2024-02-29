package ru.heatrk.languageapp.auth.impl.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
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
}
