package com.example.menuactivity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HouseDetails : AppCompatActivity() {

    private lateinit var currentAddress: Address

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house_details)

        // Retrieve data from intent
        val id = intent.getIntExtra("id", -1)
        val address = intent.getStringExtra("address") ?: ""
        val price = intent.getDoubleExtra("price", 0.0)
        val commission = intent.getDoubleExtra("commission", 0.0)
        val paymentOption = intent.getStringExtra("paymentOption") ?: ""
        val downpayment = intent.getDoubleExtra("downpayment", 0.0)
        val isDownpayment = paymentOption == "Downpayment"
        val idText = findViewById<TextView>(R.id.idText)

        currentAddress = Address(
            id = id,
            address = address,
            price = price,
            commission = commission,
            paymentOption = paymentOption,
            isDownpayment = isDownpayment,
            downpayment = if (isDownpayment) downpayment else null
        )

        idText.text = "ID: $id"
        findViewById<TextView>(R.id.add).text = "Address: $address"
        findViewById<TextView>(R.id.price).text = "Price: ₱$price"
        findViewById<TextView>(R.id.commission).text = "Commission: $commission%"
        findViewById<TextView>(R.id.paymentOption).text = "Payment Option: $paymentOption"

        val downpaymentText = findViewById<TextView>(R.id.downpayment)
        if (isDownpayment) {
            downpaymentText.text = "Downpayment: ₱$downpayment"
            downpaymentText.visibility = TextView.VISIBLE
        } else {
            downpaymentText.visibility = TextView.GONE
        }

        findViewById<Button>(R.id.back).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnUpdate).setOnClickListener {
            val intent = Intent(this, EditHouse::class.java)
            val gson = Gson()
            val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            sharedPref.edit().putString("selectedHouse", gson.toJson(currentAddress)).apply()
            startActivityForResult(intent, UPDATE_REQUEST_CODE)
        }

        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            showDeleteConfirmation()
        }
    }

    private fun showDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Delete House")
            .setMessage("Are you sure you want to delete this house record?\n\nAddress: ${currentAddress.address}")
            .setPositiveButton("Delete") { _, _ -> deleteHouse() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteHouse() {
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val gson = Gson()

        val json = sharedPref.getString("addressList", null)
        val type = object : TypeToken<MutableList<Address>>() {}.type
        val addressList: MutableList<Address> = if (json != null) gson.fromJson(json, type) else mutableListOf()

        addressList.removeIf { it.id == currentAddress.id }

        // Save updated address list
        sharedPref.edit().putString("addressList", gson.toJson(addressList)).apply()

        //Unlink any PurchasedHouse entries related to this house
        val purchaseJson = sharedPref.getString("purchased_house", null)
        val purchaseType = object : TypeToken<MutableList<PurchasedHouse>>() {}.type
        val purchaseList: MutableList<PurchasedHouse> =
            if (purchaseJson != null) gson.fromJson(purchaseJson, purchaseType) else mutableListOf()

        val updatedPurchaseList = purchaseList.filterNot {
            it.houseId == currentAddress.id
        }

        sharedPref.edit().putString("purchased_house", gson.toJson(updatedPurchaseList)).apply()

        Toast.makeText(this, "House deleted successfully!", Toast.LENGTH_SHORT).show()
        finish()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPDATE_REQUEST_CODE && resultCode == RESULT_OK) {
            finish() // Refreshes list
        }
    }

    companion object {
        private const val UPDATE_REQUEST_CODE = 1001
    }
}
