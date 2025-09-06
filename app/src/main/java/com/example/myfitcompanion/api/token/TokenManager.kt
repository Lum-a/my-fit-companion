package com.example.myfitcompanion.api.token

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.myfitcompanion.db.datastore.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
): TokenProvider {

    private val TOKEN_KEY = stringPreferencesKey("auth_token")

    @Volatile
    private var cachedToken: String? = null

    val authToken: Flow<String?> = context.dataStore.data
        .map { prefs -> prefs[TOKEN_KEY] }
        .onEach { token -> cachedToken = token }
        .stateIn(scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs -> prefs[TOKEN_KEY] = token }
    }

    suspend fun clearToken() {
        context.dataStore.edit { prefs -> prefs.remove(TOKEN_KEY) }
    }

    override fun getToken() = cachedToken

}