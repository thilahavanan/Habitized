package com.codewithdipesh.habitized.presentation.homescreen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.presentation.homescreen.HomeViewModel
import com.codewithdipesh.habitized.ui.theme.instrumentSerif
import com.codewithdipesh.habitized.ui.theme.regular

/**
 * Composable for adding a task in Todo's screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOneTimeTask(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    viewModel: HomeViewModel?
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
            Text(
                text = "Add your Todo's", color = MaterialTheme.colorScheme.onPrimary,
                fontFamily = instrumentSerif,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = addTodoTask,
                onValueChange = { addTodoTask = it },
                label = { Text("Add Todo Task") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions {
                    viewModel?.addTodoWithTitle(addTodoTask)
                    onDismiss()
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            ElevatedButton(
                shape = ButtonDefaults.elevatedShape,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(),
                onClick = {
                    viewModel?.addTodoWithTitle(addTodoTask)
                    onDismiss()
                }) {
                Text(
                    text = "Add", style = TextStyle(
                        fontFamily = regular,
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun AddTaskPreview() {
    AddOneTimeTask(viewModel = null)
}