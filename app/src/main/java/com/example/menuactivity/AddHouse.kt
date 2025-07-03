package com.example.menuactivity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AddHouse : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_house)

        val addressInput = findViewById<EditText>(R.id.add)
        val priceInput = findViewById<EditText>(R.id.etPrice)
        val commissionInput = findViewById<EditText>(R.id.etCommission)
        val downpaymentInput = findViewById<EditText>(R.id.etDownpayment)

        val paymentOptionGroup = findViewById<RadioGroup>(R.id.rgPaymentOption)
        val saveBtn = findViewById<Button>(R.id.btnsave)
        val backBtn = findViewById<Button>(R.id.back)

        // Show/hide downpayment input based on payment option
        paymentOptionGroup.setOnCheckedChangeListener { _, checkedId ->
            downpaymentInput.visibility =
                if (checkedId == R.id.rbDownpayment) View.VISIBLE else View.GONE
        }

        backBtn.setOnClickListener {
            finish()
        }

        saveBtn.setOnClickListener {
            val address = addressInput.text.toString().trim()
            val price = priceInput.text.toString().toDoubleOrNull()
            val commission = commissionInput.text.toString().toDoubleOrNull()

            val isDownpayment = paymentOptionGroup.checkedRadioButtonId == R.id.rbDownpayment
            val paymentOption = if (isDownpayment) "Downpayment" else "Full Payment"
            val downpayment = if (isDownpayment) {
                downpaymentInput.text.toString().toDoubleOrNull()
            } else null

            // Input validation
            if (address.isBlank() || price == null || commission == null ||
                (isDownpayment && downpayment == null)
            ) {
                Toast.makeText(this, "Please fill in all fields correctly.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Load existing list
            val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPref.getString("addressList", null)
            val type = object : TypeToken<MutableList<Address>>() {}.type
            val addressList: MutableList<Address> =
                if (json != null) gson.fromJson(json, type) else mutableListOf()

            // Add new Address
            val newAddress = Address(
                address = address,
                price = price,
                commission = commission,
                paymentOption = paymentOption,
                isDownpayment = isDownpayment,
                downpayment = downpayment
            )
            addressList.add(newAddress)

            // Save updated list
            val updatedJson = gson.toJson(addressList)
            sharedPref.edit().putString("addressList", updatedJson).apply()

            Toast.makeText(this, "House info saved!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
