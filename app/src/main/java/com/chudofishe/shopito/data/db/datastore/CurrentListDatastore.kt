package com.chudofishe.shopito.data.db.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CurrentListDatastore(
    private val context: Context
) {

    companion object {
        private const val CURRENT_LIST_PREF = "current_list_pref"
        private val CURRENT_LIST_ID = longPreferencesKey("current_list_id")
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = CURRENT_LIST_PREF)

    fun getCurrentListId(): Flow<Long?> = context.dataStore.data.map {
        val id = it[CURRENT_LIST_ID]
        if (id == -1L) null else id
    }

    suspend fun setCurrentListId(id: Long?) {
        context.dataStore.edit {
            it[CURRENT_LIST_ID] = id ?: -1
        }
    }
}