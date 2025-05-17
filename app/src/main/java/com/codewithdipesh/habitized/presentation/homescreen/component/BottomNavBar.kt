package com.codewithdipesh.habitized.presentation.homescreen.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHost
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.presentation.navigation.Screen
import com.codewithdipesh.habitized.ui.theme.regular
import org.w3c.dom.Text

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    selectedScreen: Screen,
    isShowingAddOption : Boolean,
    onAddClick: () -> Unit = {}
){
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .height(120.dp),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .wrapContentHeight()
                .fillMaxWidth()
                .then(
                    if(isShowingAddOption) Modifier.blur(16.dp)
                    else Modifier
                )
        ){
            HorizontalDivider(
                thickness = 2.dp,
                color = colorResource(R.color.secondary_gray)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(85.dp)
                    .fillMaxWidth()
            ) {
                NavItem(
                    text = "Home",
                    icon = painterResource(R.drawable.home),
                    isSelected = selectedScreen == Screen.Home,
                    onClick = {
                        //todo nav to home screen
                    }
                )

                NavItem(
                    text = "Habits",
                    icon = painterResource(R.drawable.habits),
                    isSelected = selectedScreen == Screen.Habits,
                    onClick = {
                        //todo nav to habit screen
                    }
                )
            }
        }
        //add button
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(
                    if(isShowingAddOption) colorResource(R.color.primary)
                    else Color.White
                )
                .align(Alignment.TopCenter)
                .clickable {
                    onAddClick()
                },
            contentAlignment = Alignment.Center
        ) {
            val rotation by animateFloatAsState(
                targetValue = if (isShowingAddOption) 45f else 0f,
                animationSpec = tween(durationMillis = 300),
                label = "rotation"
            )

            Icon(
                painter = painterResource(R.drawable.add_big), // keep just one icon
                contentDescription = "add",
                tint = if (isShowingAddOption) colorResource(R.color.white) else colorResource(R.color.primary),
                modifier = Modifier.graphicsLayer {
                    rotationZ = rotation
                }
            )
        }
    }
}


@Composable
fun NavItem(
    text: String,
    icon : Painter,
    isSelected : Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = if (isSelected) Color.White else colorResource(R.color.light_gray)
    Column(
        modifier = Modifier
            .clickable{
              onClick()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Icon(
            painter = icon,
            contentDescription = text,
            tint = color
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = text,
            style = TextStyle(
                color = color,
                fontFamily = regular,
                fontWeight = FontWeight.Normal
            )
        )
    }
}

