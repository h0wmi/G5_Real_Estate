package com.example.menuactivity

data class Person(
    val id: Int = nextId(),
    val name: String,
    val age: Int,
    val email: String,
    val contact: String
) {
    companion object {
        private var currentId = 2013100
        fun nextId(): Int = currentId++
    }
}
