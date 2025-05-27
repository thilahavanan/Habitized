package com.codewithdipesh.habitized.presentation.addscreen.component

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.ui.theme.regular
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderTimePicker(
    isShowing : Boolean,
    reminderTime : String,
    onSelect : (String)->Unit,
    onDismiss : ()->Unit
) {
    val showTimePicker = remember { mutableStateOf(isShowing) }

    val calendar = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE),
        is24Hour = true
    )

    Text(
        text = reminderTime,
        style = TextStyle(
            color = MaterialTheme.colorScheme.onPrimary,
            fontFamily = regular,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
    )



    if (showTimePicker.value) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = {
                    val hour = timePickerState.hour
                    val minute = timePickerState.minute
                    val formattedTime = String.format("%02d:%02d", hour, minute)
                    onSelect(formattedTime)
                    onDismiss()
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onDismiss()
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
