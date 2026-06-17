package com.shopease.app.data.repository

import com.shopease.app.data.local.datastore.UserPreferencesDataStore
import com.shopease.app.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow

class UserPreferencesRepositoryImpl(
    private val dataStore: UserPreferencesDataStore
) : UserPreferencesRepository {

    override val isLoggedIn: Flow<Boolean> = dataStore.isLoggedIn
    override val userId: Flow<String?> = dataStore.userId

    override suspend fun login(userId: String) {
        dataStore.setLoggedIn(userId)
    }

    override suspend fun logout() {
        dataStore.clearSession()
    }
}
