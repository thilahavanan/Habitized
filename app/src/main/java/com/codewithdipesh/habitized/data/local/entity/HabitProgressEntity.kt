package com.codewithdipesh.habitized.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.codewithdipesh.habitized.domain.model.HabitType
import com.codewithdipesh.habitized.domain.model.Status
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@Entity(
    tableName = "habit_progress",
    foreignKeys = [
        ForeignKey(
            entity = HabitEntity::class,
            parentColumns = ["habit_id"],
            childColumns = ["habitId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HabitProgressEntity(
    @PrimaryKey val progressId: UUID = UUID.randomUUID(),
    val habitId: UUID,
    val date: LocalDate,  // Store as "YYYY-MM-DD"
    val type: String = HabitType.Count.displayName,
    val countParam: String,
    val currentCount: Int? = null,
    val targetCount: Int? = null,
    val targetDurationValue: String? = null,
    val currentSessionNumber: Int? = null,
    val targetSessionNumber: Int? = null,
    val status: String = Status.NotStarted.toString(),
    val notes: String? = null,
    val excuse: String? = null
)