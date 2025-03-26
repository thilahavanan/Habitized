package com.codewithdipesh.habitized.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.time.LocalTime
import java.util.Date
import java.util.UUID

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey val habit_id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String?= null,
    val type: String, //(Count, Duration, Percentage, Session)
    val goal_id: UUID,
    val start_date: Date,
    val frequency: String, // (Daily, Weekly, Monthly, Custom)
    val days_of_week: String, //"1,0,0,1,0,1,1"
    val daysOfMonth: String, //"1,15,30"
    val reminder_time: LocalTime,
    val is_active: Boolean,
    val icon: String,
    val color :String,
    val countParam : String,
    val countTarget:Int,
    val durationParam: String,
    val duration:Float,
)
