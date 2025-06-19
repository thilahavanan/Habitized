package com.codewithdipesh.habitized.presentation.timerscreen.sessionScreen

import com.codewithdipesh.habitized.domain.model.HabitWithProgress
import com.codewithdipesh.habitized.domain.model.SubTask
import com.codewithdipesh.habitized.presentation.timerscreen.Theme
import com.codewithdipesh.habitized.presentation.timerscreen.TimerState
import java.util.UUID

data class SessionUI(
    val  progressId: UUID? =  null,
    val habitWithProgress: HabitWithProgress? = null,
    val timerState : TimerState = TimerState.Not_Started,
    val theme : Theme = Theme.Normal,
    val isStarted : Boolean = false,
    val isThemesOpen : Boolean = false,

    val tempSubTask: UUID? = null
)


