package com.example.frndassignment.utils

import android.view.View

fun String.Companion.empty() = ""
fun String.Companion.space() = " "

fun Int?.toStringOrEmpty(): String {
    return this?.toString() ?: String.empty()
}

fun View.setVisible(flag: Boolean) {
    this.visibility = if (flag) View.VISIBLE else View.GONE
}

fun Int?.orZero() = this ?: 0


fun View.throttleClick(onClick: () -> Unit) {
    this.setOnClickListener {
        onClick()
    }
}