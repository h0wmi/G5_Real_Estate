package com.example.menuactivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class House : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_house)

        val ahBtn = findViewById<Button>(R.id.btn7)
        ahBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, AddHouse::class.java)
            startActivity(intent)
        })

        val lohBtn = findViewById<Button>(R.id.btn8)
        lohBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, HouseList::class.java)
            startActivity(intent)
        })





        val mainBtn = findViewById<Button>(R.id.btn4)
        mainBtn.setOnClickListener(View.OnClickListener {
            finish()
        })

    }
}