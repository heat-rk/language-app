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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext
import org.junit.Rule
import org.junit.Test
import ru.heatrk.languageapp.common.utils.testTag
import ru.heatrk.languageapp.common.utils.vectorRes
import ru.heatrk.languageapp.core.design.styles.AppTheme
import ru.heatrk.languageapp.core.navigation.api.Route
import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.core.navigation.api.RoutingAction
import ru.heatrk.languageapp.core.navigation.compose_test.TestComposeRouter
import ru.heatrk.languageapp.login.api.LOGIN_SCREEN_TEST_TAG
import ru.heatrk.languageapp.login.api.LoginScreenRoute
import ru.heatrk.languageapp.login.impl.ui.LoginScreen
import ru.heatrk.languageapp.login.impl.ui.LoginViewModel
import ru.heatrk.languageapp.onboarding.api.domain.OnboardingRepository
import ru.heatrk.languageapp.onboarding.api.domain.models.OnboardingUnit
import ru.heatrk.languageapp.onboarding.api.ui.navigation.ONBOARDING_SCREEN_TEST_TAG
import ru.heatrk.languageapp.onboarding.impl.R
import ru.heatrk.languageapp.onboarding.impl.ui.ONBOARDING_SHIMMER_TEST_TAG
import ru.heatrk.languageapp.onboarding.impl.ui.OnboardingScreen
import ru.heatrk.languageapp.onboarding.impl.ui.OnboardingViewModel

class OnboardingTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    lateinit var navController: TestNavHostController

    private val activity: Activity
        get() = composeTestRule.activity

    private val composeRouter = TestComposeRouter(
        onNavigate = { route ->
            when (route) {
                is LoginScreenRoute -> navController.navigate(LOGIN_SCREEN_DESTINATION)
                else -> Unit
            }
        },
        onNavigateBack = {
            navController.popBackStack()
        }
    )

    private fun setup(
        onboardingRepository: OnboardingRepository
    ) {
        composeTestRule.setContent {
            AppTheme {
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())

                NavHost(
                    navController = navController,
                    startDestination = ONBOARDING_SCREEN_DESTINATION,
                    builder = {
                        composable(ONBOARDING_SCREEN_DESTINATION) {
                            OnboardingScreen(
                                viewModel = OnboardingViewModel(
                                    onboardingRepository = onboardingRepository,
                                    router = composeRouter
                                )
                            )
                        }

                        composable(LOGIN_SCREEN_DESTINATION) {
                            LoginScreen(
                                viewModel = LoginViewModel()
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
    fun `onboarding navigation to login test`() {
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

        composeTestRule
            .onNodeWithTag(LOGIN_SCREEN_TEST_TAG)
            .assertIsDisplayed()
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

        composeTestRule.waitUntilDoesNotExist(hasTestTag(ONBOARDING_SCREEN_TEST_TAG))

        composeTestRule
            .onNodeWithTag(LOGIN_SCREEN_TEST_TAG)
            .assertIsDisplayed()
    }

    private fun createOnboardingRepository(
        queue: List<OnboardingUnit>
    ) = object : OnboardingRepository {
        override suspend fun getUnwatchedUnits() = queue
        override suspend fun saveWatchedUnit(unit: OnboardingUnit) = Unit
    }

    companion object {
        private const val ONBOARDING_SCREEN_DESTINATION = "onboarding"
        private const val LOGIN_SCREEN_DESTINATION = "login"
    }
}