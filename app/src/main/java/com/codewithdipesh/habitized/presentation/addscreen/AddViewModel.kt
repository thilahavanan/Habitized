package com.codewithdipesh.habitized.presentation.addscreen

import androidx.lifecycle.ViewModel
import com.codewithdipesh.habitized.domain.model.CountParam
import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.domain.model.Habit
import com.codewithdipesh.habitized.domain.model.HabitType
import com.codewithdipesh.habitized.domain.repository.HabitRepository
import com.codewithdipesh.habitized.presentation.addscreen.addhabitscreen.AddHabitUI
import com.codewithdipesh.habitized.presentation.homescreen.component.HabitCard
import com.codewithdipesh.habitized.presentation.util.Days
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalTime

@HiltViewModel
class AddViewModel @Inject constructor(
    private val repo : HabitRepository
) : ViewModel(){

    private val _habitUiState = MutableStateFlow(AddHabitUI())
    val habitUiState = _habitUiState.asStateFlow()

    suspend fun addHabit(){
//        repo.addOrUpdateHabit(
//            Habit(
//                title = _habitUiState.value.title,
//                description = _habitUiState.value.description,
//                type = _habitUiState.value.type,
//                goal_id = _habitUiState.value.goal_id,
//                start_date = _habitUiState.value.start_date,
//                frequency = _habitUiState.value.frequency,
//                days_of_week = _habitUiState.value.days_of_week,
//                daysOfMonth = _habitUiState.value.daysOfMonth,
//                reminder_time = _habitUiState.value.reminder_time,
//                is_active = _habitUiState.value.is_active,
//                color = _habitUiState.value.color,
//                countParam = _habitUiState.value.countParam,
//                countTarget = _habitUiState.value.countTarget,
//                durationParam = _habitUiState.value.durationParam,
//                duration = _habitUiState.value.duration
//            )
//        )
    }

    fun setType(type: HabitType){
        _habitUiState.value = _habitUiState.value.copy(
            type = type,
            paramOptions = CountParam.getParams(type),
            countParam = if(type == HabitType.OneTime) null
            else CountParam.getParams(type).first()
        )
    }

    fun setParam(param: CountParam){
        _habitUiState.value = _habitUiState.value.copy(
            countParam = param
        )
    }
    fun setTargetCount(count: Int){
        _habitUiState.value = _habitUiState.value.copy(
            countTarget = count
        )
    }
    fun setHours(duration: Int){
        _habitUiState.value = _habitUiState.value.copy(
            selectedHour = duration
        )
    }
    fun setMinutes(duration: Int){
        _habitUiState.value = _habitUiState.value.copy(
            selectedMinute = duration
        )
    }
    fun setSeconds(duration: Int){
        _habitUiState.value = _habitUiState.value.copy(
            selectedSeconds = duration
        )
    }

    fun setColor(color: Int){
        _habitUiState.value = _habitUiState.value.copy(
            color = color
        )
    }

    fun setFrequency(frequency: Frequency){
        _habitUiState.value = _habitUiState.value.copy(
            frequency = frequency
        )
    }

    fun toggleReminderOption(){
        _habitUiState.value = _habitUiState.value.copy(
            isShowReminderTime = !_habitUiState.value.isShowReminderTime
        )
    }

    fun setReminderTime(time : LocalTime){
        _habitUiState.value = _habitUiState.value.copy(
            reminder_time = time
        )
    }

    fun setTitle(title : String){
        _habitUiState.value = _habitUiState.value.copy(
            title = title
        )
    }

    fun setDescription(description : String){
        _habitUiState.value = _habitUiState.value.copy(
            description = description
        )
    }
    fun toggleColorOption(){
        _habitUiState.value = _habitUiState.value.copy(
            colorOptionAvailable = !_habitUiState.value.colorOptionAvailable
        )
    }

    fun toggleParamOption(){
        _habitUiState.value = _habitUiState.value.copy(
            isShowingParamOptions = !_habitUiState.value.isShowingParamOptions
        )
    }

    fun onSelectDay(day: Days) {
        val currentMap = _habitUiState.value.days_of_week.toMutableMap()
        val currentValue = currentMap[day] ?: false
        currentMap[day] = !currentValue  // Toggle the value

        _habitUiState.value = _habitUiState.value.copy(
            days_of_week = currentMap
        )
    }


}