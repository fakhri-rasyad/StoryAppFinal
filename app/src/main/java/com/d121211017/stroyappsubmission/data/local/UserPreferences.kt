package com.d121211017.stroyappsubmission.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.d121211017.stroyappsubmission.data.remote.entity.LoginResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.datastore : DataStore<Preferences> by preferencesDataStore(name = "token")
class UserPreferences private constructor(private val dataStore : DataStore<Preferences>) {

    private val userToken = stringPreferencesKey("user_token")
    private val userName = stringPreferencesKey("user_name")
    private val userId = stringPreferencesKey("user_id")

    suspend fun saveUserData(userData : LoginResult) {
        dataStore.edit {
            it[this.userToken] = userData.token.toString()
            it[this.userName] = userData.name.toString()
            it[this.userId] = userData.userId.toString()
        }
    }

    fun getUserToken() : Flow<String> {
        return dataStore.data.map {
            it[userToken] ?: ""
        }
    }

    suspend fun clearUserSession(){
        dataStore.edit {
            it.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}