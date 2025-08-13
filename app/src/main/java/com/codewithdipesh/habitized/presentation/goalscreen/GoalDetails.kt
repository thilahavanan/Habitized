package com.codewithdipesh.habitized.presentation.goalscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.domain.model.Habit
import com.codewithdipesh.habitized.presentation.goalscreen.components.CustomChart
import com.codewithdipesh.habitized.presentation.goalscreen.components.DeleteGoalAlert
import com.codewithdipesh.habitized.presentation.goalscreen.components.GraphType
import com.codewithdipesh.habitized.presentation.goalscreen.components.HabitsShowcase
import com.codewithdipesh.habitized.presentation.goalscreen.components.ShowDeleteOption
import com.codewithdipesh.habitized.presentation.goalscreen.components.toName
import com.codewithdipesh.habitized.presentation.habitscreen.components.Element
import com.codewithdipesh.habitized.presentation.navigation.Screen
import com.codewithdipesh.habitized.ui.theme.instrumentSerif
import com.codewithdipesh.habitized.ui.theme.regular
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalDetails(
    id : UUID?,
    title : String,
    modifier: Modifier = Modifier,
    viewmodel: GoalViewModel,
    navController:NavController
){
    val state by viewmodel.state.collectAsState()
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val sheetState = rememberModalBottomSheetState()

    var showGraphTypeChoose by remember { mutableStateOf(false) }

    var showHabits by remember { mutableStateOf(false) }
    var showHabitSubTitle by remember { mutableStateOf("") }
    var showingHabitList by remember { mutableStateOf(emptyList<Habit>()) }

    //show the delete option and warning
    var showDeleteOptions by remember { mutableStateOf(false) }
    var showDeleteWarning by remember { mutableStateOf(false) }
    var DeletingHabitAlso by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            viewmodel.clearUi()
        }
    }
    LaunchedEffect(Unit) {
        scope.launch {
            viewmodel.init(id)
        }
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
            ){
                //left icon
                IconButton(
                    onClick = {
                        navController.navigateUp()
                    },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(top = 30.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "back",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                //title
                Text(
                    text = "Goal",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = instrumentSerif,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontSize = 20.sp
                    ),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 40.dp)
                )
                //options(share,edit,delete
                Row(modifier = Modifier.align(Alignment.CenterEnd) ,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    if(id != null){
                       IconButton(
                           onClick = {
                               navController.navigate(Screen.AddGoal.createRoute(state.id.toString()))
                           },
                           modifier = Modifier
                               .padding(top = 30.dp)
                       ) {
                           Icon(
                               imageVector = Icons.Default.Edit,
                               contentDescription = "edit",
                               tint = MaterialTheme.colorScheme.onPrimary
                           )
                       }
                        IconButton(
                            onClick = {
                                showDeleteOptions = true
                            },
                            modifier = Modifier
                                .padding(top = 30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "delete",
                                tint = colorResource(R.color.delete_red)
                            )
                        }
                   }
                }
            }
        }
    ) { innerPadding ->
        //choose graph type
        if(showGraphTypeChoose){
            ChooseGraphType(
                types = GraphType.entries.toList(),
                selected = state.showedGraphType,
                onSelected = {
                  viewmodel.setShowedEfforts(it)
                },
                onDismiss = {
                    showGraphTypeChoose = false
                },
                sheetState = sheetState
            )
        }
        if(showHabits){
            HabitsShowcase(
                title = state.title,
                subtitle = showHabitSubTitle,
                habits = showingHabitList,
                onHabitClick = {
                    navController.navigate(Screen.HabitScreen.createRoute(it))
                },
                onDismiss = {
                    showHabits = false
                }
            )
        }
        //delete options
        if(showDeleteOptions){
            ShowDeleteOption(
                onDismiss = { showDeleteOptions = false},
                onDeleteGoalOnly = {
                    showDeleteWarning = true
                    DeletingHabitAlso = false
                },
                onDeleteHabitAlso = {
                    showDeleteWarning = true
                    DeletingHabitAlso = true
                }
            )
        }
        //delete warning
        if(showDeleteWarning) {
            DeleteGoalAlert(
                onConfirm = {
                    scope.launch {
                        if(DeletingHabitAlso) viewmodel.deleteGoal(id!!,true) else viewmodel.deleteGoal(id!!,false)
                        navController.navigateUp()
                    }
                },
                isDeleteHabits = DeletingHabitAlso == true,
                onCancel = {
                    showDeleteWarning = false
                }
            )
        }
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            //title, probability and date
            //title and prob
            Element{
                //title
                Text(
                    text = title,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = regular,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
            }
            //description
            if(state.description != ""){
                Element{
                    Text(
                        text = "description",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = regular,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "${state.description}",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = regular,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        )
                    )
                }
            }
            //habits
            Element{
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ){
                    Text(
                        text = "${state.habits.size}",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = regular,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    )
                    Box(
                        Modifier.wrapContentWidth()
                            .height(20.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable{
                                showHabits = true
                                showingHabitList = state.habits
                            },
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "see all",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = regular,
                                fontWeight = FontWeight.Light,
                                fontSize = 12.sp
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Total Habits",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = regular,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
            }

            //on track and off track
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Element(Modifier.weight(1f)) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ){
                        Text(
                            text = "${state.onTrack.size}",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontFamily = regular,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        )
                        Box(
                            Modifier.wrapContentWidth()
                                .height(20.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable{
                                    showHabits = true
                                    showHabitSubTitle = "On Track"
                                    showingHabitList = state.onTrack
                                },
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = "see details",
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontFamily = regular,
                                    fontWeight = FontWeight.Light,
                                    fontSize = 12.sp
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        Text(
                            text = "On Track",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onSecondary,
                                fontFamily = regular,
                                fontWeight = FontWeight.Light,
                                fontSize = 16.sp
                            )
                        )
                        Icon(
                            painter = painterResource(R.drawable.ontrack_icon),
                            tint = MaterialTheme.colorScheme.scrim,
                            contentDescription = "on track",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Element(Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ){
                        Text(
                            text = "${state.offTrack.size}",
                            style = TextStyle(
                                color = colorResource(R.color.yellow),
                                fontFamily = regular,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        )
                        Box(
                            Modifier.wrapContentWidth()
                                .height(20.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable{
                                    showHabits = true
                                    showHabitSubTitle = "Off Track"
                                    showingHabitList = state.offTrack
                                },
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = "see details",
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontFamily = regular,
                                    fontWeight = FontWeight.Light,
                                    fontSize = 12.sp
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        Text(
                            text = "Off Track",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onSecondary,
                                fontFamily = regular,
                                fontWeight = FontWeight.Light,
                                fontSize = 16.sp
                            )
                        )
                        Icon(
                            painter = painterResource(R.drawable.offtrack_icon),
                            tint = MaterialTheme.colorScheme.scrim,
                            contentDescription = "off track",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            //at risk and closed
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Element(Modifier.weight(1f)) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ){
                        Text(
                            text = "${state.AtRisk.size}",
                            style = TextStyle(
                                color = colorResource(R.color.delete_red),
                                fontFamily = regular,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        )
                        Box(
                            Modifier.wrapContentWidth()
                                .height(20.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable{
                                    showHabits = true
                                    showHabitSubTitle = "At Risk"
                                    showingHabitList = state.AtRisk
                                },
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = "see details",
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontFamily = regular,
                                    fontWeight = FontWeight.Light,
                                    fontSize = 12.sp
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        Text(
                            text = "At Risk",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onSecondary,
                                fontFamily = regular,
                                fontWeight = FontWeight.Light,
                                fontSize = 16.sp
                            )
                        )
                        Icon(
                            painter = painterResource(R.drawable.atrisk_icon),
                            tint = MaterialTheme.colorScheme.scrim,
                            contentDescription = "at risk",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Element(Modifier.weight(1f)) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ){
                        Text(
                            text = "${state.closed.size}",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                                fontFamily = regular,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        )
                        Box(
                            Modifier.wrapContentWidth()
                                .height(20.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable{
                                    showHabits = true
                                    showHabitSubTitle = "Closed"
                                    showingHabitList = state.closed
                                },
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = "see details",
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontFamily = regular,
                                    fontWeight = FontWeight.Light,
                                    fontSize = 12.sp
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        Text(
                            text = "Closed",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onSecondary,
                                fontFamily = regular,
                                fontWeight = FontWeight.Light,
                                fontSize = 16.sp
                            )
                        )
                        Icon(
                            painter = painterResource(R.drawable.closed_icon),
                            tint = MaterialTheme.colorScheme.scrim,
                            contentDescription = "closed",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            //efforts stat
            Element{
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "My Efforts",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontFamily = regular,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .wrapContentHeight()
                                .clip(RoundedCornerShape(5.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable {
                                    showGraphTypeChoose = true
                                },
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = state.showedGraphType.toName(),
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontFamily = regular,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp
                                ),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                    CustomChart(
                        graphColor = MaterialTheme.colorScheme.primary,
//                        infos = state.showedEfforts
                        infos = state.showedEfforts
                    )
                }
            }

        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseGraphType(
    types : List<GraphType> = emptyList(),
    onSelected : (GraphType) -> Unit,
    selected : GraphType,
    onDismiss : () -> Unit,
    sheetState: SheetState,
    modifier: Modifier = Modifier
) {

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 70.dp,top = 30.dp)
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            types.forEach {
                Box(
                    Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(15.dp))
                        .background(
                            if(selected == it) MaterialTheme.colorScheme.primary
                            else Color.Transparent
                        )
                        .clickable{
                            onSelected(it)
                            onDismiss()
                        },
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = it.toName(),
                        style = TextStyle(
                            color = if(selected == it) MaterialTheme.colorScheme.inverseOnSurface
                            else MaterialTheme.colorScheme.onPrimary,
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