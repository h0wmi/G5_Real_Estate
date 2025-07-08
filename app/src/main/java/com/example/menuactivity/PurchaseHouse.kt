package com.example.menuactivity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PurchaseHouseActivity : AppCompatActivity() {
    private lateinit var houseSpinner: Spinner
    private lateinit var personnelSpinner: Spinner
    private lateinit var purchaseButton: Button
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_house)

        houseSpinner = findViewById(R.id.spinnerHouse)
        personnelSpinner = findViewById(R.id.spinnerPersonnel)
        purchaseButton = findViewById(R.id.purchaseButton)
        backButton = findViewById(R.id.back)

        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val gson = Gson()

        val houseJson = sharedPref.getString("addressList", null)
        val personJson = sharedPref.getString("personList", null)
        val purchasedJson = sharedPref.getString("purchased_house", null)

        val houseType = object : TypeToken<MutableList<Address>>() {}.type
        val personType = object : TypeToken<MutableList<Person>>() {}.type
        val purchaseType = object : TypeToken<MutableList<PurchasedHouse>>() {}.type

        val houseList: MutableList<Address> = if (houseJson != null) gson.fromJson(houseJson, houseType) else mutableListOf()
        val personList: MutableList<Person> = if (personJson != null) gson.fromJson(personJson, personType) else mutableListOf()
        val purchasedList: MutableList<PurchasedHouse> = if (purchasedJson != null) gson.fromJson(purchasedJson, purchaseType) else mutableListOf()

        //Filter out already-purchased houses
        val filteredHouses = houseList.filter { house ->
            purchasedList.none { it.houseId == house.id }
        }

        // Set filtered house list in spinner
        val houseAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, filteredHouses.map { "${it.id} - ${it.address}" })
        houseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        houseSpinner.adapter = houseAdapter

        val personnelAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, personList.map { "${it.name} (ID: ${it.id})" })
        personnelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        personnelSpinner.adapter = personnelAdapter



        purchaseButton.setOnClickListener {
            if (filteredHouses.isEmpty()) {
                Toast.makeText(this, "No available houses to purchase.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedHouse = filteredHouses[houseSpinner.selectedItemPosition]
            val selectedPerson = personList[personnelSpinner.selectedItemPosition]
            val totalCommission = selectedHouse.price * (selectedHouse.commission / 100)

            val newPurchase = PurchasedHouse(
                houseId = selectedHouse.id,
                personnelName = selectedPerson.name,
                commission = totalCommission,
                housePrice = selectedHouse.price
            )

            purchasedList.add(newPurchase)
            sharedPref.edit().putString("purchased_house", gson.toJson(purchasedList)).apply()

            Toast.makeText(this, "${selectedHouse.address} Sold by ${selectedPerson.name}", Toast.LENGTH_LONG).show()

            // Refresh the activity to update the spinner
            finish()
            startActivity(intent)
        }

        backButton.setOnClickListener {
            finish()
        }
    }
}
