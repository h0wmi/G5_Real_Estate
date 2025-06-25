package com.example.menuactivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Personnel : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_personnel)

        val apBtn = findViewById<Button>(R.id.btn5)
        apBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, AddPersonnel::class.java)
            startActivity(intent)
        })

        val lopBtn = findViewById<Button>(R.id.btn6)
        lopBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, PersonnelList::class.java)
            startActivity(intent)
        })

        val mainBtn = findViewById<Button>(R.id.btn3)
        mainBtn.setOnClickListener(View.OnClickListener {
            finish()
        })

    }
}