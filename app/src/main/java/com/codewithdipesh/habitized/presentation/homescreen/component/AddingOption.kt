package com.codewithdipesh.habitized.presentation.homescreen.component

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import com.codewithdipesh.habitized.domain.model.SubTask
import com.codewithdipesh.habitized.presentation.addscreen.component.Button
import com.codewithdipesh.habitized.presentation.addscreen.component.ColorChoserItem
import com.codewithdipesh.habitized.ui.theme.regular

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddingOption(
    modifier: Modifier = Modifier,
    onDismiss : () -> Unit = {},
    onAddHabitClicked : () -> Unit = {},
    onAddGoalClicked : () ->Unit = {}
){
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ){
            AddOptionButton(
                title = "New Goal",
                subtitle = "Create personalized goal",
                onClick = {
                    onAddGoalClicked()
                    onDismiss()
                }
            )
            HorizontalDivider(
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            AddOptionButton(
                title = "New Habit",
                subtitle = "Daily actionable task",
                onClick = {
                    onAddHabitClicked()
                    onDismiss()
                }
            )
        }
    }
}


@Composable
fun AddOptionButton(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{
                onClick()
            }
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .padding(start = 40.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 20.sp,
                fontFamily = regular,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = subtitle,
            style = TextStyle(
                color = MaterialTheme.colorScheme.surfaceDim,
                fontSize = 14.sp,
                fontFamily = regular,
                fontWeight = FontWeight.Normal
            )
        )
    }
}