package com.codewithdipesh.habitized.presentation.homescreen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Composable for adding a task in Todo's screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTask(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {}
) {
    val addTaskSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var addTodoTask by rememberSaveable { mutableStateOf<String>("") }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = addTaskSheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Add your Todo's", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = addTodoTask,
                onValueChange = { addTodoTask = it },
                label = { Text("Add Todo Task") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions {
                    onDismiss()
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            ElevatedButton(shape = ButtonDefaults.elevatedShape, modifier = Modifier.fillMaxWidth(), onClick = { onDismiss() }) {
                Text("Add")
            }
        }
    }
}

@Preview
@Composable
private fun AddTaskPreview() {
    AddTask()
}