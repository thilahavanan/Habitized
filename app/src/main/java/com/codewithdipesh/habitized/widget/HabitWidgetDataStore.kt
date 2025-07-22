package com.codewithdipesh.habitized.widget

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.withContext

private val Context.widgetDataStore by preferencesDataStore(
    name = "widget_data_store"
)
object HabitWidgetDataStore {

    //get key for widget
    private fun widgetHabitIdKey(appWidgetId: Int): Preferences.Key<Long> =
        longPreferencesKey("habit_id_$appWidgetId")

    // SAVE
    suspend fun saveHabitIdForWidget(context: Context, widgetId: Int, habitId: Long) {
        withContext(Dispatchers.IO){
            context.widgetDataStore.edit { preferences ->
                preferences[widgetHabitIdKey(widgetId)] = habitId
            }
        }
    }

    // GET
    suspend fun getHabitIdForWidget(context: Context, widgetId: Int) : Long?{
        return withContext(Dispatchers.IO) {
            context.widgetDataStore.data.first()[widgetHabitIdKey(widgetId)]
        }
    }

    // REMOVE:
    suspend fun removeWidgetPreference(context: Context, widgetId: Int) {
        withContext(Dispatchers.IO) {
            context.widgetDataStore.edit {
                it.remove(widgetHabitIdKey(widgetId))
            }
        }
    }
}