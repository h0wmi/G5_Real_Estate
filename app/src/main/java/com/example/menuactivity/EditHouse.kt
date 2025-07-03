package com.example.menuactivity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class EditHouse : AppCompatActivity() {

    private lateinit var originalAddress: Address

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_house)

        val addressInput = findViewById<EditText>(R.id.add)
        val priceInput = findViewById<EditText>(R.id.price)
        val commissionInput = findViewById<EditText>(R.id.commission)
        val downpaymentInput = findViewById<EditText>(R.id.downPaymentAmount)
        val paymentTypeGroup = findViewById<RadioGroup>(R.id.paymentType)
        val updateBtn = findViewById<Button>(R.id.btnUpdate)
        val cancelBtn = findViewById<Button>(R.id.btnCancel)

        // Load original selected house
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPref.getString("selectedHouse", null)
        val type = object : TypeToken<Address>() {}.type

        if (json == null) {
            Toast.makeText(this, "No house selected", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        originalAddress = gson.fromJson(json, type)

        // Populate UI fields
        addressInput.setText(originalAddress.address)
        priceInput.setText(originalAddress.price.toString())
        commissionInput.setText(originalAddress.commission.toString())

        if (originalAddress.isDownpayment) {
            paymentTypeGroup.check(R.id.downPayment)
            downpaymentInput.visibility = View.VISIBLE
            downpaymentInput.setText(originalAddress.downpayment?.toString() ?: "")
        } else {
            paymentTypeGroup.check(R.id.fullPayment)
            downpaymentInput.visibility = View.GONE
        }

        // Toggle visibility
        paymentTypeGroup.setOnCheckedChangeListener { _, checkedId ->
            downpaymentInput.visibility = if (checkedId == R.id.downPayment) View.VISIBLE else View.GONE
        }

        updateBtn.setOnClickListener {
            val newAddress = addressInput.text.toString().trim()
            val newPrice = priceInput.text.toString().toDoubleOrNull()
            val newCommission = commissionInput.text.toString().toDoubleOrNull()
            val isDownpayment = paymentTypeGroup.checkedRadioButtonId == R.id.downPayment
            val newDownpayment = if (isDownpayment) {
                downpaymentInput.text.toString().toDoubleOrNull()
            } else null

            if (newAddress.isBlank() || newPrice == null || newCommission == null || (isDownpayment && newDownpayment == null)) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create updated Address
            val updatedAddress = Address(
                id = originalAddress.id,
                address = newAddress,
                price = newPrice,
                commission = newCommission,
                paymentOption = if (isDownpayment) "Downpayment" else "Full Payment",
                isDownpayment = isDownpayment,
                downpayment = newDownpayment
            )

            // Load address list
            val listJson = sharedPref.getString("addressList", null)
            val listType = object : TypeToken<MutableList<Address>>() {}.type
            val addressList: MutableList<Address> =
                if (listJson != null) gson.fromJson(listJson, listType) else mutableListOf()

            // Replace the original
            val index = addressList.indexOfFirst { it.id == originalAddress.id }
            if (index != -1) {
                addressList[index] = updatedAddress
                sharedPref.edit().putString("addressList", gson.toJson(addressList)).apply()
                Toast.makeText(this, "House updated successfully!", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "Original house not found", Toast.LENGTH_SHORT).show()
            }
        }

        cancelBtn.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}
