package com.example.assignment1helloandroid

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import com.example.assignment1helloandroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        // Set all buttons to call the handleButtonClick method when they are clicked.
        binding.button1.setOnClickListener{ handleButtonClick(binding.button1) }
        binding.button2.setOnClickListener{ handleButtonClick(binding.button2) }
        binding.button3.setOnClickListener{ handleButtonClick(binding.button3) }
        binding.button4.setOnClickListener{ handleButtonClick(binding.button4) }
        binding.button5.setOnClickListener{ handleButtonClick(binding.button5) }

        setContentView(binding.root)
    }

    fun handleButtonClick(button: Button) {
        val intent = Intent(this, SecondActivity::class.java)

        // Pass the button text to the next screen.
        intent.putExtra("theText", button.text.toString())
        startActivity(intent)
    }
}