package com.codewithdipesh.habitized.presentation.homescreen.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.ui.theme.playfair
import com.codewithdipesh.habitized.ui.theme.regular
import kotlinx.coroutines.launch

@Composable
fun AppDrawer(
    modifier: Modifier = Modifier,
    secondSectionItems : List<DrawerItem>,
    state : DrawerState,
    onFollowClick : () -> Unit = {},
    onGithubClick : () -> Unit = {},
    content : @Composable () -> Unit
){
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = state,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest
            ) {
                Box(modifier = Modifier.fillMaxSize()){
                    //main content
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(horizontal = 16.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        //heading
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(40.dp)) {
                                Image(
                                    painter = painterResource(R.drawable.app_logo),
                                    contentDescription = "logo",
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentScale = ContentScale.Fit
                                )
                            }
                            Text(
                                text = "habitized",
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontFamily = playfair,
                                    fontWeight = FontWeight.Bold,
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 24.sp
                                )
                            )
                        }
                        Text(
                            text = "Made for students and professionals \uD83C\uDFAF",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                                fontFamily = regular,
                                fontWeight = FontWeight.Light,
                                fontSize = 14.sp
                            )
                        )
                        Spacer(Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(MaterialTheme.colorScheme.surfaceBright)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ){
                                secondSectionItems.forEach {
                                    NavigationDrawerItem(
                                        label = {
                                            Text(
                                                text =it.text,
                                                style = TextStyle(
                                                    color = MaterialTheme.colorScheme.onPrimary,
                                                    fontFamily = regular,
                                                    fontWeight = FontWeight.Normal,
                                                    fontSize = 16.sp
                                                )
                                            )
                                        },
                                        icon = {
                                            Icon(
                                                painter = painterResource(it.icon),
                                                contentDescription = it.text,
                                                tint = MaterialTheme.colorScheme.onPrimary
                                            )
                                        },
                                        selected = false,
                                        onClick = {
                                            scope.launch{
                                                it.onclick()
                                                state.close()
                                            }
                                        }
                                    )
                                }
                            }

                        }

                    }
                    //own logo and follow
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = 16.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                       //logo
                       Image(
                           painter = painterResource(R.drawable.my_logo),
                           contentDescription = "logo",
                           modifier = Modifier.size(50.dp)
                                 .clip(RoundedCornerShape(50.dp))
                       )
                        Spacer(Modifier.height(8.dp))
                       //Text
                       Text(
                           text = "Made with lots of \uD83D\uDC9A",
                           style = TextStyle(
                               color = MaterialTheme.colorScheme.onPrimary,
                               fontFamily = regular,
                               fontWeight = FontWeight.Bold,
                               fontSize = 16.sp
                           )
                       )
                        Text(
                            text = "by Dipesh",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontFamily = regular,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                        Spacer(Modifier.height(8.dp))
                        //github button
                            Box(modifier = Modifier.wrapContentSize()
                                .clip(RoundedCornerShape(25.dp))
                                .background(Color.White)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary.copy(0.2f),
                                            MaterialTheme.colorScheme.primary.copy(0.4f),
                                            MaterialTheme.colorScheme.primary.copy(0.6f),
                                            MaterialTheme.colorScheme.primary.copy(0.8f),
                                            MaterialTheme.colorScheme.primary
                                        )
                                    )
                                )
                                .clickable{ onGithubClick() },
                                contentAlignment = Alignment.Center
                            ){
                                Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ){
                                    Text(
                                        text = "Give it a star ",
                                        style = TextStyle(
                                            color = Color.Black,
                                            fontFamily = regular,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 12.sp
                                        )
                                    )
                                    Icon(
                                        painter = painterResource(R.drawable.github_icon),
                                        contentDescription = "github",
                                        tint = Color.Black,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        Spacer(Modifier.height(8.dp))
                       //follow button
                        Box(modifier = Modifier.wrapContentSize()
                            .clip(RoundedCornerShape(25.dp))
                            .background(MaterialTheme.colorScheme.onTertiary)
                            .clickable{
                                onFollowClick()
                            },
                            contentAlignment = Alignment.Center
                        ){
                            Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ){
                                Text(
                                    text = "Follow me on ",
                                    style = TextStyle(
                                        color = MaterialTheme.colorScheme.inverseOnSurface,
                                        fontFamily = regular,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 12.sp
                                    )
                                )
                                Icon(
                                    painter = painterResource(R.drawable.x_icon),
                                    contentDescription = "x",
                                    tint = MaterialTheme.colorScheme.inverseOnSurface,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                    }
                }
            }
        }
    ) {
        content()
    }
}
