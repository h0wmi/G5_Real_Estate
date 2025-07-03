package com.example.menuactivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PersonnelList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personnel_list)

        val listView = findViewById<ListView>(R.id.dataListView)
        val backBtn = findViewById<Button>(R.id.back)

        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPref.getString("personList", null)
        val type = object : TypeToken<MutableList<Person>>() {}.type

        val personList: MutableList<Person> =
            if (json != null) gson.fromJson(json, type) else mutableListOf()

        // Filter out entries with blank names or contact
        val cleanList = personList.filter {
            it.name.isNotBlank() && it.contact.isNotBlank()
        }

        // Save the cleaned list if needed
        if (cleanList.size != personList.size) {
            val updatedJson = gson.toJson(cleanList)
            sharedPref.edit().putString("personList", updatedJson).apply()
        }

        // Display: Name - Contact Number
        val displayList = cleanList.map { "${it.name} - ${it.contact}" }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, displayList)
        listView.adapter = adapter

        // Send full person details to pDetail activity
        listView.setOnItemClickListener { _, _, position, _ ->
            val selected = cleanList[position]
            val intent = Intent(this, pDetail::class.java).apply {
                putExtra("id", selected.id)
                putExtra("name", selected.name)
                putExtra("age", selected.age)
                putExtra("email", selected.email)
                putExtra("contact", selected.contact)
            }
            startActivity(intent)
        }

        backBtn.setOnClickListener {
            finish()
        }
    }
}
