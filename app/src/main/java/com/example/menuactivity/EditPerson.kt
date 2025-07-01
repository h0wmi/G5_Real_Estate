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
    private lateinit var originalPerson: Person

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_person)

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val idInput = findViewById<EditText>(R.id.idInput)
        val updateBtn = findViewById<Button>(R.id.btnUpdate)
        val cancelBtn = findViewById<Button>(R.id.btnCancel)

        // Get original data
        val originalName = intent.getStringExtra("name") ?: ""
        val originalId = intent.getStringExtra("idnum") ?: ""
        originalPerson = Person(originalName, originalId)

        // Populate fields with existing data
        nameInput.setText(originalName)
        idInput.setText(originalId)

        updateBtn.setOnClickListener {
            val newName = nameInput.text.toString()
            val newId = idInput.text.toString()

            if (newName.isNotBlank() && newId.isNotBlank()) {
                updatePerson(newName, newId)
            } else {
                Toast.makeText(this, "Enter both Name and ID Number", Toast.LENGTH_SHORT).show()
            }
        }

        cancelBtn.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    private fun updatePerson(newName: String, newId: String) {
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val gson = Gson()

        val json = sharedPref.getString("personList", null)
        val type = object : TypeToken<MutableList<Person>>() {}.type
        val personList: MutableList<Person> =
            if (json != null) gson.fromJson(json, type) else mutableListOf()

        // Check if new ID already exists (only if it's different from original)
        if (newId != originalPerson.idnum) {
            val existingPerson = personList.find { it.idnum == newId }
            if (existingPerson != null) {
                Toast.makeText(this, "ID Number already exists!", Toast.LENGTH_SHORT).show()
                return
            }
        }

        // Find and update the person
        for (i in personList.indices) {
            if (personList[i].name == originalPerson.name &&
                personList[i].idnum == originalPerson.idnum) {
                personList[i] = Person(newName, newId)
                break
            }
        }

        // Save updated list
        val updatedJson = gson.toJson(personList)
        sharedPref.edit().putString("personList", updatedJson).apply()

        Toast.makeText(this, "Person updated successfully!", Toast.LENGTH_SHORT).show()
        setResult(Activity.RESULT_OK)
        finish()
    }
}