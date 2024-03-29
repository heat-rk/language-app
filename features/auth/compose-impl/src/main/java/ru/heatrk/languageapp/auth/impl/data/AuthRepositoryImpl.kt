package ru.heatrk.languageapp.auth.impl.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.providers.builtin.IDToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import ru.heatrk.languageapp.auth.api.domain.AuthRepository
import ru.heatrk.languageapp.auth.api.domain.User
import ru.heatrk.languageapp.auth.impl.BuildConfig

class AuthRepositoryImpl(
    private val supabaseClient: SupabaseClient,
    private val authStorage: AuthStorage,
    private val json: Json,
    private val supabaseDispatcher: CoroutineDispatcher,
) : AuthRepository {

    override suspend fun awaitInitialization() {
        supabaseClient.auth.awaitInitialization()
    }

    override suspend fun hasSavedSession(): Boolean =
        withContext(supabaseDispatcher) {
            var savedSession = supabaseClient.auth.currentSessionOrNull()

            if (savedSession == null) {
                val refreshToken = authStorage.getRefreshToken()
                if (refreshToken != null) {
                    try {
                        savedSession = supabaseClient.auth.refreshSession(refreshToken)
                    } catch (ignored: RestException) {}
                }
            }

            authStorage.saveTokens(
                AuthStorage.Tokens(
                    accessToken = savedSession?.accessToken,
                    refreshToken = savedSession?.refreshToken
                )
            )

            savedSession != null
        }

    override suspend fun signIn(
        email: String,
        password: String
    ) = withContext(supabaseDispatcher) {
        supabaseClient.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }

        val currentSession = supabaseClient.auth.currentSessionOrNull()

        authStorage.saveTokens(
            AuthStorage.Tokens(
                accessToken = currentSession?.accessToken,
                refreshToken = currentSession?.refreshToken
            )
        )

        val userInfo = supabaseClient.auth.currentUserOrNull()
            ?: throw IllegalStateException("No new user found")

        val userMetaData = userInfo.userMetadata
            ?.let { json.decodeFromJsonElement<UserMetaData>(it) }

        User(
            id = userInfo.id,
            avatarUrl = userMetaData?.avatarUrl,
            firstName = userMetaData?.firstName,
            lastName = userMetaData?.lastName,
        )
    }

    override suspend fun signInWithGoogle(
        idToken: String,
        email: String,
        firstName: String,
        lastName: String,
        rawNonce: String,
    ): User = withContext(supabaseDispatcher) {
        supabaseClient.auth.signInWith(IDToken) {
            this.provider = Google
            this.idToken = idToken
            this.nonce = rawNonce
        }

        supabaseClient.auth.modifyUser {
            this.data = json.decodeFromString(
                json.encodeToString(
                    UserMetaData(
                        firstName = firstName,
                        lastName = lastName,
                    )
                )
            )
        }

        val currentSession = supabaseClient.auth.currentSessionOrNull()

        authStorage.saveTokens(
            AuthStorage.Tokens(
                accessToken = currentSession?.accessToken,
                refreshToken = currentSession?.refreshToken
            )
        )

        val userInfo = supabaseClient.auth.currentUserOrNull()
            ?: throw IllegalStateException("No new user found")

        val userMetaData = userInfo.userMetadata
            ?.let { json.decodeFromJsonElement<UserMetaData>(it) }

        User(
            id = userInfo.id,
            avatarUrl = userMetaData?.avatarUrl,
            firstName = firstName,
            lastName = lastName,
        )
    }

    override suspend fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Unit = withContext(supabaseDispatcher) {
        supabaseClient.auth.signUpWith(
            provider = Email,
            redirectUrl = buildString {
                append(BuildConfig.SUPABASE_REDIRECT_SCHEME)
                append("://")
                append(BuildConfig.SUPABASE_REDIRECT_HOST)
                append("/")
                append(EMAIL_CONFIRM_URL_PATH)
            }
        ) {
            this.email = email
            this.password = password

            this.data = json.decodeFromString(
                json.encodeToString(
                    UserMetaData(
                        firstName = firstName,
                        lastName = lastName,
                    )
                )
            )
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

    companion object {
        const val RECOVERY_CONFIRM_URL_PATH = "recovery_confirm"
        const val EMAIL_CONFIRM_URL_PATH = "email_confirm"
    }
}
