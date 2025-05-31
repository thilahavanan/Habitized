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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.compose.ui.text.font.Font
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
    selectedScreen: Screen,
    isShowingAddOption: Boolean,
    onAddClick: () -> Unit = {},
    onNavigate: (Screen) -> Unit = {},
    modifier : Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
            .then(if (isShowingAddOption) Modifier.blur(16.dp) else Modifier)
    ) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background,
            tonalElevation = 0.dp
        ) {
            NavigationBarItem(
                selected = selectedScreen == Screen.Home,
                onClick = { onNavigate(Screen.Home) },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.home),
                        contentDescription = "Home",
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface
                )

            )

            // Spacer for central Add button
            Spacer(modifier = Modifier.width(16.dp))

            // Custom Add button (centered manually)
            Box(modifier = Modifier
                    .size(48.dp)
                    .offset(y = (-8).dp) // push upward
                    .clip(shape = CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable { onAddClick() }, contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.inverseOnSurface
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            NavigationBarItem(
                selected = selectedScreen == Screen.Habits,
                onClick = { onNavigate(Screen.Habits) },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.habits),
                        contentDescription = "Habits"
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}
