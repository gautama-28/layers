package com.shopease.app.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Base class for all feature ViewModels following unidirectional data flow (MVI):
 *
 *   View --(Event)--> ViewModel --(reduces to)--> State --> View re-renders
 *                                   |
 *                                   +--(Effect)--> View (one-shot: navigation, snackbar, etc.)
 *
 * State is exposed as StateFlow so the UI always has a current value to render.
 * Effects are exposed through a Channel so they're consumed exactly once.
 */
abstract class BaseViewModel<State : UiState, Event : UiEvent, Effect : UiEffect>(
    initialState: State
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    val currentState: State
        get() = _uiState.value

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    /**
     * Entry point for the UI to dispatch a user intent.
     */
    fun setEvent(event: Event) {
        handleEvent(event)
    }

    /**
     * Each feature ViewModel implements how an Event mutates state / triggers effects.
     */
    protected abstract fun handleEvent(event: Event)

    /**
     * Convenience for reducer-style state updates.
     */
    protected fun setState(reducer: State.() -> State) {
        _uiState.value = currentState.reducer()
    }

    /**
     * Convenience for firing a one-shot side effect.
     */
    protected fun setEffect(builder: () -> Effect) {
        viewModelScope.launch {
            _effect.send(builder())
        }
    }
}
