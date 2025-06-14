package com.codewithdipesh.habitized.presentation.timerscreen.durationScreen

import com.codewithdipesh.habitized.domain.model.HabitWithProgress
import java.util.UUID

data class DurationUI(
    val  progressId: UUID? =  null,
    val habitWithProgress: HabitWithProgress? = null,
    val timerState : TimerState = TimerState.Not_Started,
    val theme : Theme = Theme.Normal,
    val isThemesOpen : Boolean = false
)


enum class TimerState{
    Paused,
    Not_Started,
    Resumed,
    Finished
}
