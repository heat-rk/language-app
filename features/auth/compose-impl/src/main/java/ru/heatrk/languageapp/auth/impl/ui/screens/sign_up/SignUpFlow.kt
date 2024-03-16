package ru.heatrk.languageapp.auth.impl.ui.screens.sign_up

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.heatrk.languageapp.auth.impl.R
import ru.heatrk.languageapp.auth.impl.di.AuthComponent
import ru.heatrk.languageapp.auth.impl.ui.navigation.sign_up.SignUpGraphRoute
import ru.heatrk.languageapp.auth.impl.ui.screens.sign_up.SignUpScreenContract.Intent
import ru.heatrk.languageapp.auth.impl.ui.screens.sign_up.SignUpScreenContract.SideEffect
import ru.heatrk.languageapp.auth.impl.ui.screens.sign_up.SignUpScreenContract.State
import ru.heatrk.languageapp.common.utils.extract
import ru.heatrk.languageapp.core.design.composables.AppBarTitleGravity
import ru.heatrk.languageapp.core.design.composables.AppLinkedText
import ru.heatrk.languageapp.core.design.composables.AppLinkedTextUnit
import ru.heatrk.languageapp.core.design.composables.button.AppButton
import ru.heatrk.languageapp.core.design.composables.button.AppButtonState
import ru.heatrk.languageapp.core.design.composables.scaffold.AppBarState
import ru.heatrk.languageapp.core.design.composables.scaffold.AppScaffoldControllerEffect
import ru.heatrk.languageapp.core.design.composables.scaffold.LocalAppScaffoldController
import ru.heatrk.languageapp.core.design.styles.AppTheme
import ru.heatrk.languageapp.core.navigation.api.NavHost
import ru.heatrk.languageapp.core.navigation.compose_impl.ComposeRouter

@Composable
fun SignUpFlow(viewModel: SignUpViewModel) {
    val appBarTitle = stringResource(R.string.signup)
    val navController = rememberNavController()
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    val buttonsController = rememberSignUpButtonsController()

    SignUpScreenSideEffects(
        sideEffects = viewModel.container.sideEffectFlow
    )

    AppScaffoldControllerEffect(
        appBarState = AppBarState.Shown(
            title = appBarTitle,
            titleGravity = AppBarTitleGravity.CENTER,
            onGoBackClick = { viewModel.processIntent(Intent.OnGoBackClick) },
        )
    )

    Column(
        modifier = Modifier
            .padding(vertical = 40.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.weight(1f))

        CompositionLocalProvider(LocalSignUpButtonsController provides buttonsController) {
            NavHost(
                navController = navController,
                graph = SignUpGraphRoute(
                    signUpViewModelProvider = { viewModel }
                ),
            )
        }

        Spacer(modifier = Modifier.height(34.dp))

        Spacer(modifier = Modifier.weight(1f))

        SignUpButtons(
            state = state,
            buttonsState = buttonsController,
            onIntent = viewModel::processIntent
        )
    }

    SignUpLaunchedRouterEffect(navController = navController)
}

@Composable
private fun ColumnScope.SignUpButtons(
    state: State,
    buttonsState: SignUpButtonsController,
    onIntent: (Intent) -> Unit,
) {
    AppButton(
        text = buttonsState.text,
        buttonState = state.registrationState.toButtonState(),
        onClick = buttonsState.onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    )

    if (buttonsState.isLoginButtonVisible) {
        Spacer(modifier = Modifier.height(24.dp))

        AppLinkedText(
            onClick = { onIntent(Intent.OnSignInButtonClick) },
            units = listOf(
                AppLinkedTextUnit(
                    text = stringResource(R.string.signup_already_you_member_before_annotation),
                ),
                AppLinkedTextUnit(
                    text = stringResource(R.string.signup_already_you_member_after_annotation),
                    linkTag = "sign_up",
                )
            ),
            textSpanStyle = SpanStyle(
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = AppTheme.colors.textBody,
            ),
            linkSpanStyle = SpanStyle(
                color = AppTheme.colors.secondary,
            ),
            isEnabled = state.registrationState == State.Registration.None,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
internal fun ColumnScope.SignUpTitle(
    @StringRes titleRes: Int
) {
    Text(
        text = stringResource(titleRes),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Medium,
        style = AppTheme.typography.titleLarge,
        modifier = Modifier
            .padding(horizontal = 56.dp)
            .align(Alignment.CenterHorizontally)
    )
}

@Composable
private fun SignUpLaunchedRouterEffect(
    navController: NavController,
    router: ComposeRouter = AuthComponent.signUpComposeRouter,
) {
    LaunchedEffect(navController, router) {
        router.attachNavController(
            navController = navController,
            coroutineScope = this
        )
    }
}

@Composable
private fun SignUpScreenSideEffects(
    sideEffects: Flow<SideEffect>,
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = LocalAppScaffoldController.current.snackbarHostState

    LaunchedEffect(sideEffects, context) {
        sideEffects
            .onEach { sideEffect ->
                when (sideEffect) {
                    is SideEffect.Message -> {
                        handleMessageSideEffect(
                            sideEffect = sideEffect,
                            snackbarHostState = snackbarHostState,
                            context = context,
                        )
                    }

                    is SideEffect.CloseKeyboard -> {
                        keyboardController?.hide()
                    }
                }
            }
            .launchIn(this)
    }
}

private suspend fun handleMessageSideEffect(
    sideEffect: SideEffect.Message,
    snackbarHostState: SnackbarHostState,
    context: Context,
) {
    val message = sideEffect.text.extract(context)
        ?: return

    snackbarHostState.showSnackbar(message)
}

private fun State.Registration.toButtonState() = when (this) {
    State.Registration.None -> AppButtonState.Idle
    State.Registration.InProgress -> AppButtonState.Loading
    State.Registration.Success -> AppButtonState.Success
    State.Registration.Error -> AppButtonState.Error
}

internal class SignUpButtonsController {
    var text by mutableStateOf("")
    var onClick by mutableStateOf({})
    var isLoginButtonVisible by mutableStateOf(false)
}

internal val LocalSignUpButtonsController =
    staticCompositionLocalOf { SignUpButtonsController() }

@Composable
internal fun rememberSignUpButtonsController() =
    remember { SignUpButtonsController() }

@Composable
internal fun SignUpButtonsControllerEffect(
    text: String,
    isLoginButtonVisible: Boolean,
    onClick: () -> Unit,
) {
    val signUpButtonsController = LocalSignUpButtonsController.current

    LaunchedEffect(signUpButtonsController) {
        signUpButtonsController.text = text
        signUpButtonsController.isLoginButtonVisible = isLoginButtonVisible
        signUpButtonsController.onClick = onClick
    }
}

//class SignUpStatePreviewParameterProvider : PreviewParameterProvider<State> {
//    override val values = sequenceOf(
//        State.InputData(inputDataState = State.InputData.State.GENERAL),
//        State.InputData(inputDataState = State.InputData.State.PASSWORD),
//        State.EmailConfirmation,
//    )
//}
//
//@Composable
//private fun SignUpScreenPreview(
//    state: State
//) {
//    AppRootContainer {
//        SignUpScreen(
//            state = state,
//            onIntent = {}
//        )
//    }
//}
//
//@Composable
//@Preview(
//    showBackground = true,
//)
//private fun SignUpScreenPreviewLight(
//    @PreviewParameter(SignUpStatePreviewParameterProvider::class)
//    state: State
//) {
//    SignUpScreenPreview(state = state)
//}
//
//@Composable
//@Preview(
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//)
//private fun SignUpScreenPreviewDark(
//    @PreviewParameter(SignUpStatePreviewParameterProvider::class)
//    state: State
//) {
//    SignUpScreenPreview(state = state)
//}
