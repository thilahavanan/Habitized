package com.codewithdipesh.habitized.presentation.homescreen.component
import com.codewithdipesh.habitized.R

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.ui.theme.regular

@Composable
fun AddOptionButton(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    onClick: () -> Unit = {},
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable{
                onClick()
            },
        color = colorResource(R.color.tertiary_gray),
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = regular,
                    fontWeight = FontWeight.Normal
                )
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = TextStyle(
                    color = colorResource(R.color.little_white),
                    fontSize = 10.sp,
                    fontFamily = regular,
                    fontWeight = FontWeight.Light
                )
            )
        }
    }
}