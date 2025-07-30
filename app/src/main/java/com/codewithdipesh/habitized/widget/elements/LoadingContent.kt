package com.codewithdipesh.habitized.widget.elements

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.text.Text
import com.codewithdipesh.habitized.MainActivity


@Composable
fun LoadingContent() {
    Box(
        modifier = GlanceModifier
            .cornerRadius(12.dp)
            .background(ColorProvider(
                day = Color(0XFFFFFFFF),
                night = Color(0XFF111111)
            ))
            .fillMaxSize()
            .clickable(actionStartActivity<MainActivity>()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                color = ColorProvider(
                    day = Color(0XFFEEA445),
                    night = Color(0XFFEEA445)
                )
            )
            Spacer(GlanceModifier.height(8.dp))
            Text(
                text = "Pls click to reload...",
                style = androidx.glance.text.TextStyle(
                    color = ColorProvider(
                        day = Color.Gray,
                        night = Color.Gray
                    ),
                    fontSize = 12.sp
                )
            )
        }
    }
}
