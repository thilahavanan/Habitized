package com.codewithdipesh.habitized.presentation.addscreen.addGoalScreen

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.presentation.addscreen.AddViewModel
import com.codewithdipesh.habitized.presentation.addscreen.component.AddScreenTopBar
import com.codewithdipesh.habitized.presentation.addscreen.component.InputElement
import com.codewithdipesh.habitized.presentation.addscreen.component.SlidingButton
import com.codewithdipesh.habitized.ui.theme.ndot
import com.codewithdipesh.habitized.ui.theme.regular
import kotlinx.coroutines.launch
@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun AddGoalScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewmodel: AddViewModel
) {
    val state by viewmodel.habitUiState.collectAsState()
    val scrollstate = rememberScrollState()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    BackHandler {
        navController.navigateUp()
    }

    LaunchedEffect(viewmodel.uiEvent) {
        viewmodel.uiEvent.collect {
            scope.launch {
                if(it == "Goal Created Successfully"){
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    navController.navigateUp()
                }else{
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AddScreenTopBar(
                onBackClick = {navController.navigateUp()}
            )
        },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(start = 30.dp)
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .clickable{
                        scope.launch {
                            //add goal
                        }
                    },
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "Done",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                        fontFamily = regular,
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp
                    )
                )
            }
        }
    ){innerPadding->

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .padding(bottom = 120.dp)
                .verticalScroll(scrollstate),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            //heading
            Row{
                Text(
                    text = "Create",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = regular,
                        fontWeight = FontWeight.Normal,
                        fontSize = 22.sp
                    )
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "Goal",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = ndot,
                        fontWeight = FontWeight.Normal,
                        fontSize = 26.sp
                    )
                )
            }
            //title
            InputElement(
                color = if(state.title.isEmpty()) MaterialTheme.colorScheme.surface
                else MaterialTheme.colorScheme.secondary
            ){
                Box {
                    BasicTextField(
                        value = state.title,
                        onValueChange = {
                            //onchange
                        },
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = regular,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        ),
                        singleLine = false,
                        maxLines = 1,
                        cursorBrush = SolidColor(colorResource(R.color.primary)),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Placeholder shown only when title is empty
                    if (state.title.isEmpty()) {
                        Text(
                            text = "Title",
                            style = TextStyle(
                                color = colorResource(R.color.light_gray), // make it look like a placeholder
                                fontFamily = regular,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }
            //description
            InputElement(
                color = if(state.description.isEmpty()) MaterialTheme.colorScheme.surface
                else MaterialTheme.colorScheme.secondary
            ){
                Box {
                    BasicTextField(
                        value = state.description,
                        onValueChange = {
                            //todo
                        },
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = regular,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        ),
                        singleLine = false,
                        maxLines = 5,
                        cursorBrush = SolidColor(colorResource(R.color.primary)),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Placeholder shown only when title is empty
                    if (state.description.isEmpty()) {
                        Text(
                            text = "Describe your Goal",
                            style = TextStyle(
                                color = colorResource(R.color.light_gray), // make it look like a placeholder
                                fontFamily = regular,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }
            //Target Date
            InputElement{
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Target Date",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontFamily = regular,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            )
                        )
                        SlidingButton(
                            isSelected = state.isShowReminderTime,
                            onToggle = {
                                //todo
                            }
                        )
                    }

                }
            }

        }
    }

}


