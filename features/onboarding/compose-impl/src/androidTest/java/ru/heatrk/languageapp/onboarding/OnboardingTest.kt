@file:OptIn(ExperimentalTestApi::class)

package ru.heatrk.languageapp.onboarding

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.testing.TestNavHostController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.junit.Rule
import org.junit.Test
import ru.heatrk.languageapp.auth.api.domain.AuthEvent
import ru.heatrk.languageapp.auth.api.domain.AuthRepository
import ru.heatrk.languageapp.auth.api.domain.User
import ru.heatrk.languageapp.auth.api.ui.navigation.AUTH_GRAPH_ROUTE_PATH
import ru.heatrk.languageapp.auth.impl.domain.google.AuthGoogleNonce
import ru.heatrk.languageapp.auth.impl.domain.google.AuthGoogleNonceProvider
import ru.heatrk.languageapp.auth.impl.domain.sign_in.SignInUseCase
import ru.heatrk.languageapp.auth.impl.domain.sign_in.SignInWithGoogleUseCase
import ru.heatrk.languageapp.auth.impl.ui.screens.sign_in.SignInScreen
import ru.heatrk.languageapp.auth.impl.ui.screens.sign_in.SignInViewModel
import ru.heatrk.languageapp.common.utils.currentRoute
import ru.heatrk.languageapp.common.utils.testTag
import ru.heatrk.languageapp.common.utils.vectorRes
import ru.heatrk.languageapp.core.design.composables.AppRootContainer
import ru.heatrk.languageapp.core.design.styles.AppTheme
import ru.heatrk.languageapp.core.navigation.compose_test.rememberTestComposeRouter
import ru.heatrk.languageapp.core.profiles.api.domain.Profile
import ru.heatrk.languageapp.core.profiles.api.domain.ProfilesRepository
import ru.heatrk.languageapp.onboarding.api.domain.OnboardingRepository
import ru.heatrk.languageapp.onboarding.api.domain.models.OnboardingUnit
import ru.heatrk.languageapp.onboarding.api.ui.navigation.ONBOARDING_SCREEN_ROUTE_PATH
import ru.heatrk.languageapp.onboarding.impl.R
import ru.heatrk.languageapp.onboarding.impl.ui.screens.onboarding.ONBOARDING_SHIMMER_TEST_TAG
import ru.heatrk.languageapp.onboarding.impl.ui.screens.onboarding.OnboardingScreen
import ru.heatrk.languageapp.onboarding.impl.ui.screens.onboarding.OnboardingViewModel

class OnboardingTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    lateinit var navController: TestNavHostController

    private val activity: Activity
        get() = composeTestRule.activity

    private fun setup(
        onboardingRepository: OnboardingRepository
    ) {
        composeTestRule.setContent {
            AppRootContainer(isDarkTheme = false) { _, _ ->
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())

                val composeRouter = rememberTestComposeRouter(navController)

                NavHost(
                    navController = navController,
                    startDestination = ONBOARDING_SCREEN_ROUTE_PATH,
                    builder = {
                        composable(ONBOARDING_SCREEN_ROUTE_PATH) {
                            OnboardingScreen(
                                viewModel = OnboardingViewModel(
                                    onboardingRepository = onboardingRepository,
                                    router = composeRouter
                                )
                            )
                        }

                        composable(AUTH_GRAPH_ROUTE_PATH) {
                            val authRepository = createAuthRepository()
                            val profilesRepository = createProfileRepository()

                            SignInScreen(
                                viewModel = SignInViewModel(
                                    signIn = SignInUseCase(
                                        authRepository = authRepository,
                                        profilesRepository = profilesRepository,
                                    ),
                                    signInWithGoogle = SignInWithGoogleUseCase(
                                        authRepository = authRepository,
                                        profilesRepository = profilesRepository,
                                    ),
                                    authGoogleNonceProvider = createAuthGoogleNonceProvider(),
                                    router = composeRouter
                                )
                            )
                        }
                    }
                )
            }
        }
    }

    @Test
    fun `onboarding queue test`() {
        setup(
            onboardingRepository = createOnboardingRepository(
                queue = listOf(
                    OnboardingUnit.CONVERSATION_BASED_LEARNING,
                    OnboardingUnit.ANYTIME_LEARNING,
                    OnboardingUnit.VARIETY_LEARNING,
                )
            )
        )

        composeTestRule.waitUntilDoesNotExist(hasTestTag(ONBOARDING_SHIMMER_TEST_TAG))

        composeTestRule
            .onNodeWithTag(vectorRes(R.drawable.onboarding_conversation_based_learning).testTag())
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(activity.getString(R.string.onboarding_conversation_based_learning_title))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(activity.getString(R.string.onboarding_conversation_based_learning_description))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(activity.getString(R.string.onboarding_next))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(activity.getString(R.string.onboarding_skip))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(activity.getString(R.string.onboarding_next))
            .performClick()

        composeTestRule.waitUntilDoesNotExist(hasTestTag(R.drawable.onboarding_conversation_based_learning.toString()))

        composeTestRule
            .onNodeWithTag(vectorRes(R.drawable.onboarding_anytime_learning).testTag())
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(activity.getString(R.string.onboarding_anytime_learning_title))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(activity.getString(R.string.onboarding_anytime_learning_description))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(activity.getString(R.string.onboarding_more))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(activity.getString(R.string.onboarding_skip))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(activity.getString(R.string.onboarding_more))
            .performClick()

        composeTestRule.waitUntilDoesNotExist(hasTestTag(R.drawable.onboarding_anytime_learning.toString()))

        composeTestRule
            .onNodeWithTag(vectorRes(R.drawable.onboarding_variety_learning).testTag())
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(activity.getString(R.string.onboarding_variety_learning_title))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(activity.getString(R.string.onboarding_variety_learning_description))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(activity.getString(R.string.onboarding_log_in))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(activity.getString(R.string.onboarding_skip))
            .assertIsDisplayed()
    }

    @Test
    fun `onboarding navigation to sign in test`() {
        setup(
            onboardingRepository = createOnboardingRepository(
                queue = listOf(
                    OnboardingUnit.VARIETY_LEARNING,
                )
            )
        )

        composeTestRule.waitUntilDoesNotExist(hasTestTag(ONBOARDING_SHIMMER_TEST_TAG))

        composeTestRule
            .onNodeWithText(activity.getString(R.string.onboarding_log_in))
            .assertIsDisplayed()
            .performClick()

        composeTestRule.waitUntil { AUTH_GRAPH_ROUTE_PATH == navController.currentRoute }
    }

    @Test
    fun `onboarding navigation skip test`() {
        setup(
            onboardingRepository = createOnboardingRepository(
                queue = listOf(
                    OnboardingUnit.CONVERSATION_BASED_LEARNING,
                    OnboardingUnit.ANYTIME_LEARNING,
                    OnboardingUnit.VARIETY_LEARNING,
                )
            )
        )

        composeTestRule.waitUntilDoesNotExist(hasTestTag(ONBOARDING_SHIMMER_TEST_TAG))

        composeTestRule
            .onNodeWithText(activity.getString(R.string.onboarding_skip))
            .assertIsDisplayed()
            .performClick()

        composeTestRule.waitUntil { AUTH_GRAPH_ROUTE_PATH == navController.currentRoute }
    }

    private fun createOnboardingRepository(
        queue: List<OnboardingUnit>
    ) = object : OnboardingRepository {
        override suspend fun getUnwatchedUnits() = queue
        override suspend fun saveWatchedUnit(unit: OnboardingUnit) = Unit
        override suspend fun saveWatchedUnits(units: List<OnboardingUnit>) = Unit
    }

    private fun createAuthRepository() = object : AuthRepository {
        override val authEvents: Flow<AuthEvent> = emptyFlow()

        private val userStub = User("", null, null, null)

        override suspend fun signIn(email: String, password: String) = userStub
        override suspend fun awaitInitialization() = Unit
        override suspend fun saveSession() = Unit
        override suspend fun hasSavedSession() = false
        override suspend fun resetPassword(email: String) = Unit
        override suspend fun applyRecoveryCode(code: String) = Unit
        override suspend fun changePassword(password: String) = Unit
        override suspend fun signOut() = Unit

        override suspend fun signInWithGoogle(
            idToken: String,
            email: String,
            firstName: String,
            lastName: String,
            rawNonce: String
        ) = userStub

        override suspend fun signUp(
            firstName: String,
            lastName: String,
            email: String,
            password: String
        ) = Unit
    }

    private fun createProfileRepository() = object : ProfilesRepository {
        private val profileStub =
            Profile("", null, null, null, 0, null)

        override suspend fun createProfile(profile: Profile) = Unit

        override suspend fun fetchCurrentProfile() = profileStub

        override suspend fun observeCurrentProfile(reload: Boolean): Flow<Profile> = emptyFlow()

        override suspend fun updateCurrentProfileAvatar(
            avatarBytes: ByteArray,
            extension: String
        ) = Unit

        override suspend fun getLeaderboard(count: Long) = emptyList<Profile>()
    }

    private fun createAuthGoogleNonceProvider() = object : AuthGoogleNonceProvider {
        override suspend fun provideNonce() = AuthGoogleNonce("", "")
    }
}