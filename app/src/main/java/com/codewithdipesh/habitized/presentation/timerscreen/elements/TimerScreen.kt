package com.codewithdipesh.habitized.presentation.timerscreen.elements

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.presentation.addscreen.component.AddScreenTopBar
import com.codewithdipesh.habitized.presentation.timerscreen.durationScreen.DurationViewModel
import com.codewithdipesh.habitized.presentation.timerscreen.Theme.*
import com.codewithdipesh.habitized.presentation.timerscreen.TimerState
import com.codewithdipesh.habitized.presentation.util.getColorFromKey
import com.codewithdipesh.habitized.presentation.util.toWord
import com.codewithdipesh.habitized.ui.theme.regular
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    modifier: Modifier = Modifier,
    habitProgressId : UUID,
    title : String,
    targetDurationValue : LocalTime,
    colorKey : String,
    viewmodel: DurationViewModel,
    navController: NavController
){

    
}