package com.codewithdipesh.habitized.presentation.timerscreen.durationScreen

import com.codewithdipesh.habitized.domain.model.CountParam
import com.codewithdipesh.habitized.domain.model.HabitProgress
import com.codewithdipesh.habitized.domain.model.HabitType
import com.codewithdipesh.habitized.domain.model.HabitWithProgress
import com.codewithdipesh.habitized.domain.model.Status
import java.time.LocalDate
import java.util.UUID

data class DurationUI(
    val progressId: UUID? =  null,
    val habitWithProgress: HabitWithProgress? = null,
    val timerState : TimerState = TimerState.Not_Started,
    val theme : Theme = Theme.Normal,
    val isSettingsOpen : Boolean = false
)


enum class TimerState{
    Paused,
    Not_Started,
    Resumed,
    Finished
}
