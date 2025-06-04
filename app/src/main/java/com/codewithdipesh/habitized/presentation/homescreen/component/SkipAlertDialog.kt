package com.codewithdipesh.habitized.presentation.homescreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.domain.model.HabitProgress
import com.codewithdipesh.habitized.domain.model.HabitWithProgress
import com.codewithdipesh.habitized.ui.theme.regular


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkipAlertDialog(
    modifier: Modifier = Modifier,
    habitWithProgress: HabitWithProgress,
    onDismiss : ()-> Unit,
    onConfirm : (HabitWithProgress)->Unit
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Text(
                text = "Are you a quitter?",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = regular,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )
        },
        text = {
            Text(
                text = "Or just having a weak moment? Don’t let it win today",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = regular,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                ),
                modifier = Modifier.padding(vertical = 16.dp)
            )
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.onPrimary),
                onClick = {
                    onDismiss()
                }
            ) {
                Text(
                    text = "No,I'll do",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                        fontFamily = regular,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(80.dp)
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onConfirm(habitWithProgress)
                    onDismiss()
                }
            ) {
                Text(
                    text = "Yes,Skip",
                    style = TextStyle(
                        color = Color.Red,
                        fontFamily = regular,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(70.dp)
                )
            }
        }
    )
}