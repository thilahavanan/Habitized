package com.codewithdipesh.habitized.presentation.homescreen

import com.codewithdipesh.habitized.domain.model.HabitWithProgress
import com.codewithdipesh.habitized.domain.model.OneTimeTask
import java.time.LocalDate

data class HomeScreenUI(
    val habitWithProgressList: List<HabitWithProgress> = emptyList(),
    val selectedOption: HomeScreenOption = HomeScreenOption.Habits,
    val selectedDate: LocalDate = LocalDate.now(),
    val oneTimeTasksUIState: OneTimeTasksUIState = OneTimeTasksUIState(),
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
        return oneTimeTaskList.filter { it.isCompleted }
    }

    /**
     *  Fun to get Today's One Time Tasks
     *  @return List<OneTimeTask> - List of Today's One Time Tasks
     */
    fun getTodayOneTimeTasks(currentDate: LocalDate): List<OneTimeTask> {
        return oneTimeTaskList.filter { it.date == currentDate && !it.isCompleted }
    }

    /**
     *  Fun to check if there are OverDue One Time Tasks
     */
    fun showOverDueTasks(): Boolean = getOverDueOneTimeTasks().isNotEmpty()

    /**
     * Checks if there are any completed one-time tasks.
     * @return `true` if there is at least one completed one-time task, `false` otherwise.
     */
    fun showCompletedTasks(): Boolean = getCompletedOneTimeTasks().isNotEmpty()

    /**
     * Checks if there are any today's one-time tasks.
     */
    fun showTodayTasks(currentDate: LocalDate): Boolean =
        getTodayOneTimeTasks(currentDate).isNotEmpty()
}

enum class HomeScreenOption {
    Habits,
    Todos
}