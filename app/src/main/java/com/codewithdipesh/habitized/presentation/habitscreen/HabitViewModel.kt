package com.codewithdipesh.habitized.presentation.habitscreen

import android.app.AlarmManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithdipesh.habitized.domain.alarmManager.AlarmItem
import com.codewithdipesh.habitized.domain.alarmManager.AlarmScheduler
import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.domain.model.HabitType
import com.codewithdipesh.habitized.domain.model.ImageProgress
import com.codewithdipesh.habitized.domain.repository.HabitRepository
import com.codewithdipesh.habitized.domain.util.getNextAlarmDateTime
import com.codewithdipesh.habitized.presentation.util.IntToWeekDayMap
import com.codewithdipesh.habitized.presentation.util.WeekDayMapToInt
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import kotlin.String

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val repo : HabitRepository,
    private val alarmScheduler: AlarmScheduler
): ViewModel() {

    private val _state = MutableStateFlow(HabitDetailsUI())
    val state = _state.asStateFlow()

    suspend fun init( id : UUID){
        val habit = repo.getHabitById(id)
        _state.value = _state.value.copy(
            id = id,
            title = habit!!.title,
            description = habit.description,
            type = habit.type,
            frequency = habit.frequency,
            targetCount = habit.countTarget ?: 0,
            targetTime = habit.duration,
            countParam = habit.countParam,
            days_of_week = habit.days_of_week,
            daysOfMonth = habit.daysOfMonth ?: emptyList(),
            reminder_time = habit.reminder_time,
            currentStreak = habit.currentStreak,
            maximumStreak = habit.maxStreak
        )
        val completed  = repo.getAllCompletedProgress(id)
        val total = when(state.value.frequency){
            Frequency.Daily -> {
                (LocalDate.now().toEpochDay() - habit.start_date.toEpochDay()).toInt() + 1
            }
            Frequency.Weekly -> {
                val start = habit.start_date
                val now = LocalDate.now()
                val totalWeeks = (start.until(now).days / 7) + 1
                // Count only the scheduled days of the week
                totalWeeks * habit.days_of_week.count { it == 1 }
            }
            Frequency.Monthly -> {
                val start = habit.start_date
                val now = LocalDate.now()
                val months = (now.year - start.year) * 12 + (now.monthValue - start.monthValue) + 1
                months * (habit.daysOfMonth?.size ?: 0)
            }
            else -> 0
        }
        _state.value = _state.value.copy(
            totalCompleted = completed.size,
            completionRate = (completed.size.toFloat() / total.toFloat() * 100).toInt(),
            progressList = completed
        )
        val imageProgresses = repo.getImageProgressesForHabit(id)
        _state.value = _state.value.copy(
            imageProgresses = imageProgresses
        )
    }

    suspend fun saveImage(id : UUID?,image : String, date : LocalDate, description : String){
        repo.insertImageProgress(
            ImageProgress(
                id = id ?: UUID.randomUUID(),
                habitId = state.value.id!!,
                imagePath = image,
                date = date,
                description = description
            )
        )
    }

    suspend fun deleteImage(id :UUID){
        repo.deleteImageProgress(id)
    }
    suspend fun deleteHabit(id: UUID){
        repo.deleteHabit(id)
        //canceling the alarm if present
        if(_state.value.reminder_time != null){
            val alarmItem = AlarmItem(
                id = id,
                //only id is needed as request code is this
                //other details are dummy
                time = LocalDateTime.now(),
                title = "",
                text = "",
                frequency = Frequency.Daily,
                daysOfWeek = emptyList(),
                daysOfMonth = emptyList()
            )
            alarmScheduler.cancel(alarmItem)
        }
    }

    fun clearUi(){
        _state.value = HabitDetailsUI()
    }
}