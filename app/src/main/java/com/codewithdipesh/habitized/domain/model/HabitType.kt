package com.codewithdipesh.habitized.domain.model

sealed class HabitType(val displayName: String) {
    object Count : HabitType("Count-Based")
    object Duration : HabitType("Duration-Based")
    object Percentage : HabitType("Percentage-Based")
    object Session : HabitType("Session-Based")

    override fun toString(): String = displayName

    companion object {
        fun fromString(value: String): HabitType {
            return when (value.lowercase()) {
                "count", "count-based" -> Count
                "duration", "duration-based" -> Duration
                "percentage", "percentage-based" -> Percentage
                "session", "session-based" -> Session
                else -> Count
            }
        }
    }
}



