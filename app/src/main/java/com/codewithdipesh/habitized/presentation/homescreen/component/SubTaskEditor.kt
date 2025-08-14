package com.codewithdipesh.habitized.presentation.homescreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.domain.model.OneTimeTask
import com.codewithdipesh.habitized.domain.model.SubTask
import com.codewithdipesh.habitized.ui.theme.regular
import java.util.UUID

@Composable
fun SubTaskEditor(
    modifier: Modifier = Modifier,
    subTask: SubTask,
    enabled: Boolean = true,
    textSize: Int = 18,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    onSurface: Color = MaterialTheme.colorScheme.onSurface,
    onChange: (String) -> Unit = {},
    onToggleSubtask: () -> Unit = {},
    onDelete: (UUID) -> Unit = {}
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp),
        horizontalArrangement = if (enabled) Arrangement.SpaceBetween else Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        //checkbox and subtask title
        Row(
            modifier = Modifier
                .then(
                    if (enabled) Modifier
                        .wrapContentSize()
                        .weight(0.7f)
                    else Modifier.fillMaxWidth()
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .size(18.dp, 18.dp)
                    .background(
                        if (subTask.isCompleted) onSurface
                        else Color.Transparent
                    )
                    .border(
                        width = 1.dp,
                        color =
                            if (subTask.isCompleted) onSurface
                            else textColor
                    )
                    .clickable {
                        onToggleSubtask()
                    },
                contentAlignment = Alignment.Center
            ) {
                if (subTask.isCompleted) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Check",
                        tint = textColor
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
            //title
            if (enabled) {
                Box {
                    BasicTextField(
                        value = subTask.title,
                        onValueChange = {
                            onChange(it)
                        },
                        enabled = enabled,
                        textStyle = TextStyle(
                            color = textColor,
                            fontFamily = regular,
                            fontWeight = FontWeight.Normal,
                            fontSize = textSize.sp
                        ),
                        singleLine = false,
                        maxLines = 1,
                        cursorBrush = SolidColor(colorResource(R.color.primary)),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Placeholder shown only when title is empty
                    if (subTask.title.isEmpty()) {
                        Text(
                            text = "Write Down",
                            style = TextStyle(
                                color = onSurface,
                                fontFamily = regular,
                                fontWeight = FontWeight.Normal,
                                fontSize = textSize.sp
                            )
                        )
                    }
                }
            } else {
                Text(
                    text = subTask.title,
                    style = TextStyle(
                        color = textColor,
                        fontFamily = regular,
                        fontWeight = FontWeight.Normal,
                        fontSize = textSize.sp
                    )
                )
            }


        }
        //clear icon
        if (enabled) {
            IconButton(
                onClick = {
                    onDelete(subTask.subtaskId)
                },
                modifier = Modifier.weight(0.3f)
            ) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Cross",
                    tint = textColor
                )
            }
        }
    }

}

@Composable
fun TodoEditor(
    modifier: Modifier = Modifier,
    todo: OneTimeTask,
    onChange: (String) -> Unit = {},
    onToggle: () -> Unit = {},
    onDelete: (UUID) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .weight(1f)
                .toggleable(
                    value = todo.isCompleted,
                    onValueChange = { onToggle() },
                    role = Role.Checkbox
                )
        ) {
            Checkbox(
                checked = todo.isCompleted,
                onCheckedChange = {},
                enabled = !todo.isCompleted,
            )

            Text(
                text = todo.title,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f),
                style = TextStyle(
                    color = if (todo.isCompleted) MaterialTheme.colorScheme.onPrimary.copy(
                        0.7f
                    ) else MaterialTheme.colorScheme.onPrimary,
                    fontFamily = regular,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                ),
                maxLines = 3,
            )
            //More Menu option for Edit and Delete Task
            TaskMoreMenu(
                onEdit = onChange, onDelete = {
                    onDelete(it)
                },
                currentTask = todo
            )
        }

    }
}


/**
 * Composable for displaying a more options menu for tasks.
 * It includes options to edit or delete the task.
 */
@Composable
fun TaskMoreMenu(
    onEdit: (String) -> Unit = {},
    onDelete: (UUID) -> Unit = {},
    currentTask: OneTimeTask
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Delete") },
                onClick = {
                    // Handle delete action
                    onDelete(currentTask.taskId)
                    expanded = false
                }
            )
        }
    }
}