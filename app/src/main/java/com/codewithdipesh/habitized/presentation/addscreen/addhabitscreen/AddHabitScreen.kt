package com.codewithdipesh.habitized.presentation.addscreen.addhabitscreen

import android.R.attr.value
import android.widget.Space
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.domain.model.HabitType
import com.codewithdipesh.habitized.presentation.addscreen.AddViewModel
import com.codewithdipesh.habitized.presentation.addscreen.component.AddScreenTopBar
import com.codewithdipesh.habitized.presentation.addscreen.component.ColorChoser
import com.codewithdipesh.habitized.presentation.addscreen.component.DashedDivider
import com.codewithdipesh.habitized.presentation.addscreen.component.InputElement
import com.codewithdipesh.habitized.presentation.addscreen.component.TypeSelector
import com.codewithdipesh.habitized.ui.theme.ndot
import com.codewithdipesh.habitized.ui.theme.regular

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewmodel: AddViewModel
) {
    val state by viewmodel.habitUiState.collectAsState()

    BackHandler {
        navController.navigateUp()
    }

    Scaffold(
        containerColor = colorResource(R.color.background_black),
        topBar = {
            AddScreenTopBar(
                onBackClick = {navController.navigateUp()}
            )
        }
    ){innerPadding->

        val sheetstate = rememberModalBottomSheetState()

        if(state.colorOptionAvailable){
            ColorChoser(
                onColorSelected = {
                    viewmodel.setColor(it)
                },
                onDismiss = {
                    viewmodel.toggleColorOption()
                },
                sheetState = sheetstate,
                selectedColor = state.color,
                colors = state.colorOptions,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            //heading
            Row{
                Text(
                    text = "Create",
                    style = TextStyle(
                        color = Color.White,
                        fontFamily = regular,
                        fontWeight = FontWeight.Normal,
                        fontSize = 22.sp
                    )
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "Habit",
                    style = TextStyle(
                        color = Color.White,
                        fontFamily = ndot,
                        fontWeight = FontWeight.Normal,
                        fontSize = 26.sp
                    )
                )
            }
            //title
            InputElement {
                Box {
                    BasicTextField(
                        value = state.title,
                        onValueChange = {
                            viewmodel.setTitle(it)
                        },
                        textStyle = TextStyle(
                            color = Color.White,
                            fontFamily = regular,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        ),
                        singleLine = true,
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
            InputElement {
                Box {
                    BasicTextField(
                        value = state.description,
                        onValueChange = {
                            viewmodel.setDescription(it)
                        },
                        textStyle = TextStyle(
                            color = Color.White,
                            fontFamily = regular,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        ),
                        singleLine = true,
                        maxLines = 1,
                        cursorBrush = SolidColor(colorResource(R.color.primary)),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Placeholder shown only when title is empty
                    if (state.description.isEmpty()) {
                        Text(
                            text = "Description",
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
            //choose color
            InputElement {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    //Text
                    Text(
                        text = "Color",
                        style = TextStyle(
                            color = colorResource(R.color.white), // make it look like a placeholder
                            fontFamily = regular,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        )
                    )
                    //color chooser
                    Box(
                        modifier = modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(colorResource(id = state.color))
                            .clickable{
                                viewmodel.toggleColorOption()
                            }
                    )

                }
            }
            //choose type
            Text(
                text = "Type",
                style = TextStyle(
                    color = Color.White,
                    fontFamily = regular,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            )
            Spacer(Modifier.height(16.dp))
            TypeSelector(
                selectedType = state.type,
                onTypeSelected = {
                    viewmodel.setType(it)
                }
            )
            Spacer(Modifier.height(20.dp))
            //choose param
            if(state.type != HabitType.OneTime){
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Column(
                        modifier = Modifier.width(64.dp)
                    ){
                        BasicTextField(
                            value = if(state.countTarget != null) state.countTarget.toString() else "",
                            onValueChange = {
                                if(it.isEmpty() || it.toIntOrNull() != null){
                                    if(it.isNotEmpty()){
                                        viewmodel.setTargetCount(it.toInt())
                                    }else{
                                        viewmodel.setTargetCount(0)
                                    }
                                }
                            },
                            textStyle = TextStyle(
                                color = Color.White,
                                fontFamily = regular,
                                fontWeight = FontWeight.Normal,
                                fontSize = 18.sp
                            ),
                            singleLine = true,
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(4.dp))
                        DashedDivider(
                            thickness = 2.dp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = state.countParam?.displayName ?: "choose",
                        style = TextStyle(
                            color = Color.White,
                            fontFamily = regular,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        )
                    )
                    IconButton(
                        onClick = {
                            //TODO OPEN SELECT PARAMS
                        }
                    ){
                        Icon(
                            painter = painterResource(id = R.drawable.option_up_down_icon),
                            contentDescription = "choose params",
                            tint = Color.White
                        )
                    }

                }
            }

        }
    }

}