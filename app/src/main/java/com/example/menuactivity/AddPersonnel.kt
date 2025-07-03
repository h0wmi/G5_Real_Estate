package com.example.menuactivity

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.appcompat.app.AppCompatActivity

class AddPersonnel : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_personnel)

        val nameInput = findViewById<EditText>(R.id.prsn)
        val ageInput = findViewById<EditText>(R.id.age)
        val emailInput = findViewById<EditText>(R.id.email)
        val contactInput = findViewById<EditText>(R.id.contact)
        val saveButton = findViewById<Button>(R.id.btnsave)
        val exitButton = findViewById<Button>(R.id.back)

        exitButton.setOnClickListener {
            finish()
        }

        saveButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val ageText = ageInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val contact = contactInput.text.toString().trim()
            val age = ageText.toIntOrNull()

            if (name.isNotBlank() && age != null && email.isNotBlank() && contact.isNotBlank()) {
                val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val gson = Gson()

                // Load current list
                val json = sharedPref.getString("personList", null)
                val type = object : TypeToken<MutableList<Person>>() {}.type
                val personList: MutableList<Person> =
                    if (json != null) gson.fromJson(json, type) else mutableListOf()

                // Create new person
                val newPerson = Person(
                    name = name,
                    age = age,
                    email = email,
                    contact = contact
                )

                personList.add(newPerson)

                // Save updated list
                sharedPref.edit().putString("personList", gson.toJson(personList)).apply()

                Toast.makeText(this, "Personnel added!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
