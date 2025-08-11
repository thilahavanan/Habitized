package com.codewithdipesh.habitized.domain.alarmManager

import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.domain.model.ReminderType
import java.time.LocalDateTime
import java.util.UUID

data class AlarmItem(
    val id: UUID,
    val reminderType: ReminderType,
    val nextAlarmDateTime: LocalDateTime,
    val title: String,
    val text: String,
    val frequency: Frequency,
    val daysOfWeek: List<Int>,
    val daysOfMonth: List<Int>
)