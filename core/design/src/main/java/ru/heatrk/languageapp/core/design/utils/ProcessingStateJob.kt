package ru.heatrk.languageapp.core.design.utils

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.heatrk.languageapp.common.utils.states.ProcessingState

suspend fun ProcessingState.withReturnToNone(
    processingDelay: Long = 1000L,
    onState: suspend (ProcessingState) -> Unit
) {
    coroutineScope {
        launch {
            onState(this@withReturnToNone)
            delay(processingDelay)
            onState(ProcessingState.None)
        }
    }
}

suspend fun ProcessingState.withReturnToNone(
    processingDelay: Long = 1000L,
    onStartState: suspend (ProcessingState) -> Unit,
    onEndState: suspend  (ProcessingState) -> Unit,
) {
    coroutineScope {
        launch {
            onStartState(this@withReturnToNone)
            delay(processingDelay)
            onEndState(ProcessingState.None)
        }
    }
}
