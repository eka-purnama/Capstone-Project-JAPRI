package com.android.japri.utils

import com.google.firebase.Timestamp
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun convertTimestamp(seconds: Long?, nanoseconds: Int?): String {
    val timestamp = Timestamp(seconds ?: 0, nanoseconds ?: 0)
    val date: Date = timestamp.toDate()
    val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US)
    return outputFormat.format(date)
}

fun Int.formatToRupiah(): String {
    val localeID = Locale("id", "ID")
    val currencyFormat = NumberFormat.getCurrencyInstance(localeID)

    val salary = this
    return currencyFormat.format(salary)
}