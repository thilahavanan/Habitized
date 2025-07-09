package com.codewithdipesh.habitized.domain.alarmManager

import com.codewithdipesh.habitized.domain.model.Frequency
import java.time.LocalDateTime

data class AlarmItem(
    val time : LocalDateTime,
    val title : String,
    val text : String,
    val frequency : Frequency,
    val daysOfWeek : List<Int>,
    val daysOfMonth: List<Int>
)