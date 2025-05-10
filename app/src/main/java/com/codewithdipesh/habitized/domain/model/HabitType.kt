package com.codewithdipesh.habitized.domain.model

sealed class HabitType(val displayName: String) {
    object OneTime : HabitType("onetime")
    object Count : HabitType("count")
    object Duration : HabitType("duration")
    object Session : HabitType("session")

    override fun toString(): String = displayName

    companion object {
        fun fromString(value: String): HabitType {
            return when (value.lowercase()) {
                "count" -> Count
                "duration" -> Duration
                "session" -> Session
                "onetime" -> OneTime
                else -> Count
            }
        }
        fun getHabitTypes(): List<HabitType> {
            return listOf(OneTime, Count, Duration, Session)
        }
    }
}



