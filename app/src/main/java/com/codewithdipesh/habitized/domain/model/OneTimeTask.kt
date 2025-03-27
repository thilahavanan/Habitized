package com.codewithdipesh.habitized.domain.model

import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date
import java.util.UUID

data class OneTimeTask(
    val taskId: UUID,
    val title :String,
    val isCompleted : Boolean = false,
    val date : LocalDate,
    val startTime : LocalTime,
    val finishTime :String,
)
