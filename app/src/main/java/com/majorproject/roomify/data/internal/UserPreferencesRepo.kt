package com.majorproject.roomify.data.internal

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


class UserPreferencesRepo(private val context: Context) {
    companion object {
        private val Context.dataStore by preferencesDataStore(name = "user_preferences")
        val USER_NICKNAME = stringPreferencesKey("user_nickname")
        val USER_ID_TOKEN = stringPreferencesKey("user_id_token")


    }
    private val dataStore = context.dataStore

    suspend fun saveNickname(nickname: String): Boolean {
        return try {
            dataStore.edit { preferences ->
                preferences[USER_NICKNAME] = nickname
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    val userNickname: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[USER_NICKNAME] ?: "User"
        }
    // Save the user ID token to the dataStore
    suspend fun saveUserIdToken(userIdToken: String): Boolean {
        return try {
            dataStore.edit { preferences ->
                preferences[USER_ID_TOKEN] = userIdToken
            }
            true // Successfully saved
        } catch (e: Exception) {
            false // Failed to save
        }
    }

    // Retrieve the user ID token from the dataStore
    val userIdToken: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[USER_ID_TOKEN]
        }

}
