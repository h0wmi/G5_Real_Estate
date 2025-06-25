package com.example.menuactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.menuactivity.databinding.ActivityMainBinding
import android.content.Intent

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener(View.OnClickListener {
            val enteredUsername = binding.username.text.toString()
            val enteredPassword = binding.password.text.toString()

            if (enteredUsername == "admin" && enteredPassword == "1234") {
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                // Navigate to next activity here
                val intent = Intent(this, Menu::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}