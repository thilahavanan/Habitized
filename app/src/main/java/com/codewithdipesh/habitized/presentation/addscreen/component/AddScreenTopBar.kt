package com.codewithdipesh.habitized.presentation.addscreen.component

import android.widget.Space
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.ui.theme.ndot
import com.codewithdipesh.habitized.ui.theme.regular

@Composable
fun AddScreenTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    title : String? = null,
    icon : @Composable () -> Unit
){
    Box(
        modifier = Modifier.fillMaxWidth()
            .height(80.dp),
        contentAlignment = Alignment.CenterStart
    ){
        Row {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(top = 30.dp)
            ) {
                icon()
            }
            Spacer(Modifier.width(16.dp))
            title?.let {
                Text(
                    text = title,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = regular,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                    modifier = Modifier
                        .padding(top = 40.dp)
                )
            }
        }
    }
}