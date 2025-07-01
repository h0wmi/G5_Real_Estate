package com.example.menuactivity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AddHouse : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_house)

        val houseNumInput = findViewById<EditText>(R.id.hnum)
        val addressInput = findViewById<EditText>(R.id.add)
        val saveBtn = findViewById<Button>(R.id.btnsave)
        val exitbtn = findViewById<Button>(R.id.back)

        exitbtn.setOnClickListener {
            finish()
        }

        saveBtn.setOnClickListener {
            val houseNum = houseNumInput.text.toString()
            val address = addressInput.text.toString()

            if (houseNum.isNotBlank() && address.isNotBlank()) {
                val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                //Initialize json library
                val gson = Gson()

                // Load existing list
                val json = sharedPref.getString("addressList", null)
                //data type specifier
                val type = object : TypeToken<MutableList<Address>>() {}.type
                val addressList: MutableList<Address> =
                    if (json != null) gson.fromJson(json, type) else mutableListOf()

                // Add new address
                addressList.add(Address(houseNum, address))

                // Save updated list
                val updatedJson = gson.toJson(addressList)
                sharedPref.edit().putString("addressList", updatedJson).apply()

                Toast.makeText(this, "Address saved!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Enter both House No. and Address", Toast.LENGTH_SHORT).show()
            }
        }
    }
}