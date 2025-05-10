package com.codewithdipesh.habitized.presentation.addscreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.domain.model.HabitType
import com.codewithdipesh.habitized.presentation.homescreen.HomeScreenOption
import com.codewithdipesh.habitized.ui.theme.ndot
import java.util.Locale

@Composable
fun TypeSelector(
    selectedType: HabitType,
    onTypeSelected: (HabitType) -> Unit,
    modifier: Modifier = Modifier,
){

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        HabitType.getHabitTypes().forEach {type->
            Box(
                modifier = Modifier
                    .width(screenWidth/4)
                    .weight(1f)
                    .height(30.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(color =
                        if (selectedType == type) Color.White
                        else colorResource(R.color.secondary_gray),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .clickable{
                        onTypeSelected(type)
                    },
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = type.displayName.capitalize(Locale.ROOT),
                    style = TextStyle(
                        color = if (selectedType == type) colorResource(R.color.black)
                        else colorResource(R.color.white),
                        fontSize = 14.sp,
                        fontFamily = ndot
                    )
                )
            }
        }

    }

}