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
    private lateinit var commissionText: TextView
    private lateinit var purchaseButton: Button
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_house)

        houseSpinner = findViewById(R.id.spinnerHouse)
        personnelSpinner = findViewById(R.id.spinnerPersonnel)
        commissionText = findViewById(R.id.commissionText)
        purchaseButton = findViewById(R.id.purchaseButton)
        backButton = findViewById(R.id.back)

        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val gson = Gson()

        val houseJson = sharedPref.getString("addressList", null)
        val personJson = sharedPref.getString("personList", null)

        val houseType = object : TypeToken<MutableList<Address>>() {}.type
        val personType = object : TypeToken<MutableList<Person>>() {}.type

        val houseList: MutableList<Address> = if (houseJson != null) gson.fromJson(houseJson, houseType) else mutableListOf()
        val personList: MutableList<Person> = if (personJson != null) gson.fromJson(personJson, personType) else mutableListOf()

        val houseAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, houseList.map { "${it.id} - ${it.address}" })
        houseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        houseSpinner.adapter = houseAdapter

        val personnelAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, personList.map { "${it.name} (ID: ${it.id})" })
        personnelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        personnelSpinner.adapter = personnelAdapter

        houseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedHouse = houseList[position]
                val totalCommission = selectedHouse.price * (selectedHouse.commission / 100)
                commissionText.text = "Total Commission: ₱%.2f".format(totalCommission)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


        purchaseButton.setOnClickListener {
            val selectedHouse = houseList[houseSpinner.selectedItemPosition]
            val selectedPerson = personList[personnelSpinner.selectedItemPosition]

            Toast.makeText(this, "${selectedHouse.address} linked to ${selectedPerson.name}", Toast.LENGTH_LONG).show()
        }

        backButton.setOnClickListener {
            finish()
        }
    }
}
