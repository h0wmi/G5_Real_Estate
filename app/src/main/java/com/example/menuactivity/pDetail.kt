package com.example.menuactivity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class pDetail : AppCompatActivity() {
    private lateinit var currentPerson: Person

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdetail)

        val name = intent.getStringExtra("name") ?: ""
        val idnum = intent.getStringExtra("idnum") ?: ""
        currentPerson = Person(name, idnum)

        val nameText = findViewById<TextView>(R.id.nameText)
        val idText = findViewById<TextView>(R.id.idText)
        val exitBtn = findViewById<Button>(R.id.back)
        val updateBtn = findViewById<Button>(R.id.btnUpdate)
        val deleteBtn = findViewById<Button>(R.id.btnDelete)

        nameText.text = "Name: $name"
        idText.text = "ID Number: $idnum"

        exitBtn.setOnClickListener {
            finish()
        }

        updateBtn.setOnClickListener {
            val intent = Intent(this, EditPerson::class.java)
            intent.putExtra("name", currentPerson.name)
            intent.putExtra("idnum", currentPerson.idnum)
            startActivityForResult(intent, UPDATE_REQUEST_CODE)
        }

        deleteBtn.setOnClickListener {
            showDeleteConfirmation()
        }
    }

    private fun showDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Delete Person")
            .setMessage("Are you sure you want to delete this person record?\n\nName: ${currentPerson.name}\nID Number: ${currentPerson.idnum}")
            .setPositiveButton("Delete") { _, _ ->
                deletePerson()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deletePerson() {
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val gson = Gson()

        val json = sharedPref.getString("personList", null)
        val type = object : TypeToken<MutableList<Person>>() {}.type
        val personList: MutableList<Person> =
            if (json != null) gson.fromJson(json, type) else mutableListOf()

        // Find and remove the person
        val iterator = personList.iterator()
        while (iterator.hasNext()) {
            val person = iterator.next()
            if (person.name == currentPerson.name && person.idnum == currentPerson.idnum) {
                iterator.remove()
                break
            }
        }

        // Save updated list
        val updatedJson = gson.toJson(personList)
        sharedPref.edit().putString("personList", updatedJson).apply()

        Toast.makeText(this, "Person deleted successfully!", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPDATE_REQUEST_CODE && resultCode == RESULT_OK) {
            // Refresh the display if the person was updated
            finish()
        }
    }

    companion object {
        private const val UPDATE_REQUEST_CODE = 1001
    }
}