package com.codewithdipesh.habitized.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@Entity(tableName = "one_time_tasks")
data class OneTimeTaskEntity(
    @PrimaryKey val taskId: UUID=UUID.randomUUID(),
    val title :String,
    val isCompleted : Boolean = false,
    val date : LocalDate,
    val startTime :LocalTime ,
    val finishTime :LocalTime ,
)
