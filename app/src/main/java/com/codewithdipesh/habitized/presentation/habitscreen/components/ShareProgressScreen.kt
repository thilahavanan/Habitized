package com.codewithdipesh.habitized.presentation.habitscreen.components

import com.codewithdipesh.habitized.R
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Spacer
import androidx.navigation.NavController
import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.domain.model.Habit
import com.codewithdipesh.habitized.domain.model.HabitProgress
import com.codewithdipesh.habitized.domain.model.HabitType
import com.codewithdipesh.habitized.domain.model.Status
import com.codewithdipesh.habitized.presentation.habitscreen.Background
import com.codewithdipesh.habitized.presentation.habitscreen.HabitViewModel
import com.codewithdipesh.habitized.presentation.habitscreen.ShareCardUI
import com.codewithdipesh.habitized.presentation.habitscreen.components.Element
import com.codewithdipesh.habitized.presentation.progress.components.FireAnimation
import com.codewithdipesh.habitized.presentation.progress.components.OverAllCell
import com.codewithdipesh.habitized.presentation.progress.components.WeeklyCell
import com.codewithdipesh.habitized.presentation.util.IntToWeekDayMap
import com.codewithdipesh.habitized.presentation.util.getAnimatedFireIcon
import com.codewithdipesh.habitized.presentation.util.getOriginalColorFromKey
import com.codewithdipesh.habitized.presentation.util.toWord
import com.codewithdipesh.habitized.ui.theme.instrumentSerif
import com.codewithdipesh.habitized.ui.theme.regular
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import dev.shreyaspatil.capturable.capturable
import dev.shreyaspatil.capturable.controller.CaptureController
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.YearMonth
import java.util.Locale
import kotlin.collections.chunked
import kotlin.collections.forEach

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeApi::class)
@Composable
fun ShareProgress(
    navController: NavController,
    viewModel : HabitViewModel,
    modifier: Modifier = Modifier
){
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = {3})
    val state by viewModel.shareCardState.collectAsState()
    val captureController = rememberCaptureController()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri:Uri? ->
        uri?.let {
            scope.launch(Dispatchers.IO) {
                try {
                    val bitmap = if (Build.VERSION.SDK_INT < 28) {
                        MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                    } else {
                        val source = ImageDecoder.createSource(context.contentResolver, it)
                        ImageDecoder.decodeBitmap(source)
                    }
                    viewModel.setImage(bitmap)
                    viewModel.setBackground(Background.Custom)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(getOriginalColorFromKey(state.colorKey).copy(0.2f))
            ){
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.align(Alignment.CenterStart)
                        .padding(top = 30.dp, start = 32.dp)
                        .clickable{
                            navController.navigateUp()
                        }
                )
                Text(
                    text = "Share",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = instrumentSerif,
                        fontStyle = FontStyle.Italic,
                        fontSize = 22.sp
                    ),
                    modifier = Modifier.align(Alignment.Center)
                        .padding(top = 30.dp)
                )

            }
        },
        containerColor = MaterialTheme.colorScheme.inverseOnSurface
    ){
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(getOriginalColorFromKey(state.colorKey).copy(0.2f))
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //heading
            Text(
                text = when(pagerState.currentPage){
                    0 -> "OverAll Progress Stat"
                    1 -> "Weekly Progress Stat"
                    2 -> "Habit Monthly Stat"
                    else -> ""
                },
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = regular,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            )
            Spacer(Modifier.height(8.dp))
            //subheading for monthly habit map
            if(pagerState.currentPage == 2){
                Text(
                    text = "You can change month by swiping left or right",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary.copy(0.5f),
                        fontFamily = regular,
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(Modifier.height(40.dp))
            //background select
            Row(modifier = Modifier.fillMaxWidth().padding(end = 24.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Choose Background",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = regular,
                        fontWeight = FontWeight.Light,
                        fontSize = 18.sp
                    )
                )
                Spacer(Modifier.width(8.dp))
                //default bg
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(getOriginalColorFromKey(state.colorKey))
                        .border(
                            width = 1.5.dp,
                            color = if(state.selectedBackground == Background.Default) MaterialTheme.colorScheme.onPrimary else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable{
                            viewModel.setBackground(Background.Default)
                        }
                )
                Spacer(Modifier.width(8.dp))
                //image
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.onSurface)
                        .border(
                            width = 1.5.dp,
                            color = if(state.selectedBackground == Background.Custom) MaterialTheme.colorScheme.onPrimary else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable{
                            if(state.selectedBackground == Background.Custom) {
                                galleryLauncher.launch("image/*")
                            }else{
                                if(state.imageBgBitmap == null) {
                                    galleryLauncher.launch("image/*")
                                }else{
                                    viewModel.setBackground(Background.Custom)
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ){
                    if(state.imageBgBitmap != null){
                        Image(
                            bitmap = state.imageBgBitmap!!.asImageBitmap(),
                            contentDescription = null,
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }else{
                        Icon(
                            painter = painterResource(R.drawable.baseline_insert_photo_24),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            HorizontalPager(
                state = pagerState
            ) { page ->
                Spacer(Modifier.width(24.dp))
                 when(page){
                     0 -> {
                         ShareItem(
                             color = getOriginalColorFromKey(state.colorKey),
                             selectedBackground = state.selectedBackground,
                             imageBgBitmap = state.imageBgBitmap,
                             captureController = captureController

                         ){
                             Box(
                                 modifier = Modifier
                                     .width(252.dp)
                                     .clip(RoundedCornerShape(15.dp))
                                     .background(
                                         if(state.selectedBackground == Background.Default) colorResource(R.color.secondary_gray)
                                         else Color.Transparent),
                                 contentAlignment = Alignment.Center
                             ){
                                 val width = 236
                                 Column(
                                     modifier = Modifier.fillMaxWidth()
                                         .padding(10.dp),
                                     horizontalAlignment = Alignment.CenterHorizontally,
                                     verticalArrangement = Arrangement.Center
                                 ){
                                     //title and streak
                                     Row(modifier = Modifier.fillMaxWidth(),
                                         horizontalArrangement = Arrangement.SpaceBetween,
                                         verticalAlignment = Alignment.CenterVertically
                                     ){
                                         Text(
                                             text = state.title,
                                             style = TextStyle(
                                                 color = Color.White,
                                                 fontFamily = instrumentSerif,
                                                 fontStyle = FontStyle.Italic,
                                                 fontSize = 16.sp
                                             )
                                         )
                                         Row(horizontalArrangement = Arrangement.spacedBy(2.dp),
                                             verticalAlignment = Alignment.CenterVertically
                                         ){
                                             Text(
                                                 text = state.currentStreak.toString(),
                                                 style = TextStyle(
                                                     color = Color.White,
                                                     fontFamily = regular,
                                                     fontWeight = FontWeight.Bold,
                                                     fontSize = 16.sp
                                                 )
                                             )
                                             FireAnimation(
                                                 colorKey = state.colorKey,
                                                 size = 30,
                                                 modifier = Modifier.padding(bottom = 8.dp)
                                             )
                                         }
                                     }
                                     Spacer(Modifier.height(8.dp))
                                     Row(
                                         modifier = Modifier.wrapContentSize()
                                     ){
                                         var habitDayList : List<LocalDate> = emptyList()

                                         when(state.frequency){
                                             Frequency.Daily -> {
                                                 habitDayList = state.OverAllDateRange
                                             }
                                             Frequency.Monthly -> {
                                                 habitDayList = state.OverAllDateRange.filter { state.daysOfMonth!!.contains(it.dayOfMonth) }
                                             }
                                             Frequency.Weekly -> {
                                                 habitDayList = state.OverAllDateRange.filter { state.daysOfWeek[it.dayOfWeek.value - 1] == 1 }
                                             }
                                             else ->{}
                                         }
                                         //filtering days for only after creating the habit
                                         habitDayList = habitDayList.filter { it >= state.startDate }

                                         state.OverAllDateRange.chunked(7).forEach { week->
                                             Column {
                                                 week.forEach { day ->
                                                     val progress = state.progressList.find { it.date == day }
                                                     OverAllCell(
                                                         size = width/19,
                                                         color = getOriginalColorFromKey(state.colorKey),
                                                         isActive = habitDayList.contains(day),
                                                         isSelect = (progress != null && progress.status == Status.Done)
                                                     )
                                                 }
                                             }
                                         }
                                     }
                                 }

                             }
                         }
                     }
                     1->{
                         ShareItem(
                             color = getOriginalColorFromKey(state.colorKey),
                             selectedBackground = state.selectedBackground,
                             imageBgBitmap = state.imageBgBitmap,
                             captureController = captureController
                         ) {
                             Box(
                                 modifier = Modifier
                                     .width(252.dp)
                                     .clip(RoundedCornerShape(15.dp))
                                     .background(
                                         if (state.selectedBackground == Background.Default) colorResource(
                                             R.color.secondary_gray
                                         )
                                         else Color.Transparent
                                     ),
                                 contentAlignment = Alignment.Center
                             ) {
                                 val width = 236
                                 Column(
                                     modifier = Modifier.fillMaxWidth()
                                         .padding(10.dp),
                                     horizontalAlignment = Alignment.CenterHorizontally,
                                     verticalArrangement = Arrangement.Center
                                 ) {
                                     //title and streak
                                     Row(
                                         modifier = Modifier.fillMaxWidth(),
                                         horizontalArrangement = Arrangement.SpaceBetween,
                                         verticalAlignment = Alignment.CenterVertically
                                     ) {
                                         Text(
                                             text = state.title,
                                             style = TextStyle(
                                                 color = Color.White,
                                                 fontFamily = instrumentSerif,
                                                 fontStyle = FontStyle.Italic,
                                                 fontSize = 16.sp
                                             )
                                         )
                                         Row(
                                             horizontalArrangement = Arrangement.spacedBy(2.dp),
                                             verticalAlignment = Alignment.CenterVertically
                                         ) {
                                             Text(
                                                 text = state.currentStreak.toString(),
                                                 style = TextStyle(
                                                     color = Color.White,
                                                     fontFamily = regular,
                                                     fontWeight = FontWeight.Bold,
                                                     fontSize = 16.sp
                                                 )
                                             )
                                             FireAnimation(
                                                 colorKey = state.colorKey,
                                                 size = 30,
                                                 modifier = Modifier.padding(bottom = 8.dp)
                                             )
                                         }
                                     }
                                     Spacer(Modifier.height(16.dp))
                                     Row(
                                         modifier = Modifier.wrapContentSize(),
                                         horizontalArrangement = Arrangement.spacedBy(4.dp)
                                     ) {
                                         var habitDayList: List<LocalDate> = emptyList()

                                         when (state.frequency) {
                                             Frequency.Daily -> {
                                                 habitDayList = state.OverAllDateRange
                                             }

                                             Frequency.Monthly -> {
                                                 habitDayList = state.OverAllDateRange.filter {
                                                     state.daysOfMonth!!.contains(it.dayOfMonth)
                                                 }
                                             }

                                             Frequency.Weekly -> {
                                                 habitDayList =
                                                     state.OverAllDateRange.filter { state.daysOfWeek[it.dayOfWeek.value - 1] == 1 }
                                             }

                                             else -> {}
                                         }
                                         //filtering days for only after creating the habit
                                         habitDayList =
                                             habitDayList.filter { it >= state.startDate }

                                         state.WeeklyDateRange.forEach { day ->
                                             val progress =
                                                 state.progressList.find { it.date == day }
                                             WeeklyCell(
                                                 size = width/8,
                                                 isActive = habitDayList.contains(day),
                                                 isSelect = (progress != null && progress.status == Status.Done),
                                                 isLater = habitDayList.contains(day) && day > LocalDate.now(),
                                                 color = getOriginalColorFromKey(state.colorKey)
                                             )
                                         }
                                     }
                                 }

                             }
                         }
                     }
                     2->{
                         ShareItem(
                             color = getOriginalColorFromKey(state.colorKey),
                             selectedBackground = state.selectedBackground,
                             imageBgBitmap = state.imageBgBitmap,
                             captureController = captureController
                         ) {
                             Box(
                                 modifier = Modifier
                                     .width(252.dp)
                                     .clip(RoundedCornerShape(15.dp))
                                     .background(
                                         if (state.selectedBackground == Background.Default) colorResource(
                                             R.color.secondary_gray
                                         )
                                         else Color.Transparent
                                     ),
                                 contentAlignment = Alignment.Center
                             ) {
                                 val width = 236
                                 Column(
                                     modifier = Modifier.fillMaxWidth()
                                         .padding(10.dp),
                                     horizontalAlignment = Alignment.CenterHorizontally,
                                     verticalArrangement = Arrangement.Center
                                 ) {
                                     CalendarCard(
                                         title= state.title,
                                         color = getOriginalColorFromKey(state.colorKey),
                                         progressList = state.progressList,
                                         width = width.dp,
                                         height = 200,
                                         dayTextSize = 12,
                                         backgroundColor = if (state.selectedBackground == Background.Default) colorResource(
                                             R.color.secondary_gray
                                         )
                                         else Color.Transparent
                                     )
                                 }

                             }
                         }
                     }
                 }
            }
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                (0..pagerState.pageCount - 1).forEach { page->
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(
                                if(pagerState.currentPage == page) getOriginalColorFromKey(state.colorKey)
                                else MaterialTheme.colorScheme.onPrimary.copy(0.5f)
                            )
                            .clickable{
                               scope.launch {
                                   pagerState.animateScrollToPage(page)
                               }
                            }
                    )
                    Spacer(Modifier.width(4.dp))
                }
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = "share on social media",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = regular,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                ),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            //download and share button
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                //share
                 Box(
                     modifier = Modifier
                         .size(50.dp)
                         .clip(CircleShape)
                         .background(MaterialTheme.colorScheme.onPrimary.copy(0.3f))
                         .clickable{
                             scope.launch {
                                 val bitmapAsSync = captureController.captureAsync()
                                 try{
                                     val bitmap = bitmapAsSync.await()
                                     viewModel.shareCard(context,bitmap.asAndroidBitmap())
                                 }catch (error : Throwable){
                                     error.printStackTrace()
                                     Log.e("error",error.toString())
                                     Toast.makeText(context, "Error sharing image", Toast.LENGTH_SHORT).show()
                                 }
                             }
                         },
                     contentAlignment = Alignment.Center
                 ){
                     Icon(
                         painter = painterResource(R.drawable.share_icon),
                         contentDescription = null,
                         tint = MaterialTheme.colorScheme.onPrimary
                     )
                 }
                //download
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onPrimary.copy(0.3f))
                        .clickable{
                            scope.launch {
                                val bitmapAsSync = captureController.captureAsync()
                                try{
                                    val bitmap = bitmapAsSync.await()
                                    viewModel.downloadCard(context,bitmap.asAndroidBitmap())
                                    Toast.makeText(context, "Image Downloaded", Toast.LENGTH_SHORT).show()
                                }catch (error : Throwable){
                                    error.printStackTrace()
                                    Log.w("error",error.toString())
                                    Toast.makeText(context, "Error downloading image", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        painter = painterResource(R.drawable.download_icon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

            }

        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ShareItem(
    modifier: Modifier = Modifier,
    color : Color,
    captureController : CaptureController,
    selectedBackground: Background,
    imageBgBitmap: Bitmap? = null,
    content : @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(300.dp)
            .clip(RoundedCornerShape(20.dp))
            .capturable(captureController)
            .background(
                if(selectedBackground == Background.Default) color
                else Color.Black
            ),
        contentAlignment = Alignment.Center
    ){
        //image bg
        if(selectedBackground == Background.Custom && imageBgBitmap != null){
            Image(
                bitmap = imageBgBitmap.asImageBitmap(),
                contentDescription = null,
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.3f)
                    .blur(7.dp)
            )
        }

        //content
        Box(Modifier.fillMaxSize()){
            Box(
                modifier = Modifier.align(Alignment.Center)
            ){
                content()
            }
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = "icon",
                    modifier = Modifier.width(40.dp)
                )
                Text(
                    text = stringResource(R.string.app_name),
                    style = TextStyle(
                        color = Color.White,
                        fontFamily = instrumentSerif,
                        fontStyle = FontStyle.Italic,
                        fontSize = 10.sp
                    )
                )
            }
        }
    }
}

@Composable
fun CalendarCard(
    title : String,
    progressList: List<HabitProgress> = emptyList(),
    color: Color,
    backgroundColor: Color = MaterialTheme.colorScheme.secondary,
    height: Int = 300,
    width: Dp,
    dayTextSize : Int = 16,
    onclick: (HabitProgress) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val state = rememberCalendarState(
        startMonth= YearMonth.now().minusYears(4),
        endMonth = YearMonth.now().plusYears(4),
        firstVisibleMonth = YearMonth.now()
    )
    HorizontalCalendar(
        state = state,
        dayContent = { day ->
            if (day.position == DayPosition.MonthDate) {
                val progress = progressList.find { it.date == day.date }
                DayCard(
                    day = day,
                    isSelected = progress != null,
                    color = color,
                    size = dayTextSize
                )
            } else {
                // Empty Box for non-month days (padding cells)
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(Color.Transparent)
                )
            }
        }
        ,
        monthContainer = { _, container ->
            Element(
                backgroundColor = backgroundColor,
                modifier = Modifier
                    .width(width)
                    .height(height.dp)
            ) {
                container() // Render the provided container!
            }
        },
        monthHeader = {month->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        color = Color.White,
                        fontFamily = instrumentSerif,
                        fontStyle = FontStyle.Italic,
                        fontSize = if(title.length > 13) 14.sp else 20.sp
                    ),
                    modifier = Modifier.fillMaxWidth(0.7f)
                )
                Text(
                    text = "${month.yearMonth.month.name.lowercase().capitalize(Locale.ROOT)} ${month.yearMonth.year}",
                    style = androidx.compose.ui.text.TextStyle(
                        color = Color.White,
                        fontSize = 8.sp,
                        fontFamily = regular,
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier.padding(end = 16.dp,bottom= 8.dp)
                )
            }
        }
    )

}

@Composable
fun DayCard(
    day: CalendarDay,
    isSelected: Boolean = false,
    color: Color,
    size : Int = 16,
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(25.dp))
            .background(
                color = if (isSelected) color else Color.Transparent
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            style = androidx.compose.ui.text.TextStyle(
                color = Color.White,
                fontSize = size.sp,
                fontFamily = regular,
                fontWeight = FontWeight.Light
            )
        )
    }
}

