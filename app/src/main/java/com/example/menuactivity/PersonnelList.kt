package com.example.menuactivity

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PersonnelList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_personnel_list)

        val mainBtn = findViewById<Button>(R.id.btn10)
        mainBtn.setOnClickListener(View.OnClickListener {
            finish()
        })
    }
}