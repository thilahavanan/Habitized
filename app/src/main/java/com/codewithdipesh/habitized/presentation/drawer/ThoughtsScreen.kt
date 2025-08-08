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
fun ThoughtsScreen(
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
                text = "My Thoughts",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = playfair,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 28.sp
                )
            )
            Text(
                text = "(estimate 30 sec)",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary.copy(0.5f),
                    fontFamily = regular,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp
                )
            )

            Spacer(Modifier.height(40.dp))

            Text(
                text = buildAnnotatedString {
                    val paragraphStyle = ParagraphStyle(
                        textAlign = TextAlign.Justify,
                        lineHeight = 24.sp,
                        textIndent = TextIndent(firstLine = 0.sp, restLine = 0.sp)
                    )

                    val text = "I made this app because I've always been into tracking habits and to-dos, but most apps I used were either **too strict**, **too minimal**, or just **didn't feel right**."

                    // Apply paragraph style to entire text
                    withStyle(paragraphStyle) {
                        // Split by ** for bold highlighting
                        val parts = text.split("**")
                        parts.forEachIndexed { index, part ->
                            if (index % 2 == 1) {
                                // Bold text
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(part)
                                }
                            } else {
                                // Normal text
                                withStyle(SpanStyle(color = color.copy(0.75f) )) {
                                    append(part)
                                }
                            }
                        }
                    }
                },
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = playfair,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            )

            Image(
                painter = painterResource(R.drawable.habit_types),
                contentDescription = "habit types",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = buildAnnotatedString {
                    val paragraphStyle = ParagraphStyle(
                        textAlign = TextAlign.Justify,
                        lineHeight = 24.sp
                    )

                    val text = "While observing my own routines, I realized people follow different kinds of habits. Some just **repeat every day** (like **brushing** or **drinking water**), some are **count-based** (drinking **10 glasses** of water), and some are **time-based** (meditation for **10 minutes**). Study focuses on **pomodoro method**, and focusing with **goal connection gives dopamine boost**."

                    withStyle(paragraphStyle) {
                        val parts = text.split("**")
                        parts.forEachIndexed { index, part ->
                            if (index % 2 == 1) {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(part)
                                }
                            } else {
                                withStyle(SpanStyle(color = color.copy(0.75f) )) {
                                    append(part)
                                }
                            }
                        }
                    }
                },
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = playfair,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = buildAnnotatedString {
                    val paragraphStyle = ParagraphStyle(
                        textAlign = TextAlign.Justify,
                        lineHeight = 24.sp
                    )

                    val text = "So, I added different ways to log habits — even a **simple image log** if you don't want to type much. Sometimes it's easier to just **click a photo and track progress visually**."

                    withStyle(paragraphStyle) {
                        val parts = text.split("**")
                        parts.forEachIndexed { index, part ->
                            if (index % 2 == 1) {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(part)
                                }
                            } else {
                                withStyle(SpanStyle(color = color.copy(0.75f) )) {
                                    append(part)
                                }
                            }
                        }
                    }
                },
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = playfair,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = buildAnnotatedString {
                    val paragraphStyle = ParagraphStyle(
                        textAlign = TextAlign.Justify,
                        lineHeight = 24.sp
                    )

                    val text = "I also included a to-do system that lets you **plan tasks for future days**, **add subtasks**, and even **start focus sessions**. It helps in setting clear steps when you don't want to forget what to do."

                    withStyle(paragraphStyle) {
                        val parts = text.split("**")
                        parts.forEachIndexed { index, part ->
                            if (index % 2 == 1) {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(part)
                                }
                            } else {
                                withStyle(SpanStyle(color = color.copy(0.75f) )) {
                                    append(part)
                                }
                            }
                        }
                    }
                },
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = playfair,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = buildAnnotatedString {
                    val paragraphStyle = ParagraphStyle(
                        textAlign = TextAlign.Justify,
                        lineHeight = 24.sp
                    )

                    val text = "I didn't build this as a productivity tool. I just wanted a **flexible habit + todo app** that actually **fits how people live**, not **force them into a system**."

                    withStyle(paragraphStyle) {
                        val parts = text.split("**")
                        parts.forEachIndexed { index, part ->
                            if (index % 2 == 1) {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(part)
                                }
                            } else {
                                withStyle(SpanStyle(color = color.copy(0.75f) )) {
                                    append(part)
                                }
                            }
                        }
                    }
                },
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = playfair,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )

            Spacer(Modifier.height(20.dp))
        }
    }
}
