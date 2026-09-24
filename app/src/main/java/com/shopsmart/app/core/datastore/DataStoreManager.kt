package com.shopsmart.app.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "shopeasy_prefs")

class DataStoreManager(private val context: Context) {

    companion object {
        // ----- Auth -----
        val TOKEN_KEY = stringPreferencesKey("auth_token")
        val USER_ID_KEY = stringPreferencesKey("user_id")
        val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        val USER_NAME_KEY = stringPreferencesKey("user_name")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")

        // ----- Settings -----
        val DARK_MODE = booleanPreferencesKey("settings_dark_mode")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("settings_notifications_enabled")
        val LANGUAGE = stringPreferencesKey("settings_language")   // 👈 fixed: added )
    }

    // ---------- REACTIVE FLOWS (for UI observation) ----------
    val token: Flow<String?> = context.dataStore.data.map { it[TOKEN_KEY] }
    val userId: Flow<String?> = context.dataStore.data.map { it[USER_ID_KEY] }
    val userEmail: Flow<String?> = context.dataStore.data.map { it[USER_EMAIL_KEY] }
    val userName: Flow<String?> = context.dataStore.data.map { it[USER_NAME_KEY] }

    val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data.map {
        it[ONBOARDING_COMPLETED] ?: false
    }   // 👈 fixed: closed the map lambda here

    // 👇 moved out of isOnboardingCompleted — they are now top-level class properties
    val darkMode: Flow<Boolean> = context.dataStore.data.map {
        it[DARK_MODE] ?: false
    }
    val notificationsEnabled: Flow<Boolean> = context.dataStore.data.map {
        it[NOTIFICATIONS_ENABLED] ?: true
    }
    val language: Flow<String> = context.dataStore.data.map {
        it[LANGUAGE] ?: "en"
    }

    // ---------- ONE-TIME READ HELPERS (for Splash / UseCases) ----------
    suspend fun getToken(): String? = token.firstOrNull()
    suspend fun getUserId(): String? = userId.firstOrNull()
    suspend fun getUserEmail(): String? = userEmail.firstOrNull()
    suspend fun getUserName(): String? = userName.firstOrNull()
    suspend fun isOnboardingComplete(): Boolean = isOnboardingCompleted.firstOrNull() ?: false

    // ---------- AUTH WRITERS ----------
    suspend fun saveAuthData(token: String, userId: String, email: String, name: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            prefs[USER_ID_KEY] = userId
            prefs[USER_EMAIL_KEY] = email
            prefs[USER_NAME_KEY] = name
        }
    }

    suspend fun updateUserProfile(email: String, name: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_EMAIL_KEY] = email
            prefs[USER_NAME_KEY] = name
        }
    }

    suspend fun clearAuthData() {
        context.dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
            prefs.remove(USER_ID_KEY)
            prefs.remove(USER_EMAIL_KEY)
            prefs.remove(USER_NAME_KEY)
        }
    }

    suspend fun setOnboardingCompleted() {
        context.dataStore.edit { prefs ->
            prefs[ONBOARDING_COMPLETED] = true
        }
    }

    suspend fun clearAllData() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }

    // ---------- SETTINGS WRITERS ----------
    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[DARK_MODE] = enabled }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { it[NOTIFICATIONS_ENABLED] = enabled }
    }

    suspend fun setLanguage(lang: String) {
        context.dataStore.edit { it[LANGUAGE] = lang }
    }
}