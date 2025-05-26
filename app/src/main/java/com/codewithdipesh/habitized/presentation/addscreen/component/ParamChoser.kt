package com.codewithdipesh.habitized.presentation.addscreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.domain.model.CountParam
import com.codewithdipesh.habitized.ui.theme.regular

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParamChoser(
    modifier: Modifier = Modifier,
    onParamSelected: (CountParam) -> Unit,
    onDismiss : ()->Unit,
    sheetState: SheetState,
    selectedParam: CountParam,
    params: List<CountParam>,
){

//    var choosedParam by remember {
//        mutableStateOf(selectedParam)
//    }

    val scrollState = rememberScrollState()

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                params.forEach{param->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(
                                color = if(param == selectedParam) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clip(RoundedCornerShape(10.dp))
                            .clickable{
                                onParamSelected(param)
                                onDismiss()
                            },
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = param.displayName,
                            style = TextStyle(
                                fontFamily = regular,
                                color = if(param == selectedParam) MaterialTheme.colorScheme.inverseOnSurface else MaterialTheme.colorScheme.onPrimary,
                                fontSize = 16.sp
                            ),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }

//            //button
//            Button(
//                onClick = {
//                    onDismiss()
//                    onParamSelected(choosedParam)
//                },
//                bgColor = colorResource(R.color.primary),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 16.dp)
//            ){
//                Text(
//                    text = "Confirm ",
//                    style = TextStyle(
//                        fontFamily = regular,
//                        color = MaterialTheme.colorScheme.inverseOnSurface,
//                        fontSize = 16.sp
//                    )
//                )
//            }

        }
    }
}