package com.codewithdipesh.habitized.presentation.homescreen
import com.codewithdipesh.habitized.domain.model.Habit
import com.codewithdipesh.habitized.domain.model.HabitProgress
import com.codewithdipesh.habitized.domain.model.HabitWithProgress
import com.codewithdipesh.habitized.domain.model.OneTimeTask
import java.time.LocalDate

data class HomeScreenUI(
    val habitWithProgressList: List<HabitWithProgress> = emptyList(),
    val tasks : List<OneTimeTask> = emptyList(),
    val selectedOption : HomeScreenOption = HomeScreenOption.TODOS,
    val selectedDate : LocalDate = LocalDate.now(),

    val ongoingHabit : HabitWithProgress? = null,
    val ongoingHour : Int = 0,
    val ongoingMinute : Int = 0,
    val ongoingSecond : Int = 0,

    val isShowingDatePicker : Boolean = false
)

enum class HomeScreenOption{
    TODOS,
    REMINDER
}