package com.codewithdipesh.habitized.data.sharedPref

import android.content.Context
import com.codewithdipesh.habitized.presentation.timerscreen.durationScreen.Theme
import androidx.core.content.edit

class HabitPreference(context : Context){
    private val sharedPreferences = context.getSharedPreferences("habit_preferences", Context.MODE_PRIVATE)
    private val key = "THEME"

    fun getTheme(default : String = Theme.Normal.displayName) : String {
        val theme = sharedPreferences.getString(key,default)
        return if(theme != null){
             theme
        }else {
             default
        }
    }

    fun updateTheme(theme:String){
        sharedPreferences.edit() { putString(key, theme) }
    }
}