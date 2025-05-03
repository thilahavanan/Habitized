package com.codewithdipesh.habitized.data.repository

import com.codewithdipesh.habitized.data.local.dao.GoalDao
import com.codewithdipesh.habitized.data.local.dao.HabitDao
import com.codewithdipesh.habitized.data.local.dao.HabitProgressDao
import com.codewithdipesh.habitized.data.local.dao.OneTimeTaskDao
import com.codewithdipesh.habitized.data.local.dao.SubTaskDao
import com.codewithdipesh.habitized.data.local.mapper.toEntity
import com.codewithdipesh.habitized.data.local.mapper.toHabit
import com.codewithdipesh.habitized.data.local.mapper.toHabitProgress
import com.codewithdipesh.habitized.data.local.mapper.toOneTimeTask
import com.codewithdipesh.habitized.domain.model.Goal
import com.codewithdipesh.habitized.domain.model.Habit
import com.codewithdipesh.habitized.domain.model.HabitProgress
import com.codewithdipesh.habitized.domain.model.OneTimeTask
import com.codewithdipesh.habitized.domain.model.SubTask
import com.codewithdipesh.habitized.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.util.UUID

class HabitRepoImpl(
    private val habitDao: HabitDao,
    private val habitProgressDao: HabitProgressDao,
    private val oneTimeTaskDao: OneTimeTaskDao,
    private val subtaskDao: SubTaskDao,
    private val goalDao: GoalDao
) : HabitRepository {
    override suspend fun addOrUpdateHabit(habit: Habit) {
        habitDao.insertHabit(habit.toEntity())
    }
    override suspend fun deleteHabit(habitId: UUID) {
        //delete habit with habit progress and subtasks
        //get all habit progress
        val habitProgress = habitProgressDao.getHabitProgress(habitId)
        habitProgress.collect {
            it.forEach {
                subtaskDao.deleteSubtaskByHabitId(it.progressId)
                habitProgressDao.deleteProgress(it.progressId)
            }
        }
        //delete habit
        habitDao.deleteHabit(habitId)

    }
    override suspend fun getHabitById(habitId: UUID): Habit? {
        val habit = habitDao.getHabitById(habitId)
        return if(habit != null){
            habit.toHabit()
        }else{
            null
        }
    }



    override suspend fun addHabitProgressJob(date: LocalDate) {
        //fetch all habits available
        val habits  = habitDao.getAllHabits()
//        habits.collect {
//            it.forEach {
//                val habitProgress = HabitProgress(
//                    habitId = it.habit_id,
//                    date = date,
//                    currentCount = 0,
//                    type = it.type,
//                    targetCount = it.countTarget
//                     = 0f,
//                    percentageValue = 0f,
//                    subtasks = emptyList(),
//                    title = it.title,
//                    progressId = UUID.randomUUID()
//                ).toEntity()
//                habitProgressDao.insertProgress(habitProgress)
//            }
//        }
    }
    override suspend fun updateHabitProgress(progress: HabitProgress) {
        TODO("Not yet implemented")
    }

    override suspend fun getHabitProgress(
        habitId: UUID,
        date: String
    ): HabitProgress? {
        TODO("Not yet implemented")
    }

    override suspend fun getAllHabitProgress(habitId: UUID): List<HabitProgress>? {
        TODO("Not yet implemented")
    }

    override suspend fun getTodayHabitProgresses(date: LocalDate): List<HabitProgress>? {
        TODO("Not yet implemented")
    }




    override suspend fun addGoal(goal: Goal) {
        TODO("Not yet implemented")
    }
    override suspend fun updateGoal(goal: Goal) {
        TODO("Not yet implemented")
    }
    override suspend fun deleteGoal(goalId: UUID) {
        TODO("Not yet implemented")
    }
    override suspend fun getGoalById(goalId: UUID): Flow<Goal?> {
        TODO("Not yet implemented")
    }
    override fun getAllGoals(): Flow<List<Goal>> {
        TODO("Not yet implemented")
    }



    override suspend fun addOneTimeTask(task: OneTimeTask) {
        TODO("Not yet implemented")
    }
    override suspend fun updateOneTimeTask(task: OneTimeTask) {
        TODO("Not yet implemented")
    }
    override suspend fun deleteOneTimeTask(taskId: UUID) {
        TODO("Not yet implemented")
    }



    override suspend fun insertSubtask(subtask: SubTask) {
        TODO("Not yet implemented")
    }
    override suspend fun updateSubtask(subtask: SubTask) {
        TODO("Not yet implemented")
    }
    override suspend fun deleteSubtask(subtaskId: UUID) {
        TODO("Not yet implemented")
    }
    override suspend fun markSubTask(subtaskId: UUID, status: Boolean) {
        TODO("Not yet implemented")
    }

  
    override fun getHabitsForDay(date: LocalDate): Flow<List<HabitProgress>> {

        return habitProgressDao.getAllProgress(date)
            .map {list->
                list.map {
                    it.toHabitProgress()
                }
            }
    }

    override fun getTasksForDay(date: LocalDate): Flow<List<OneTimeTask>> {
        return oneTimeTaskDao.getAllTasks(date)
            .map {list->
                list.map {
                    it.toOneTimeTask()
                }
            }
    }


}