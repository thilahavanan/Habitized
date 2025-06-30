package com.codewithdipesh.habitized.presentation.habitscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.domain.model.HabitType
import com.codewithdipesh.habitized.domain.model.ImageProgress
import com.codewithdipesh.habitized.domain.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import kotlin.String

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val repo : HabitRepository
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
}