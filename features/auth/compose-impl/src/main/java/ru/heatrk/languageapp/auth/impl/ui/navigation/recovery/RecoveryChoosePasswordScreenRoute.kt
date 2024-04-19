package ru.heatrk.languageapp.auth.impl.ui.navigation.recovery

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import ru.heatrk.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowContract
import ru.heatrk.languageapp.auth.impl.ui.screens.recovery.RecoveryFlowViewModel
import ru.heatrk.languageapp.auth.impl.ui.screens.recovery.choose_password.RecoveryChoosePasswordScreen
import ru.heatrk.languageapp.core.navigation.compose_impl.Route

internal class RecoveryChoosePasswordScreenRoute(
    private val recoveryViewModelProvider: () -> RecoveryFlowViewModel,
) : Route.Screen(
    path = RECOVERY_CHOOSE_PASSWORD_SCREEN_ROUTE_PATH,
    namedNavArguments = listOf(
        navArgument(RecoveryChoosePasswordScreenArgs.RECOVERY_CODE) {
            type = NavType.StringType
        }
    )
) {
    @Composable
    override fun AnimatedContentScope.Content(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry
    ) {
        val recoveryCode = navBackStackEntry
            .arguments?.getString(RecoveryChoosePasswordScreenArgs.RECOVERY_CODE)

        RecoveryChoosePasswordScreen(
            viewModel = recoveryViewModelProvider().apply {
                LaunchedEffect(recoveryCode) {
                    processIntent(RecoveryFlowContract.Intent.OnRecoveryCodeReceived(
                        code = recoveryCode
                    ))
                }
            }
        )
    }
}

internal const val RECOVERY_CHOOSE_PASSWORD_SCREEN_ROUTE_PATH = "recovery_choose_password"

internal object RecoveryChoosePasswordScreenArgs {
    const val RECOVERY_CODE = "recovery_code"
}
