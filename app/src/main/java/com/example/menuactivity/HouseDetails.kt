package com.example.menuactivity

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HouseDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house_details)

        val houseNumber = intent.getStringExtra("houseNumber")
        val address = intent.getStringExtra("address")
        val exitbtn = findViewById<Button>(R.id.back)


        val houseNumText = findViewById<TextView>(R.id.hnum)
        val addressText = findViewById<TextView>(R.id.add)

        houseNumText.text = "House Number: $houseNumber"
        addressText.text = "Address: $address"

        exitbtn.setOnClickListener {
            finish()
        }

    }
}
