package com.shopease.app.ui.base

/**
 * Marker interface for a screen's immutable UI state.
 * Every feature defines a data class implementing this, e.g. ProductListState.
 */
interface UiState

/**
 * Marker interface for user-triggered intents / events coming from the UI.
 * Every feature defines a sealed interface implementing this, e.g. ProductListEvent.
 */
interface UiEvent

/**
 * Marker interface for one-off side effects the UI should react to once
 * (navigation, snackbars, toasts) — as opposed to persistent state.
 */
interface UiEffect
