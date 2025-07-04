package com.codewithdipesh.habitized.presentation.goalscreen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.LocalAutofillHighlightColor
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.domain.model.HabitType
import com.codewithdipesh.habitized.domain.model.ImageProgress
import com.codewithdipesh.habitized.presentation.addscreen.component.AddScreenTopBar
import com.codewithdipesh.habitized.presentation.addscreen.component.Button
import com.codewithdipesh.habitized.presentation.goalscreen.components.CustomChart
import com.codewithdipesh.habitized.presentation.goalscreen.components.GraphType
import com.codewithdipesh.habitized.presentation.goalscreen.components.toName
import com.codewithdipesh.habitized.presentation.habitscreen.HabitViewModel
import com.codewithdipesh.habitized.presentation.habitscreen.components.AddEditImageProgress
import com.codewithdipesh.habitized.presentation.habitscreen.components.CalendarStat
import com.codewithdipesh.habitized.presentation.habitscreen.components.DeleteAlertBox
import com.codewithdipesh.habitized.presentation.habitscreen.components.Element
import com.codewithdipesh.habitized.presentation.habitscreen.components.ImageElement
import com.codewithdipesh.habitized.presentation.habitscreen.components.ShowImage
import com.codewithdipesh.habitized.presentation.navigation.Screen
import com.codewithdipesh.habitized.presentation.progress.components.FireAnimation
import com.codewithdipesh.habitized.presentation.util.IntToWeekDayMap
import com.codewithdipesh.habitized.presentation.util.getOriginalColorFromKey
import com.codewithdipesh.habitized.presentation.util.getThemedColorFromKey
import com.codewithdipesh.habitized.presentation.util.toWord
import com.codewithdipesh.habitized.ui.theme.playfair
import com.codewithdipesh.habitized.ui.theme.regular
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Locale
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
    LaunchedEffect(state.effortList) {
        Log.d("stats",state.effortList.toString())
    }

    BackHandler {
        navController.navigateUp()
        viewmodel.clearUi()
    }


    LaunchedEffect(Unit) {
        scope.launch {
            viewmodel.init(id)
        }
    }

    var showGraphTypeChoose by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                //left icon
                IconButton(
                    onClick = {
                        navController.navigateUp()
                        viewmodel.clearUi()
                    },
                    modifier = Modifier
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
                        fontFamily = playfair,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.padding(top = 40.dp)
                )
                //options(share,edit,delete
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    IconButton(
                        onClick = {
                           //todo edit
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
                            //todo delete
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
            //title  and target
            Element(){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
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
                    //target date
                    //todo

                }
                if(state.description != ""){
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
            if(id != null){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    Element(Modifier.weight(1f)){
                        Text(
                            text = "${state.habits.size}",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontFamily = regular,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                        )
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
                    Element (Modifier.weight(1f)){
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)){
                            Text(
                                text = "${LocalDate.now().toEpochDay() - state.startDate!!.toEpochDay()}",
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontFamily = regular,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 20.sp
                                )
                            )
                            Text(
                                text = "days",
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.scrim,
                                    fontFamily = regular,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 20.sp
                                )
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Passed",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontFamily = regular,
                                fontWeight = FontWeight.Light,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }
            else{
                Element{
                    Text(
                        text = "${state.habits.size}",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = regular,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    )
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
            }

            //on track and off track
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Element(Modifier.weight(1f)) {
                    Text(
                        text = "${state.onTrack.size}",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = regular,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    )
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
                    Text(
                        text = "${state.offTrack.size}",
                        style = TextStyle(
                            color = colorResource(R.color.yellow),
                            fontFamily = regular,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    )
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
                    Text(
                        text = "${state.AtRisk.size}",
                        style = TextStyle(
                            color = colorResource(R.color.delete_red),
                            fontFamily = regular,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    )
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
                    Text(
                        text = "${state.closed.size}",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                            fontFamily = regular,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    )
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