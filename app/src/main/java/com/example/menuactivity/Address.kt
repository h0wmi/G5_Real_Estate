package com.example.menuactivity

data class Address(
    val id: Int = nextId(),
    val address: String,
    val price: Double,
    val commission: Double,
    val paymentOption: String,
    val isDownpayment: Boolean,
    val downpayment: Double? = null
) {
    companion object {
        private var currentId = 102000
        private fun nextId(): Int = currentId++
    }
}
