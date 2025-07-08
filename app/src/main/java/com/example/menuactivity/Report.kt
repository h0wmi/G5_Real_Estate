package com.example.menuactivity

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Report : AppCompatActivity() {

    private lateinit var searchInput: EditText
    private lateinit var searchButton: Button
    private lateinit var resultText: TextView
    private lateinit var listView: ListView
    private lateinit var totalCommissionText: TextView
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)


        searchInput = findViewById(R.id.searchInput)
        searchButton = findViewById(R.id.searchButton)
        resultText = findViewById(R.id.resultText)
        listView = findViewById(R.id.houseListView)
        totalCommissionText = findViewById(R.id.totalCommission)
        backButton = findViewById(R.id.backButton)

        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val gson = Gson()

        // Load stored data
        val personJson = sharedPref.getString("personList", null)
        val purchaseJson = sharedPref.getString("purchased_house", null)

        val personType = object : TypeToken<MutableList<Person>>() {}.type
        val purchaseType = object : TypeToken<MutableList<PurchasedHouse>>() {}.type

        val personList: MutableList<Person> = if (personJson != null) gson.fromJson(personJson, personType) else mutableListOf()
        val purchasedList: MutableList<PurchasedHouse> = if (purchaseJson != null) gson.fromJson(purchaseJson, purchaseType) else mutableListOf()

        // Search action
        searchButton.setOnClickListener {
            val query = searchInput.text.toString().trim()
            val foundPerson = personList.find { it.name.equals(query, ignoreCase = true) }

            if (foundPerson != null) {
                resultText.text = """
                ID: ${foundPerson.id}
                Name: ${foundPerson.name}
                Age: ${foundPerson.age}
                Email: ${foundPerson.email}
                Contact: ${foundPerson.contact}
                """.trimIndent()

                val housesLinked = purchasedList.filter { it.personnelName.equals(foundPerson.name, ignoreCase = true) }

                val houseDetails = housesLinked.map {
                    "House ID: ${it.houseId} | Price: ₱%.2f | Commission: ₱%.2f".format(it.housePrice, it.commission)
                }

                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, houseDetails)
                listView.adapter = adapter

                val totalCommission = housesLinked.sumOf { it.commission }
                totalCommissionText.text = "Total Commission Earned: ₱%.2f".format(totalCommission)

            } else {
                resultText.text = "Personnel not found."
                listView.adapter = null
                totalCommissionText.text = ""
            }
        }

        // Back button
        backButton.setOnClickListener {
            finish()
        }
    }
}
