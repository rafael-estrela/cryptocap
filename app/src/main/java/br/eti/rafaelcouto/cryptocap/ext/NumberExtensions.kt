package br.eti.rafaelcouto.cryptocap.ext

import java.text.NumberFormat
import java.util.Locale

private const val MIN_INTEGERS = 1
private const val PERCENTAGE_MIN_FRACTION = 0
private const val MONETARY_FRACTION = 2
private const val PERCENTAGE_MAX_FRACTION = 8

fun Number.humanReadable(): String {
    val format = NumberFormat.getInstance(Locale.US).apply {
        minimumFractionDigits = MONETARY_FRACTION
        maximumFractionDigits = MONETARY_FRACTION
        minimumIntegerDigits = MIN_INTEGERS
    }

    return format.format(this)
}

fun Number.asMonetary(): String {
    val format = NumberFormat.getInstance(Locale.US).apply {
        minimumFractionDigits = MONETARY_FRACTION
        maximumFractionDigits = MONETARY_FRACTION
        minimumIntegerDigits = MIN_INTEGERS
    }

    return "US$ ${format.format(this)}"
}

fun Number.asPercentage(): String {
    val format = NumberFormat.getInstance(Locale.US).apply {
        minimumFractionDigits = PERCENTAGE_MIN_FRACTION
        minimumIntegerDigits = MIN_INTEGERS
        maximumFractionDigits = PERCENTAGE_MAX_FRACTION
    }

    return "${format.format(this)}%"
}
