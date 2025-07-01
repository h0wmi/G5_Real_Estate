package com.example.menuactivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class   PersonnelList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personnel_list)

        val listView = findViewById<ListView>(R.id.dataListView)
        val exitbtn = findViewById<Button>(R.id.back)


        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPref.getString("personList", null)
        val type = object : TypeToken<MutableList<Person>>() {}.type
        val personList: MutableList<Person> =
            if (json != null) gson.fromJson(json, type) else mutableListOf()

        // clear out blank items
        val cleanList = personList.filter {
            it.name.isNotBlank() && it.idnum.isNotBlank()
        }

        // Cleaner for invalid entries
        if (cleanList.size != personList.size) {
            val updatedJson = gson.toJson(cleanList)
            sharedPref.edit().putString("personList", updatedJson).apply()
        }


        val nameList = cleanList.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, nameList)
        listView.adapter = adapter

        //Detail view
        listView.setOnItemClickListener { _, _, position, _ ->
            val selected = cleanList[position]
            val intent = Intent(this, pDetail::class.java)
            intent.putExtra("name", selected.name)
            intent.putExtra("idnum", selected.idnum)
            startActivity(intent)
        }

        exitbtn.setOnClickListener {
            finish()
        }
    }
}