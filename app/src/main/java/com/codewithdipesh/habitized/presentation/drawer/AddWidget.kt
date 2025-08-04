package com.codewithdipesh.habitized.presentation.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.presentation.addscreen.component.AddScreenTopBar
import com.codewithdipesh.habitized.ui.theme.playfair
import com.codewithdipesh.habitized.ui.theme.regular

@Composable
fun AddWidgetScreen(
    modifier: Modifier = Modifier,
    navController : NavController
){
    val color = MaterialTheme.colorScheme.onPrimary
    val scrollState = rememberScrollState()
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AddScreenTopBar(
                isShowingLeftIcon = true,
                leftIcon = {
                    IconButton(
                        onClick = {navController.navigateUp()},
                        modifier = Modifier.padding(top = 30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .verticalScroll(scrollState)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary.copy(0.3f),
                            MaterialTheme.colorScheme.primary.copy(0.2f),
                            MaterialTheme.colorScheme.primary.copy(0.1f),
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                        )
                    )
                )
                .padding(innerPadding)
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ){
            Text(
                text = "Widget Process",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = playfair,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 28.sp
                )
            )
            Spacer(Modifier.height(40.dp))
            Text(
                text = "1) Tap and Hold on Your Home Screen",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = playfair,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "2) Choose the Widgets Options among them",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = playfair,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            )
            Image(
                painter = painterResource(R.drawable.widget_instruction_1),
                contentDescription = null,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "3) Choose Habitized app in the app list",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = playfair,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            )
            Image(
                painter = painterResource(R.drawable.widget_instruction_2),
                contentDescription = null,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "4) Select the widget type among all the available",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = playfair,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            )
            Image(
                painter = painterResource(R.drawable.widget_instruction_3),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Text(
                text = "Now U have successfully added a habitized widget in home screen",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = playfair,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            )
            Spacer(Modifier.height(20.dp))
        }
    }
}
