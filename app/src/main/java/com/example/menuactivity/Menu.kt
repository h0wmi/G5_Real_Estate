package com.example.menuactivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)


        val personnelBtn = findViewById<Button>(R.id.btn)
        personnelBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, Personnel::class.java)
            startActivity(intent)
        })

        val homeBtn = findViewById<Button>(R.id.btn2)
        homeBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, House::class.java)
            startActivity(intent)
        })
    }

}