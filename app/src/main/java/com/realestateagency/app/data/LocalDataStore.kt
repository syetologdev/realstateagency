package com.realestateagency.app.data

import android.content.Context
import android.content.SharedPreferences
import com.realestateagency.app.models.LocalFavorite
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LocalDataStore(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences(
        "realestateagency_prefs",
        Context.MODE_PRIVATE
    )

    private val json = Json

    fun saveFavorites(favorites: Set<Int>) {
        preferences.edit().apply {
            putString("favorites", json.encodeToString(favorites))
        }.apply()
    }

    fun getFavorites(): Set<Int> {
        return try {
            val data = preferences.getString("favorites", "[]") ?: "[]"
            json.decodeFromString(data)
        } catch (e: Exception) {
            emptySet()
        }
    }

    fun clearFavorites() {
        preferences.edit().remove("favorites").apply()
    }

    fun saveLastViewedPropertyId(propertyId: Int) {
        preferences.edit().putInt("last_viewed_property", propertyId).apply()
    }

    fun getLastViewedPropertyId(): Int {
        return preferences.getInt("last_viewed_property", -1)
    }

    fun setUserName(name: String) {
        preferences.edit().putString("user_name", name).apply()
    }

    fun getUserName(): String {
        return preferences.getString("user_name", "") ?: ""
    }
}