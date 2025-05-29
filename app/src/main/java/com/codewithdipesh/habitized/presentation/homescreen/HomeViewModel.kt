package com.codewithdipesh.habitized.presentation.homescreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithdipesh.habitized.domain.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo : HabitRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(HomeScreenUI())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.value = _uiState.value.copy(
            selectedDate = LocalDate.now()
        )
        loadHomePage(_uiState.value.selectedDate)
    }

    fun loadHomePage(date: LocalDate){
        viewModelScope.launch(Dispatchers.IO){
            val habits = repo.getHabitsForDay(date)
            val tasks = repo.getTasksForDay(date)
            _uiState.value = _uiState.value.copy(
                habitWithProgressList = habits,
                tasks = tasks
            )
        }
    }

    fun onOptionSelected(option: HomeScreenOption){
        _uiState.value = _uiState.value.copy(
            selectedOption = option
        )
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.value = _uiState.value.copy(
            selectedDate = date
        )
        loadHomePage(date)
    }

    fun toggleDatePicker(){
        _uiState.value = _uiState.value.copy(
            isShowingDatePicker = !_uiState.value.isShowingDatePicker
        )
    }


}