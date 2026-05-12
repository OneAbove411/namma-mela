package com.paraminnovation.nammamela.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.security.MessageDigest

private val Context.dataStore by preferencesDataStore(name = "namma_mela_settings")

class SettingsRepository(private val context: Context) {

    companion object {
        private val KEY_PIN_HASH = stringPreferencesKey("manager_pin_hash")
        private val KEY_USER_NAME = stringPreferencesKey("user_name")
        private val KEY_GEMINI_KEY = stringPreferencesKey("gemini_api_key")
        private val KEY_RAZORPAY_KEY = stringPreferencesKey("razorpay_test_key")
        const val DEFAULT_PIN = "1234"
    }

    val userName: Flow<String?> = context.dataStore.data.map { it[KEY_USER_NAME] }
    val geminiKey: Flow<String?> = context.dataStore.data.map { it[KEY_GEMINI_KEY] }
    val razorpayKey: Flow<String?> = context.dataStore.data.map { it[KEY_RAZORPAY_KEY] }

    suspend fun setUserName(name: String) {
        context.dataStore.edit { it[KEY_USER_NAME] = name }
    }

    suspend fun setGeminiKey(key: String) {
        context.dataStore.edit { it[KEY_GEMINI_KEY] = key }
    }

    suspend fun setRazorpayKey(key: String) {
        context.dataStore.edit { it[KEY_RAZORPAY_KEY] = key }
    }

    /**
     * On first run there is no stored PIN — fall back to DEFAULT_PIN.
     * After the manager sets a new PIN, only that one works.
     */
    suspend fun verifyPin(input: String): Boolean {
        val stored = context.dataStore.data.first()[KEY_PIN_HASH]
        return if (stored == null) input == DEFAULT_PIN
        else stored == sha256(input)
    }

    suspend fun setPin(newPin: String) {
        context.dataStore.edit { it[KEY_PIN_HASH] = sha256(newPin) }
    }

    private fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
