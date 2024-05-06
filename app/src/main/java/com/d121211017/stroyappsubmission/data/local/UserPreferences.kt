package com.d121211017.stroyappsubmission.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.datastore : DataStore<Preferences> by preferencesDataStore(name = "token")
class UserPreferences private constructor(private val dataStore : DataStore<Preferences>) {

    private val userToken = stringPreferencesKey("user_token")
    private val userName = stringPreferencesKey("user_name")
    private val userId = stringPreferencesKey("user_id")

    suspend fun saveUserData(userToken : String, userName: String, userId : String) {
        dataStore.edit {
            it[this.userToken] = userToken
            it[this.userName] = userName
            it[this.userId] = userId
        }
    }

    fun getUserToken() : Flow<String> {
        return dataStore.data.map {
            it[userToken] ?: ""
        }
    }

    suspend fun clearUserSession(){
        dataStore.edit {
            it[this.userToken] = ""
            it[this.userName] = ""
            it[this.userId] = ""
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