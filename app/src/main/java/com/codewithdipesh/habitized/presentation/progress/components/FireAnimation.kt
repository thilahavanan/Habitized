package com.codewithdipesh.habitized.presentation.progress.components


import androidx.annotation.RawRes
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.presentation.util.getAnimatedFireIcon
import com.dotlottie.dlplayer.Mode
import com.dotlottie.dlplayer.createDefaultLayout
import com.lottiefiles.dotlottie.core.compose.runtime.DotLottieController
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieEventListener
import com.lottiefiles.dotlottie.core.util.DotLottieSource

@Composable
fun FireAnimation(
    modifier: Modifier = Modifier,
    colorKey: String,
    loop : Boolean = false,
    size : Int = 30
) {

    val context = LocalContext.current
    val jsonFile = getAnimatedFireIcon(colorKey)
    val jsonString = context.resources.openRawResource(jsonFile).bufferedReader().use { it.readText() }
    
    DotLottieAnimation(
        modifier = modifier.size(size.dp),
        source = DotLottieSource.Json(jsonString),
        autoplay = true,
        loop = loop,
        speed = 1.5f,
        playMode = Mode.FORWARD
    )
}
