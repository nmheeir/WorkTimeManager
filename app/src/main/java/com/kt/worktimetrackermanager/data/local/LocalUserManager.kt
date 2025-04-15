package com.kt.worktimetrackermanager.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.messaging.FirebaseMessaging
import com.kt.worktimetrackermanager.core.presentation.utils.TokenKey
import com.kt.worktimetrackermanager.core.presentation.utils.dataStore
import com.kt.worktimetrackermanager.core.presentation.utils.get
import com.kt.worktimetrackermanager.core.presentation.utils.set
import com.kt.worktimetrackermanager.data.remote.dto.response.Token
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "WorkTimeManager_dataStore")

class LocalUserManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val appContext = context.applicationContext

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val ROLE = stringPreferencesKey("role")
        private val DEVICE_TOKEN = stringPreferencesKey("device_token")
    }

    suspend fun saveDeviceToken() {
        appContext.dataStore.edit {
            it[DEVICE_TOKEN] = FirebaseMessaging.getInstance().token.await()
        }
    }

    suspend fun readDeviceToken(): String? {
        return appContext.dataStore.data.map { preferences ->
            preferences[TokenKey]
        }.first()
    }
    fun saveAccessToken(token: String) {
        appContext.dataStore.set(TokenKey, token)

    }

    fun readAccessToken(): String {
        return appContext.dataStore[TokenKey] ?: ""
    }

    suspend fun updateRole(role: String) {
        appContext.dataStore.edit {
            it[ROLE] = role
        }
    }

    suspend fun getRole(): String {
        return appContext.dataStore.data.map {
            it[ROLE] ?: ""
        }.first()
    }

    suspend fun clear() {
        appContext.dataStore.edit {
            it[ACCESS_TOKEN] = ""
            it[ROLE] = ""
        }
    }
}