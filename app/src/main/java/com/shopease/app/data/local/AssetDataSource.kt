package com.shopease.app.data.local

import android.content.Context
import com.shopease.app.domain.model.Category
import com.shopease.app.domain.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.IOException

/**
 * Reads the bundled product/category catalog from app/src/main/assets.
 * There's no backend for this project, so this plays the role a remote
 * API + response cache would normally play.
 */
class AssetDataSource(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun loadProducts(): List<Product> = withContext(Dispatchers.IO) {
        val raw = readAsset("products.json")
        json.decodeFromString(raw)
    }

    suspend fun loadCategories(): List<Category> = withContext(Dispatchers.IO) {
        val raw = readAsset("categories.json")
        json.decodeFromString(raw)
    }

    private fun readAsset(fileName: String): String {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            throw IllegalStateException("Could not read asset: $fileName", e)
        }
    }
}
