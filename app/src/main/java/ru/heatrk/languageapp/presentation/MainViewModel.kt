package ru.heatrk.languageapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _isInitializationFinished = MutableStateFlow(false)
    val isInitializationFinished = _isInitializationFinished.asStateFlow()

    init {
        viewModelScope.launch {
            delay(INITIALIZATION_DELAY_MILLIS)
            _isInitializationFinished.value = true
        }
    }

    companion object {
        private const val INITIALIZATION_DELAY_MILLIS = 2000L
    }
}
