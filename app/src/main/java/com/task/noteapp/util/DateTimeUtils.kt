package com.task.noteapp.util

import java.text.SimpleDateFormat
import java.util.*

fun getCurrentDateTime(): String {
    val dateFormat = SimpleDateFormat(
        "dd/MM/yyyy",
        Locale.getDefault()
    )
    return dateFormat.format(Date())
}