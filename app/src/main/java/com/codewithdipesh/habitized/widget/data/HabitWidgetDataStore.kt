package com.codewithdipesh.habitized.widget.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.withContext
import java.util.UUID

private val Context.widgetDataStore by preferencesDataStore(
    name = "widget_data_store"
)
object HabitWidgetDataStore {

    //get key for widget
    private fun widgetHabitIdKey(appWidgetId: Int): Preferences.Key<String> =
        stringPreferencesKey("habit_id_$appWidgetId")

    // SAVE
    suspend fun saveHabitIdForWidget(context: Context, widgetId: Int, habitId: UUID) {
        withContext(Dispatchers.IO){
            Log.d("preference","$widgetId -> $habitId")
            context.widgetDataStore.edit { preferences ->
                preferences[widgetHabitIdKey(widgetId)] = habitId.toString()
            }
        }
    }

    suspend fun getHabitIdForWidget(context: Context, widgetId: Int): UUID? {
        Log.d("preference","Getting habit for widget: $widgetId")
        return withContext(Dispatchers.IO) {
            try {
                val habitIdString = context.widgetDataStore.data.first()[widgetHabitIdKey(widgetId)]
                if (habitIdString != null) {
                    UUID.fromString(habitIdString)
                } else {
                    Log.w("preference", "No habit ID found for widget: $widgetId")
                    null
                }
            } catch (e: Exception) {
                Log.e("preference", "Error getting habit ID for widget $widgetId: ${e.message}")
                null
            }
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