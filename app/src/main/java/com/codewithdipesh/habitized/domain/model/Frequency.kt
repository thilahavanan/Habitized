package com.codewithdipesh.habitized.domain.model

sealed class Frequency(val displayName: String) {
    object Daily : Frequency("Daily")
    object Weekly : Frequency("Weekly")
    object Monthly : Frequency("Monthly")
    object Custom : Frequency("Custom")

    override fun toString(): String = displayName

    companion object {
        fun fromString(value: String): Frequency {
            return when (value.lowercase()) {
                "daily" -> Daily
                "weekly" -> Weekly
                "monthly" -> Monthly
                "custom" -> Custom
                else -> Daily
            }
        }
    }
}
