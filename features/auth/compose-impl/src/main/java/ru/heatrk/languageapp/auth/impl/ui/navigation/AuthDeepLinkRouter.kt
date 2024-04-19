package ru.heatrk.languageapp.auth.impl.ui.navigation

import io.ktor.http.Url
import ru.heatrk.languageapp.auth.impl.data.AuthRepositoryImpl
import ru.heatrk.languageapp.auth.impl.ui.navigation.recovery.RECOVERY_CHOOSE_PASSWORD_SCREEN_ROUTE_PATH
import ru.heatrk.languageapp.auth.impl.ui.navigation.recovery.RECOVERY_ENTER_EMAIL_SCREEN_ROUTE_PATH
import ru.heatrk.languageapp.auth.impl.ui.navigation.recovery.RECOVERY_FLOW_ROUTE_PATH
import ru.heatrk.languageapp.auth.impl.ui.navigation.recovery.RecoveryChoosePasswordScreenArgs
import ru.heatrk.languageapp.auth.impl.ui.navigation.sign_in.SIGN_IN_SCREEN_ROUTE_PATH
import ru.heatrk.languageapp.core.navigation.api.DeepLinkRouter
import ru.heatrk.languageapp.core.navigation.api.Router
import ru.heatrk.languageapp.core.navigation.api.RoutingOption

internal class AuthDeepLinkRouter(
    private val router: Router,
    private val recoveryRouter: Router,
) : DeepLinkRouter {
    override suspend fun handle(data: Url): Boolean {
        when {
            isEmailConfirmDeepLink(data) -> {
                navigateToSignIn()
                return true
            }
            isRecoveryConfirmDeepLink(data) -> {
                val params = parseParams(data.encodedQuery)

                val recoveryCode = params.getOrDefault(CODE_QUERY_KEY, "")

                if (recoveryRouter.currentRoute == null) {
                    navigateToRecoveryFlow()
                    navigateToChoosePasswordScreen(
                        recoveryCode = recoveryCode,
                        skipOtherScreens = true
                    )
                } else {
                    navigateToChoosePasswordScreen(
                        recoveryCode = recoveryCode,
                    )
                }

                return true
            }
        }

        return false
    }

    private suspend fun navigateToSignIn() {
        router.navigate(
            routePath = SIGN_IN_SCREEN_ROUTE_PATH,
            options = listOf(
                RoutingOption.PopUpTo(
                    routePath = SIGN_IN_SCREEN_ROUTE_PATH,
                    inclusive = false,
                ),
                RoutingOption.LaunchSingleTop(true),
            )
        )
    }

    private suspend fun navigateToRecoveryFlow() {
        router.navigate(routePath = RECOVERY_FLOW_ROUTE_PATH)
    }

    private suspend fun navigateToChoosePasswordScreen(
        recoveryCode: String,
        skipOtherScreens: Boolean = false
    ) {
        recoveryRouter.navigate(
            routePath = RECOVERY_CHOOSE_PASSWORD_SCREEN_ROUTE_PATH,
            options = listOf(
                RoutingOption.PopUpTo(
                    routePath = RECOVERY_ENTER_EMAIL_SCREEN_ROUTE_PATH,
                    inclusive = skipOtherScreens,
                ),
                RoutingOption.LaunchSingleTop(true),
            ),
            arguments = mapOf(
                RecoveryChoosePasswordScreenArgs.RECOVERY_CODE to recoveryCode
            )
        )
    }

    private fun parseParams(query: String?): Map<String, String> {
        val queries = query?.split('&') ?: return emptyMap()
        val params = mutableMapOf<String, String>()

        queries.forEach { queryPairStr ->
            val keyValue = queryPairStr.split('=')
            if (keyValue.size == 2) {
                params[keyValue.first()] = keyValue.last()
            }
        }

        return params
    }

    private fun isEmailConfirmDeepLink(data: Url) =
        data.pathSegments.contains(AuthRepositoryImpl.EMAIL_CONFIRM_URL_PATH)

    private fun isRecoveryConfirmDeepLink(data: Url) =
        data.pathSegments.contains(AuthRepositoryImpl.RECOVERY_CONFIRM_URL_PATH)

    companion object {
        private const val CODE_QUERY_KEY = "code"
    }
}
