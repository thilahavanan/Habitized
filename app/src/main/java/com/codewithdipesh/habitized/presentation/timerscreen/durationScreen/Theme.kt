package com.codewithdipesh.habitized.presentation.timerscreen.durationScreen

sealed class Theme(val displayName: String) {
    object Normal : Theme("normal")
    object Coffee : Theme("coffee")
    object Matcha : Theme("matcha")

    override fun toString(): String = displayName

    companion object {
        fun fromString(value: String): Theme {
            return when (value.lowercase()) {
                "normal" -> Normal
                "coffee" -> Coffee
                "matcha" -> Matcha
                else -> Normal
            }
        }
        fun getThemes(): List<Theme> {
            return listOf(Normal, Matcha, Coffee)
        }
    }
}