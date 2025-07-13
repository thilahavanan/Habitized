package com.codewithdipesh.habitized.presentation.goalscreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.ui.theme.regular

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDeleteOption(
    onDeleteGoalOnly : () -> Unit,
    onDeleteHabitAlso : () -> Unit,
    onDismiss : () ->Unit,
    modifier: Modifier = Modifier,
){
    val bottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        containerColor = MaterialTheme.colorScheme.background,
        sheetState = bottomSheetState
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Box(
                modifier = Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable {
                        onDismiss()
                        onDeleteGoalOnly()
                    }
            ){
                Row(Modifier.padding(24.dp),verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = colorResource(R.color.delete_red)
                    )
                    Text(
                        text =  "Delete the Goal Only",
                        style = TextStyle(
                            color = colorResource(R.color.delete_red),
                            fontFamily = regular,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        )
                    )
                }
            }

            Box(
                modifier = Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable {
                        onDismiss()
                        onDeleteHabitAlso()
                    }
            ){
                Row(Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = colorResource(R.color.delete_red)
                    )
                    Text(
                        text =  "Delete with Habits associated also",
                        style = TextStyle(
                            color = colorResource(R.color.delete_red),
                            fontFamily = regular,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        )
                    )
                }
            }
        }
    }
}


//deleteAlertBox
@Composable
fun DeleteGoalAlert(
    modifier: Modifier = Modifier,
    isDeleteHabits : Boolean,
    onConfirm : ()-> Unit,
    onCancel : () ->Unit
){
    AlertDialog(
        onDismissRequest = {
            onCancel()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onCancel()
                }
            ) {
                Text(
                    text = "Cancel",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = regular,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onCancel()
                }
            ) {
                Text(
                    text = "Yes,Delete",
                    style = TextStyle(
                        color = colorResource(R.color.delete_red),
                        fontFamily = regular,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    )
                )
            }
        },
        title = {
            Text(
                text = "Delete the Goal" + if(isDeleteHabits) " and habits associated with it" else "",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = regular,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
        },
        text = {
            Text(
                text = "This will delete " + if(isDeleteHabits) "all the progress and images of habits associated with this Goal also " else "all the progress of this Goal",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = regular,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            )
        }
    )
}
