package com.codewithdipesh.habitized.presentation.homescreen

import android.util.Log
import com.codewithdipesh.habitized.domain.model.HabitWithProgress
import com.codewithdipesh.habitized.domain.model.OneTimeTask
import java.time.LocalDate

data class HomeScreenUI(
    val habitWithProgressList: List<HabitWithProgress> = emptyList(),
    val oneTimeTasksUIState: OneTimeTasksUIState = OneTimeTasksUIState(),
    val selectedOption: HomeScreenOption = HomeScreenOption.Habits,
    val selectedDate: LocalDate = LocalDate.now(),
    val ongoingHabit: HabitWithProgress? = null,
    val ongoingHour: Int = 0,
    val ongoingMinute: Int = 0,
    val ongoingSecond: Int = 0,
    val isShowingDatePicker: Boolean = false
)

/**
 *  Ui State Class to handle the List<OneTimeTask> in the [HomeScreenOption.Todos] Section
 */
data class OneTimeTasksUIState(
    val oneTimeTaskList: List<OneTimeTask> = emptyList()
) {
    /**
     *  Fun to get OverDue One Time Tasks
     *  @return List<OneTimeTask> - List of OverDue One Time Tasks
     */
    fun getOverDueOneTimeTasks(): List<OneTimeTask> {
        return oneTimeTaskList.filter { !it.isCompleted && it.date < LocalDate.now() }
    }

    /**
     *  Fun to get Completed One Time Tasks
     *  @return List<OneTimeTask> - List of Completed One Time TasksOne
     */
    fun getCompletedOneTimeTasks(): List<OneTimeTask> {
        return oneTimeTaskList.filter { it.isCompleted  }
    }

    /**
     *  Fun to get Today's One Time Tasks
     *  @return List<OneTimeTask> - List of Today's One Time Tasks
     */
    fun getTodayOneTimeTasks(): List<OneTimeTask> {
        return oneTimeTaskList.filter { it.date == LocalDate.now() && !it.isCompleted }
    }
}

enum class HomeScreenOption {
    Habits,
    Todos
}
