package com.example.menuactivity

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class pDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdetail)

        val name = intent.getStringExtra("name")
        val idnum = intent.getStringExtra("idnum")
        val exitbtn = findViewById<Button>(R.id.back)


        val nameText = findViewById<TextView>(R.id.nameText)
        val idText = findViewById<TextView>(R.id.idText)

        nameText.text = "Name: $name"
        idText.text = "ID Number: $idnum"


        exitbtn.setOnClickListener {
            finish()
        }

    }
}