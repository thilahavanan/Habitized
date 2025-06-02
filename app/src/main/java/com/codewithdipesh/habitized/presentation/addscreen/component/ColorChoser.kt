package com.codewithdipesh.habitized.presentation.addscreen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.ui.theme.regular

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorChoser(
    modifier: Modifier = Modifier,
    onColorSelected: (String) -> Unit,
    onDismiss : ()->Unit,
    sheetState: SheetState,
    selectedColor: String,
    colors: Map<Int,String>
){

    var choosedColor by remember {
        mutableStateOf(selectedColor)
    }

    ModalBottomSheet(
        onDismissRequest = {
            //ondismiss
            onDismiss()
        },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                colors.forEach { color ->
                    ColorChoserItem(
                        color = color.key,
                        isSelected = color.value == choosedColor,
                        onClick = {
                            choosedColor = colors.get(color.key)!!
                        }
                    )
                }
            }

            //button
            Button(
                onClick = {
                    onDismiss()
                    onColorSelected(choosedColor)
                },
                bgColor = colorResource(R.color.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ){
                Text(
                    text = "Confirm color",
                    style = TextStyle(
                        fontFamily = regular,
                        color = colorResource(R.color.white),
                        fontSize = 16.sp
                    )
                )
            }

        }
    }
}