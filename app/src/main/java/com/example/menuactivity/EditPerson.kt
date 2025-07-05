package com.example.menuactivity

import android.app.Activity
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

        // Load original selected person (same pattern as EditHouse)
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPref.getString("selectedPerson", null)
        val type = object : TypeToken<Person>() {}.type

        if (json == null) {
            Toast.makeText(this, "No person selected", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val originalPerson = gson.fromJson<Person>(json, type)

        // Populate UI fields
        nameInput.setText(originalPerson.name)
        ageInput.setText(originalPerson.age.toString())
        emailInput.setText(originalPerson.email)
        contactInput.setText(originalPerson.contact)

        btnUpdate.setOnClickListener {
            val newName = nameInput.text.toString().trim()
            val newAge = ageInput.text.toString().toIntOrNull()
            val newEmail = emailInput.text.toString().trim()
            val newContact = contactInput.text.toString().trim()

            if (newName.isBlank() || newAge == null || newEmail.isBlank() || newContact.isBlank()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create updated Person
            val updatedPerson = Person(
                id = originalPerson.id,
                name = newName,
                age = newAge,
                email = newEmail,
                contact = newContact
            )

            // Load person list
            val listJson = sharedPref.getString("personList", null)
            val listType = object : TypeToken<MutableList<Person>>() {}.type
            val personList: MutableList<Person> =
                if (listJson != null) gson.fromJson(listJson, listType) else mutableListOf()

            // Replace the original
            val index = personList.indexOfFirst { it.id == originalPerson.id }
            if (index != -1) {
                personList[index] = updatedPerson
                sharedPref.edit().putString("personList", gson.toJson(personList)).apply()
                Toast.makeText(this, "Person updated successfully!", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "Original person not found", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }}
