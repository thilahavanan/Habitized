package com.codewithdipesh.habitized.domain.model

sealed class Status(val displayName : String){
    object Done : Status("Done")
    object NotStarted : Status("Not Started")
    object Cancelled : Status("Cancelled")
    object Ongoing : Status("Ongoing")

    override fun toString(): String = displayName
    companion object{
        fun fromString(value: String): Status {
            return when (value.lowercase()) {
                "done" -> Done
                "not started" -> NotStarted
                "cancelled" -> Cancelled
                "ongoing" -> Ongoing
                else -> NotStarted
            }
        }
    }
}
