package com.codewithdipesh.habitized.domain.model

sealed class Frequency(val displayName: String) {
    object Daily : Frequency("Daily")
    object Weekly : Frequency("Weekly")
    object Monthly : Frequency("Monthly")

    override fun toString(): String = displayName

    companion object {
        fun fromString(value: String): Frequency {
            return when (value.lowercase()) {
                "daily" -> Daily
                "weekly" -> Weekly
                "monthly" -> Monthly
                else -> Daily
            }
        }
        fun getTypes() : List<Frequency>{
            return listOf(Daily, Weekly, Monthly)
        }
    }
}
