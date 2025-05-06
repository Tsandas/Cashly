package com.example.financeapptestversion.utils

fun Double.toAbbreviated(): String {
    val absValue = kotlin.math.abs(this)
    val suffix = when {
        absValue >= 1_000_000_000 -> "B"
        absValue >= 1_000_000 -> "M"
        absValue >= 1_000 -> "K"
        else -> ""
    }

    val divisor = when (suffix) {
        "B" -> 1_000_000_000
        "M" -> 1_000_000
        "K" -> 1_000
        else -> 1
    }

    val shortValue = this / divisor
    return if (suffix.isNotEmpty()) {
        "$${"%.1f".format(shortValue)}$suffix"
    } else {
        "$${"%,.2f".format(this)}"
    }
}