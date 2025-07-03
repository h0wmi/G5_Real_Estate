package com.example.menuactivity

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class EditPerson : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_person)

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val ageInput = findViewById<EditText>(R.id.ageInput)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val contactInput = findViewById<EditText>(R.id.contactInput)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        val personId = intent.getIntExtra("id", -1)
        if (personId == -1) {
            Toast.makeText(this, "Invalid person ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPref.getString("personList", null)
        val type = object : TypeToken<MutableList<Person>>() {}.type
        val personList: MutableList<Person> = if (json != null) gson.fromJson(json, type) else mutableListOf()

        val person = personList.find { it.id == personId }
        if (person != null) {
            nameInput.setText(person.name)
            ageInput.setText(person.age.toString())
            emailInput.setText(person.email)
            contactInput.setText(person.contact)
        } else {
            Toast.makeText(this, "Person not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        btnUpdate.setOnClickListener {
            val newName = nameInput.text.toString().trim()
            val newAgeText = ageInput.text.toString().trim()
            val newEmail = emailInput.text.toString().trim()
            val newContact = contactInput.text.toString().trim()

            val newAge = newAgeText.toIntOrNull()

            if (newName.isNotBlank() && newAge != null && newEmail.isNotBlank() && newContact.isNotBlank()) {
                val index = personList.indexOfFirst { it.id == personId }
                if (index != -1) {
                    personList[index] = Person(
                        id = personId,
                        name = newName,
                        age = newAge,
                        email = newEmail,
                        contact = newContact
                    )

                    val updatedJson = gson.toJson(personList)
                    sharedPref.edit().putString("personList", updatedJson).apply()

                    Toast.makeText(this, "Person updated!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                Toast.makeText(this, "Please fill out all fields correctly", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }
}
