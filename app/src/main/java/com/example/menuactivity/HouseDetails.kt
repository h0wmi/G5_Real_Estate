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

class HouseDetails : AppCompatActivity() {
    private lateinit var currentAddress: Address

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house_details)

        val houseNumber = intent.getStringExtra("houseNumber") ?: ""
        val address = intent.getStringExtra("address") ?: ""
        currentAddress = Address(houseNumber, address)

        val houseNumText = findViewById<TextView>(R.id.hnum)
        val addressText = findViewById<TextView>(R.id.add)
        val exitBtn = findViewById<Button>(R.id.back)
        val updateBtn = findViewById<Button>(R.id.btnUpdate)
        val deleteBtn = findViewById<Button>(R.id.btnDelete)

        houseNumText.text = "House Number: $houseNumber"
        addressText.text = "Address: $address"

        exitBtn.setOnClickListener {
            finish()
        }

        updateBtn.setOnClickListener {
            val intent = Intent(this, EditHouse::class.java)
            intent.putExtra("houseNumber", currentAddress.houseNumber)
            intent.putExtra("address", currentAddress.address)
            startActivityForResult(intent, UPDATE_REQUEST_CODE)
        }

        deleteBtn.setOnClickListener {
            showDeleteConfirmation()
        }
    }

    private fun showDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Delete House")
            .setMessage("Are you sure you want to delete this house record?\n\nHouse Number: ${currentAddress.houseNumber}\nAddress: ${currentAddress.address}")
            .setPositiveButton("Delete") { _, _ ->
                deleteHouse()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteHouse() {
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val gson = Gson()

        val json = sharedPref.getString("addressList", null)
        val type = object : TypeToken<MutableList<Address>>() {}.type
        val addressList: MutableList<Address> =
            if (json != null) gson.fromJson(json, type) else mutableListOf()

        // Find and remove the address
        val iterator = addressList.iterator()
        while (iterator.hasNext()) {
            val addr = iterator.next()
            if (addr.houseNumber == currentAddress.houseNumber &&
                addr.address == currentAddress.address) {
                iterator.remove()
                break
            }
        }

        // Save updated list
        val updatedJson = gson.toJson(addressList)
        sharedPref.edit().putString("addressList", updatedJson).apply()

        Toast.makeText(this, "House deleted successfully!", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPDATE_REQUEST_CODE && resultCode == RESULT_OK) {
            // Refresh the display if the house was updated
            finish()
        }
    }

    companion object {
        private const val UPDATE_REQUEST_CODE = 1001
    }
}