package com.codewithdipesh.habitized.presentation.goalscreen.components

enum class GraphType{
    last_week,
    last_month,
    last_year
}

fun GraphType.toName() : String{
    return when(this){
        GraphType.last_week -> "Last Week"
        GraphType.last_month -> "Last Month"
        GraphType.last_year -> "Last Year"
    }
}
