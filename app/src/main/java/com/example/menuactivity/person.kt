package com.example.menuactivity

import android.content.Context

data class Person(
    val id: Int,
    val name: String,
    val age: Int,
    val email: String,
    val contact: String
) {
    companion object {
        fun nextId(context: Context): Int {
            val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val currentId = sharedPref.getInt("personnelCurrentId", 2023100)
            sharedPref.edit().putInt("addressCurrentId", currentId + 1).apply()
            return currentId
        }
    }
}
