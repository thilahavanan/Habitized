package com.codewithdipesh.habitized.domain.alarmManager

import java.time.LocalDateTime

data class AlarmItem(
    val time : LocalDateTime,
    val title : String,
    val text : String
)