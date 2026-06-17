package com.shopease.app.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val isLoggedIn: Flow<Boolean>
    val userId: Flow<String?>
    suspend fun login(userId: String)
    suspend fun logout()
}
