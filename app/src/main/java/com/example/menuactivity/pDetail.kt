package com.example.menuactivity

import android.app.Activity
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

        val id = intent.getIntExtra("id", -1)
        val name = intent.getStringExtra("name") ?: ""
        val age = intent.getIntExtra("age", 0)
        val email = intent.getStringExtra("email") ?: ""
        val contact = intent.getStringExtra("contact") ?: ""

        currentPerson = Person(id, name, age, email, contact)

        // Views
        val nameText = findViewById<TextView>(R.id.nameText)
        val ageText = findViewById<TextView>(R.id.ageText)
        val emailText = findViewById<TextView>(R.id.emailText)
        val contactText = findViewById<TextView>(R.id.contactText)
        val exitBtn = findViewById<Button>(R.id.back)
        val updateBtn = findViewById<Button>(R.id.btnUpdate)
        val deleteBtn = findViewById<Button>(R.id.btnDelete)
        val idText = findViewById<TextView>(R.id.idText)

        idText.text = "ID: $id"
        nameText.text = "Name: $name"
        ageText.text = "Age: $age"
        emailText.text = "Email: $email"
        contactText.text = "Contact: $contact"

        exitBtn.setOnClickListener {
            finish()
        }

        updateBtn.setOnClickListener {
            val intent = Intent(this, EditPerson::class.java)
            val gson = Gson()
            val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            sharedPref.edit().putString("selectedPerson", gson.toJson(currentPerson)).apply()
            startActivityForResult(intent, UPDATE_REQUEST_CODE)
        }

        deleteBtn.setOnClickListener {
            showDeleteConfirmation()
        }
    }

    private fun showDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Delete Person")
            .setMessage("Are you sure you want to delete this person?\n\nName: ${currentPerson.name}")
            .setPositiveButton("Delete") { _, _ -> deletePerson() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deletePerson() {
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val gson = Gson()

        // Get and update person list
        val personJson = sharedPref.getString("personList", null)
        val personType = object : TypeToken<MutableList<Person>>() {}.type
        val personList: MutableList<Person> =
            if (personJson != null) gson.fromJson(personJson, personType) else mutableListOf()

        // Remove current person
        val iterator = personList.iterator()
        while (iterator.hasNext()) {
            val person = iterator.next()
            if (person.id == currentPerson.id) {
                iterator.remove()
                break
            }
        }

        // Save updated person list
        val updatedPersonJson = gson.toJson(personList)
        sharedPref.edit().putString("personList", updatedPersonJson).apply()

        //Unlink any PurchasedHouse entries related to this person
        val purchaseJson = sharedPref.getString("purchased_house", null)
        val purchaseType = object : TypeToken<MutableList<PurchasedHouse>>() {}.type
        val purchaseList: MutableList<PurchasedHouse> =
            if (purchaseJson != null) gson.fromJson(purchaseJson, purchaseType) else mutableListOf()

        val updatedPurchaseList = purchaseList.filterNot {
            it.personnelName == currentPerson.name
        }

        sharedPref.edit().putString("purchased_house", gson.toJson(updatedPurchaseList)).apply()

        Toast.makeText(this, "Person deleted successfully!", Toast.LENGTH_SHORT).show()
        setResult(Activity.RESULT_OK)
        finish()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPDATE_REQUEST_CODE && resultCode == RESULT_OK) {
            finish() // Refresh view on return
        }
    }

    companion object {
        private const val UPDATE_REQUEST_CODE = 1001
    }
}
