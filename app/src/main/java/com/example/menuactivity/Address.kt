package com.example.menuactivity

import android.content.Context

data class Address(
    val id: Int,
    val address: String,
    val price: Double,
    val commission: Double,
    val paymentOption: String,
    val isDownpayment: Boolean,
    val downpayment: Double? = null
) {
    companion object {
        fun nextId(context: Context): Int {
            val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val currentId = sharedPref.getInt("addressCurrentId", 102000)
            sharedPref.edit().putInt("addressCurrentId", currentId + 1).apply()
            return currentId
        }
    }
}
