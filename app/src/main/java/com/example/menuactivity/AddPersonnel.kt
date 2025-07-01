package com.example.menuactivity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddPersonnel : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_personnel)

        val nameInput = findViewById<EditText>(R.id.prsn)
        val idnumInput = findViewById<EditText>(R.id.idnum)
        val saveButton = findViewById<Button>(R.id.btnsave)

        val exitbtn = findViewById<Button>(R.id.back)
        exitbtn.setOnClickListener {
            finish()
        }

        saveButton.setOnClickListener {
            val name = nameInput.text.toString()
            val idnum = idnumInput.text.toString()

            if (name.isNotBlank() && idnum.isNotBlank()) {
                val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val gson = Gson()

                // Load current list
                val json = sharedPref.getString("personList", null)
                val type = object : TypeToken<MutableList<Person>>() {}.type
                val personList: MutableList<Person> =
                    if (json != null) gson.fromJson(json, type) else mutableListOf()

                // Add new person
                personList.add(Person(name, idnum))

                // Save updated list
                val updatedJson = gson.toJson(personList)
                sharedPref.edit().putString("personList", updatedJson).apply()



                Toast.makeText(this, "Data saved!", Toast.LENGTH_SHORT).show()
                finish() // Optional: return to menu
            } else {
                Toast.makeText(this, "Please enter both Name and ID", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
