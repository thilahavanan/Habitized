package com.codewithdipesh.habitized.presentation.homescreen.component

import android.view.Surface
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.domain.model.HabitWithProgress
import com.codewithdipesh.habitized.domain.model.Status
import com.codewithdipesh.habitized.domain.model.SubTask
import com.codewithdipesh.habitized.presentation.addscreen.component.Button
import com.codewithdipesh.habitized.presentation.addscreen.component.ColorChoserItem
import com.codewithdipesh.habitized.ui.theme.regular
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSubTask(
    modifier: Modifier = Modifier,
    habitWithProgress: HabitWithProgress,
    onUpdateSubTask : (List<SubTask>) -> Unit
){
    var subtasks by remember {
        mutableStateOf(habitWithProgress.subtasks)
    }

    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = {
            subtasks = subtasks.toMutableList().apply {
                removeIf { it.title == "" }
            }
            onUpdateSubTask(subtasks)
        },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.heightIn(min = 300.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp,bottom = 100.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ){

            Text(
                text = habitWithProgress.habit.title,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = regular,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            )
            Spacer(Modifier.height(8.dp))
            subtasks.forEachIndexed { index, subTask ->
                SubTaskEditor(
                    subTask = subTask,
                    onChange = {newtitle ->
                        //change the title
                        subtasks = subtasks.toMutableList().also { it[index] = it[index].copy(title = newtitle) }

                    },
                    onToggleSubtask = {
                        val previous = subtasks[index].isCompleted
                        subtasks = subtasks.toMutableList().also { it[index] = it[index].copy(isCompleted = !previous) }
                    },
                    onDelete = {
                        subtasks = subtasks.toMutableList().also { it.removeAt(index) }
                    }
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "+ List Subtask",
                style = TextStyle(
                    color = if(habitWithProgress.progress.status == Status.Done){
                        MaterialTheme.colorScheme.scrim
                    }else{
                        MaterialTheme.colorScheme.onSurface
                    },
                    fontFamily = regular,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                ),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable{
                    subtasks = subtasks.toMutableList().apply {
                        add(
                            SubTask(
                                title = "",
                                habitProgressId = habitWithProgress.progress.progressId
                            )
                        )
                    }
                }
            )
        }
    }

}


