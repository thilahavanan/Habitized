package com.codewithdipesh.habitized.presentation.addscreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.domain.model.ReminderType
import com.codewithdipesh.habitized.ui.theme.regular
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderTimePicker(
    reminderType: ReminderType,
    onSelectReminderType : (ReminderType)->Unit,
    onSelectReminderFrom : (LocalTime)->Unit,
    onSelectReminderTo : (LocalTime)->Unit,
    onSelectReminderInterval : (Int)->Unit,
    onSelectReminderTime : (LocalTime)->Unit
) {
    val showReminderTimePicker = remember { mutableStateOf(false) }
    val showReminderFromPicker = remember { mutableStateOf(false) }
    val showReminderToPicker = remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE),
        is24Hour = false
    )
    val FromTimePickerState = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE),
        is24Hour = false
    )
    val ToTimePickerState = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE),
        is24Hour = false
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Selector(
            options = ReminderType.getTypes().map { it.displayName },
            selectedOption = reminderType.displayName,
            onOptionSelected = { selectedType ->
                val newReminderType = when (selectedType.lowercase()) {
                    "once" -> {
                        when (reminderType) {
                            is ReminderType.Once -> reminderType
                            is ReminderType.Interval -> ReminderType.Once(LocalTime.now())
                        }
                    }
                    "interval" -> {
                        when (reminderType) {
                            is ReminderType.Interval -> reminderType
                            is ReminderType.Once -> ReminderType.Interval(120, LocalTime.now(), LocalTime.now().plusHours(12))
                        }
                    }
                    else -> ReminderType.Once(LocalTime.now())
                }
                onSelectReminderType(newReminderType)
            },
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
            selectedOptionColor = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.fillMaxWidth()
        )
        when(reminderType){
            is ReminderType.Interval -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Box(
                            modifier = Modifier
                                .wrapContentSize()
                                .clip(RoundedCornerShape(15.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(0.7f)),
                            contentAlignment = Alignment.CenterStart
                        ){
                            Text(
                                text = "From ${reminderType.fromTime.format(
                                    DateTimeFormatter.ofPattern("hh:mm a")
                                ) ?: "Set Reminder"}",
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontFamily = regular,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp
                                ),
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable{
                                    showReminderFromPicker.value = true
                                }
                            )
                        }
                        Text(
                            text = "...",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontFamily = regular,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            )
                        )
                        Box(
                            modifier = Modifier
                                .wrapContentSize()
                                .clip(RoundedCornerShape(15.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(0.7f)),
                            contentAlignment = Alignment.CenterStart
                        ){
                            Text(
                                text = "To ${reminderType.toTime.format(
                                    DateTimeFormatter.ofPattern("hh:mm a")
                                )}",
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontFamily = regular,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp
                                ),
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable{
                                    showReminderToPicker.value = true
                                }
                            )
                        }
                    }
                    //interval
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Column(
                            modifier = Modifier.width(40.dp)
                        ){
                            BasicTextField(
                                value = if(reminderType.interval != 0) reminderType.interval.toString() else "",
                                onValueChange = {
                                    if(it.isEmpty() || it.toIntOrNull() != null){
                                        if(it.isNotEmpty()){
                                            if(it.toInt() < 720 ) onSelectReminderInterval(it.toInt())
                                        }else{
                                            onSelectReminderInterval(0)
                                        }
                                    }
                                },
                                textStyle = TextStyle(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontFamily = regular,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 18.sp
                                ),
                                singleLine = true,
                                maxLines = 1,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(4.dp))
                            DashedDivider(
                                thickness = 2.dp,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Min",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontFamily = regular,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }
            is ReminderType.Once -> {
                Text(
                    text = reminderType.reminderTime.format(
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
                            showReminderTimePicker.value = true
                        }
                )
            }
        }
    }

    if (showReminderTimePicker.value) {
        AlertDialog(
            onDismissRequest = { showReminderTimePicker.value = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedTime = LocalTime.of(timePickerState.hour,timePickerState.minute)
                    onSelectReminderTime(selectedTime)
                    showReminderTimePicker.value = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showReminderTimePicker.value = false
                }) {
                    Text("Cancel")
                }
            },
            text = {
                TimePicker(state= timePickerState)
            }
        )
    }
    if (showReminderFromPicker.value) {
        AlertDialog(
            onDismissRequest = { showReminderFromPicker.value = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedTime = LocalTime.of(FromTimePickerState.hour,FromTimePickerState.minute)
                    onSelectReminderFrom(selectedTime)
                    showReminderFromPicker.value = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showReminderFromPicker.value = false
                }) {
                    Text("Cancel")
                }
            },
            text = {
                TimePicker(state= FromTimePickerState)
            }
        )
    }
    if (showReminderToPicker.value) {
        AlertDialog(
            onDismissRequest = { showReminderToPicker.value = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedTime = LocalTime.of(ToTimePickerState.hour,ToTimePickerState.minute)
                    onSelectReminderTo(selectedTime)
                    showReminderToPicker.value = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showReminderToPicker.value = false
                }) {
                    Text("Cancel")
                }
            },
            text = {
                TimePicker(state= ToTimePickerState)
            }
        )
    }
}
