package com.codewithdipesh.habitized.presentation.timerscreen.durationScreen

sealed class Theme(val displayName: String) {
    object Normal : Theme("normal")
    object Coffee : Theme("coffee")
    object Matcha : Theme("matcha")
    object Black : Theme("black")

    override fun toString(): String = displayName

    companion object {
        fun fromString(value: String): Theme {
            return when (value.lowercase()) {
                "normal" -> Normal
                "coffee" -> Coffee
                "matcha" -> Matcha
                "black" -> Black
                else -> Normal
            }
        }
        fun getThemes(): List<Theme> {
            return listOf(Normal, Matcha, Coffee, Black)
        }
    }
}