package com.codewithdipesh.habitized.domain.util

sealed  class AppError(val message :String){
    data class NotFound(val errorMessage : String = "Not Found") : AppError(errorMessage)
    data class UnknownError(val errorMessage : String = "Something went wrong") : AppError(errorMessage)
} 