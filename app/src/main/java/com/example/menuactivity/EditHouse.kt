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

class EditHouse : AppCompatActivity() {
    private lateinit var originalAddress: Address

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_house)

        val houseNumInput = findViewById<EditText>(R.id.hnum)
        val addressInput = findViewById<EditText>(R.id.add)
        val updateBtn = findViewById<Button>(R.id.btnUpdate)
        val cancelBtn = findViewById<Button>(R.id.btnCancel)

        // Get original data
        val originalHouseNumber = intent.getStringExtra("houseNumber") ?: ""
        val originalAddressStr = intent.getStringExtra("address") ?: ""
        originalAddress = Address(originalHouseNumber, originalAddressStr)

        // Populate fields with existing data
        houseNumInput.setText(originalHouseNumber)
        addressInput.setText(originalAddressStr)

        updateBtn.setOnClickListener {
            val newHouseNum = houseNumInput.text.toString()
            val newAddress = addressInput.text.toString()

            if (newHouseNum.isNotBlank() && newAddress.isNotBlank()) {
                updateHouse(newHouseNum, newAddress)
            } else {
                Toast.makeText(this, "Enter both House No. and Address", Toast.LENGTH_SHORT).show()
            }
        }

        cancelBtn.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    private fun updateHouse(newHouseNum: String, newAddress: String) {
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val gson = Gson()

        val json = sharedPref.getString("addressList", null)
        val type = object : TypeToken<MutableList<Address>>() {}.type
        val addressList: MutableList<Address> =
            if (json != null) gson.fromJson(json, type) else mutableListOf()

        // Find and update the address
        for (i in addressList.indices) {
            if (addressList[i].houseNumber == originalAddress.houseNumber &&
                addressList[i].address == originalAddress.address) {
                addressList[i] = Address(newHouseNum, newAddress)
                break
            }
        }

        // Save updated list
        val updatedJson = gson.toJson(addressList)
        sharedPref.edit().putString("addressList", updatedJson).apply()

        Toast.makeText(this, "House updated successfully!", Toast.LENGTH_SHORT).show()
        setResult(Activity.RESULT_OK)
        finish()
    }
}