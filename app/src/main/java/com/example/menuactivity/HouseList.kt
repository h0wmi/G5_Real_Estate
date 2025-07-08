package com.example.menuactivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HouseList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house_list)

        val listView = findViewById<ListView>(R.id.addressListView)

        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val gson = Gson()

        val json = sharedPref.getString("addressList", null)
        val type = object : TypeToken<MutableList<Address>>() {}.type
        val addressList: MutableList<Address> =
            if (json != null) gson.fromJson(json, type) else mutableListOf()

        // Remove entries with blank addresses
        val cleanList = addressList.filter { it.address.isNotBlank() }

        // Save cleaned list if needed
        if (cleanList.size != addressList.size) {
            val updatedJson = gson.toJson(cleanList)
            sharedPref.edit().putString("addressList", updatedJson).apply()
        }

        // Show only the address in the list
        val addressDisplayList = cleanList.map { it.address }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, addressDisplayList)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selected = cleanList[position]
            val intent = Intent(this, HouseDetails::class.java)
            intent.putExtra("id", selected.id)
            intent.putExtra("address", selected.address)
            intent.putExtra("price", selected.price)
            intent.putExtra("commission", selected.commission)
            intent.putExtra("paymentOption", selected.paymentOption)
            intent.putExtra("isDownpayment", selected.isDownpayment)
            intent.putExtra("downpayment", selected.downpayment)
            startActivity(intent)
        }

        val exitbtn = findViewById<Button>(R.id.back)
        exitbtn.setOnClickListener {
            finish()
        }
    }




}
