package com.example.todoapp.network

import android.content.Context

fun Context.setRevision(revision: Int) {
    val prefs = getSharedPreferences("sp", Context.MODE_PRIVATE)
    prefs.edit().putInt("revision", revision).apply()
}
fun Context.getRevision(): Int {
    val prefs = getSharedPreferences("sp", Context.MODE_PRIVATE)
    return prefs.getInt("revision", 0)
}