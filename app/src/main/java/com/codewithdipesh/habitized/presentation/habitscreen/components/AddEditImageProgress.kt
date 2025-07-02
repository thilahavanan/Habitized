package com.codewithdipesh.habitized.presentation.habitscreen.components

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.fonts.FontStyle
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.domain.model.ImageProgress
import com.codewithdipesh.habitized.presentation.util.getOriginalColorFromKey
import com.codewithdipesh.habitized.ui.theme.playfair
import com.codewithdipesh.habitized.ui.theme.regular
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.util.Locale
import java.util.UUID
import kotlin.String

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditImageProgress(
    modifier: Modifier = Modifier,
    title : String,
    imageProgress: ImageProgress? = null,
    color: Color,
    onSave : (UUID?,String, LocalDate, String) ->Unit,
    onCancel : ()->Unit,
    onDelete : (ImageProgress)->Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val imageDir = File(context.filesDir, "habit_image").apply { if (!exists()) mkdirs() }

    var image by remember { mutableStateOf(imageProgress?.imagePath ?: "") }
    var date by remember { mutableStateOf(imageProgress?.date ?: LocalDate.now()) }
    var description by remember { mutableStateOf(imageProgress?.description ?: "") }

    var showOptionChooser by remember { mutableStateOf(false) }
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showLoader by remember { mutableStateOf(false) }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri:Uri? ->
        uri?.let {
            showLoader = true
            scope.launch(Dispatchers.IO) {
                try {
                    val bitmap = if (Build.VERSION.SDK_INT < 28) {
                        MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                    } else {
                        val source = ImageDecoder.createSource(context.contentResolver, it)
                        ImageDecoder.decodeBitmap(source)
                    }

                    val file = File(imageDir, "${System.currentTimeMillis()}.png")
                    FileOutputStream(file).use { out ->
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                    }

                    withContext(Dispatchers.Main) {
                        image = file.absolutePath
                        showLoader = false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    showLoader = false
                }
            }
        }

    }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && capturedImageUri != null) {
            showLoader = true
            scope.launch(Dispatchers.IO) {
                try {
                    val bitmap = if (Build.VERSION.SDK_INT < 28) {
                        MediaStore.Images.Media.getBitmap(context.contentResolver, capturedImageUri)
                    } else {
                        val source = ImageDecoder.createSource(context.contentResolver, capturedImageUri!!)
                        ImageDecoder.decodeBitmap(source)
                    }

                    val file = File(imageDir, "${System.currentTimeMillis()}.png")
                    FileOutputStream(file).use { out ->
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                    }

                    withContext(Dispatchers.Main) {
                        image = file.absolutePath
                        showLoader = false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    showLoader = false
                }
            }
        }
    }
    val painter = rememberAsyncImagePainter(model = File(image))
    val state = painter.state

    var showDeleteBox by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = {
           onCancel()
        },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxSize()
    ) {
        if(showOptionChooser){
            ChooseMediaOption(
                onDismiss = {
                    showOptionChooser = false
                },
                onCameraSelected = {
                    val imageFile = File(
                        context.filesDir,
                        "habit_image/${System.currentTimeMillis()}.jpg"
                    )
                    val uri = FileProvider.getUriForFile(
                        context,
                        context.packageName + ".provider",
                        imageFile
                    )
                    capturedImageUri = uri
                    cameraLauncher.launch(uri)
                },
                onGallerySelected = {
                    galleryLauncher.launch("image/*")
                }
            )
        }
        if(showDeleteBox){
            DeleteAlertBox(
                onConfirm = {
                    onDelete(imageProgress!!)
                    onCancel()
                },
                onCancel = {
                    showDeleteBox = false
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            //heading
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Text(
                    text = "Visual progress",
                    style = androidx.compose.ui.text.TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 18.sp,
                        fontFamily = playfair,
                        fontWeight = FontWeight.Bold,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
                if(imageProgress != null){
                    IconButton(
                        onClick = {
                            showDeleteBox = true
                        },
                        modifier = Modifier.align(Alignment.CenterEnd).padding(top = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "delete",
                            tint = colorResource(R.color.delete_red)
                        )
                    }
                }
            }
            //habit name
            Text(
                text = title,
                style = androidx.compose.ui.text.TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 24.sp,
                    fontFamily = regular,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                ),
                modifier = Modifier.fillMaxWidth()
            )
            //Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "Date",
                    style = androidx.compose.ui.text.TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 20.sp,
                        fontFamily = regular,
                        fontWeight = FontWeight.Normal
                    )
                )
                Text(
                    text = "${date.dayOfMonth} ${date.month.name.take(3)?.lowercase()} ${date.year}",
                    style = androidx.compose.ui.text.TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 20.sp,
                        fontFamily = regular,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
            //Box for image
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .background(MaterialTheme.colorScheme.outline)
                    .clickable {
                        if (image.isEmpty()) {
                            showOptionChooser = true
                        }
                    },
                contentAlignment = Alignment.Center
            ){
                if(image.isNotEmpty()){
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    //loader
                    if (showLoader || state is AsyncImagePainter.State.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(30.dp),
                            strokeWidth = 3.dp,
                            color = color
                        )
                    }
                    //edit and delete image
                    Row(modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopEnd),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.3f))
                                .clickable {
                                    showOptionChooser = true
                                },
                            contentAlignment = Alignment.Center
                        ){
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "edit image",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.3f))
                                .clickable {
                                    image = ""
                                },
                            contentAlignment = Alignment.Center
                        ){
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "delete image",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }else{
                    Icon(
                        painter = painterResource(R.drawable.baseline_camera_alt_24),
                        contentDescription = "add image  ",
                        tint = MaterialTheme.colorScheme.scrim
                    )
                }
            }
            //description
            Box {
                BasicTextField(
                    value = description,
                    onValueChange = {
                        description = it
                    },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = regular,
                        fontWeight = FontWeight.Normal,
                        fontSize = 22.sp
                    ),
                    singleLine = false,
                    cursorBrush = SolidColor(colorResource(R.color.primary)),
                    modifier = Modifier.fillMaxWidth()
                )

                // Placeholder shown only when title is empty
                if (description.isEmpty()) {
                    Text(
                        text = "Tell about your progress",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.scrim, // make it look like a placeholder
                            fontFamily = regular,
                            fontWeight = FontWeight.Normal,
                            fontSize = 22.sp
                        )
                    )
                }
            }

        }
        //log progress button
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 30.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(color)
                    .clickable {
                        onSave(imageProgress?.id, image, date, description)
                        onCancel()
                    },
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "Log Progress",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                        fontFamily = regular,
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp
                    )
                )
            }
        }
    }

}

//deleteAlertBox
@Composable
fun DeleteAlertBox(
    modifier: Modifier = Modifier,
    onConfirm : ()-> Unit,
    onCancel : () ->Unit
){
    AlertDialog(
        onDismissRequest = {
            onCancel()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text(
                    text = "Yes,Delete",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = regular,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text(
                    text = "Cancel",
                    style = TextStyle(
                        color = colorResource(R.color.delete_red),
                        fontFamily = regular,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    )
                )
            }
        },
        title = {
            Text(
                text = "Delete the Log",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = regular,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
        },
        text = {
            Text(
                text = "This action is permanent and cannot be undone",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = regular,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            )
        }
    )
}