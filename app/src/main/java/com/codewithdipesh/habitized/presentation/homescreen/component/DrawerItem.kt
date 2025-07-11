package com.codewithdipesh.habitized.presentation.homescreen.component

import androidx.annotation.DrawableRes
import com.codewithdipesh.habitized.R

data class DrawerItem(
    @DrawableRes val icon : Int ,
    val text : String,
    val onclick : () ->Unit = {}
)