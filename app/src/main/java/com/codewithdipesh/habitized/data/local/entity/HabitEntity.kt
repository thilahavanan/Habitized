package com.codewithdipesh.habitized.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.domain.model.HabitType
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date
import java.util.UUID

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey val  habit_id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String?= null,
    val type: String = HabitType.Count.toString(), //(Count, Duration, Percentage, Session)
    val goal_id: UUID? = null,
    val start_date: LocalDate,
    val frequency: String = Frequency.Daily.toString(), // (Daily, Weekly, Monthly, Custom)
    val days_of_week: String, //"1,0,0,1,0,1,1"
    val daysOfMonth: String?, //"1,15,30"
    val reminderType: String? = null,
    val reminderFrom: LocalTime?,
    val reminderTo: LocalTime?,
    val reminderInterval: Int?,
    val reminder_time: LocalTime? = null,
    val is_active: Boolean,
    val colorKey :String,
    val countParam : String,
    val countTarget:Int? = null,
    val duration:String? = null,
    val currentStreak : Int = 0,
    val maxStreak : Int = 0
)
