package com.codewithdipesh.habitized.presentation.addscreen.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.ui.theme.regular
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderTimePicker(
    reminderTime : LocalTime?,
    onSelect : (LocalTime)->Unit,
    isReminderEnabled : Boolean
) {
    val showTimePicker = remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE),
        is24Hour = false
    )

    if(isReminderEnabled){
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ){
            Text(
                text = reminderTime?.format(
                    DateTimeFormatter.ofPattern("hh:mm a")
                ) ?: "Set Reminder",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = regular,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                ),
                modifier = Modifier
                    .clickable{
                        showTimePicker.value = true
                    }
            )
        }
    }

    if (showTimePicker.value) {
        AlertDialog(
            onDismissRequest = { showTimePicker.value = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedTime = LocalTime.of(timePickerState.hour,timePickerState.minute)
                    onSelect(selectedTime)
                    showTimePicker.value = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showTimePicker.value = false
                }) {
                    Text("Cancel")
                }
            },
            text = {
                TimePicker(state= timePickerState)
            }
        )
    }
}
