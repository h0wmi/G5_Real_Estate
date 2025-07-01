package com.example.menuactivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HouseList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_house_list)

        val listView = findViewById<ListView>(R.id.addressListView)

        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val gson = Gson()



        val json = sharedPref.getString("addressList", null)
        val type = object : TypeToken<MutableList<Address>>() {}.type
        val addressList: MutableList<Address> =
            if (json != null) gson.fromJson(json, type) else mutableListOf()

        // Remove empty entries
        val cleanList = addressList.filter {
            it.houseNumber.isNotBlank() && it.address.isNotBlank()
        }

        // Update SharedPreferences if there were invalid items
        if (cleanList.size != addressList.size) {
            val updatedJson = gson.toJson(cleanList)
            sharedPref.edit().putString("addressList", updatedJson).apply()
        }

        // Display house#
        val houseNumberList = cleanList.map { it.houseNumber }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, houseNumberList)
        listView.adapter = adapter


        listView.setOnItemClickListener { _, _, position, _ ->
            val selected = cleanList[position]
            val intent = Intent(this, HouseDetails::class.java)
            intent.putExtra("houseNumber", selected.houseNumber)
            intent.putExtra("address", selected.address)
            startActivity(intent)
        }

        // Back button
        val exitbtn = findViewById<Button>(R.id.back)
        exitbtn.setOnClickListener {
            finish()
        }
    }
}