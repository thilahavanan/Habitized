package com.codewithdipesh.habitized.presentation.goalscreen.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.domain.model.Habit
import com.codewithdipesh.habitized.domain.model.HabitType
import com.codewithdipesh.habitized.presentation.habitscreen.components.Element
import com.codewithdipesh.habitized.presentation.util.IntToWeekDayMap
import com.codewithdipesh.habitized.presentation.util.getOriginalColorFromKey
import com.codewithdipesh.habitized.presentation.util.toWord
import com.codewithdipesh.habitized.ui.theme.instrumentSerif
import com.codewithdipesh.habitized.ui.theme.regular

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsShowcase(
    title :String,
    subtitle : String = "",
    habits : List<Habit>,
    onHabitClick : (Habit) -> Unit,
    onDismiss : () -> Unit,
    modifier: Modifier = Modifier
){
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue ->
            newValue != SheetValue.Hidden
        }
    )
    BackHandler {
        onDismiss()
    }
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = {},
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            //heading
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(
                    onClick = {onDismiss()},
                    modifier = Modifier
                        .padding(top = 30.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "back",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Text(
                    text = title,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = instrumentSerif,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontSize = 24.sp
                    )
                )
            }
            //sub heading
            if (subtitle != ""){
                Text(
                    text = subtitle,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = regular,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )
            }
            //habits
            habits.forEach {
                HabitBox(
                    habit = it,
                    onClick = {
                        onHabitClick(it)
                        onDismiss()
                    }
                )
            }
            if(habits.isEmpty()){
                Text(
                    text = "No habits to show",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary.copy(0.6f),
                        fontFamily = regular,
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }
    }
}

@Composable
fun HabitBox(
    habit : Habit ,
    onClick : () -> Unit,
    modifier: Modifier = Modifier
) {
    Element(
        backgroundColor = getOriginalColorFromKey(habit.colorKey).copy(0.25f),
        clickable = true,
        onClick = {
            onClick()
        }
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            //title, frequency
            Column (
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Text(
                    text = habit.title!!,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = regular,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )

                Text(
                    text = when(habit.frequency){
                        Frequency.Daily -> "Everyday"
                        Frequency.Weekly -> {
                            IntToWeekDayMap(habit.days_of_week)
                                .filter { it.value == true }
                                .keys
                                .joinToString(", ") { it.name.lowercase().take(3) }
                        }
                        Frequency.Monthly -> {
                            habit.daysOfMonth!!.joinToString(", ")
                        }
                        else -> "Everyday"
                    },
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = regular,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp
                    )
                )
            }
            //session , target
            Column (
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                when(habit.type){
                    HabitType.OneTime -> {
                        Text(
                            text = "OneTime",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontFamily = regular,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            )
                        )
                    }
                    HabitType.Duration -> {
                        Text(
                            text = habit.duration?.toWord() ?: "",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontFamily = regular,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            )
                        )
                    }
                    else -> {
                        Text(
                            text = "${habit.countTarget} ${habit.countParam?.displayName}",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontFamily = regular,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
                //only for session
                if(habit.type == HabitType.Session){
                    Text(
                        text = habit.duration?.toWord() ?: "",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = regular,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    )
                }
            }
        }
    }
}