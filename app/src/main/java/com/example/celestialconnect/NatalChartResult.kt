package com.example.celestialconnect


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.celestialconnect.R.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NatalChartResult : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.natalchartresult)

        val name = intent.getStringExtra("name")

        // Display greeting with the name
        val greetingTextView = findViewById<TextView>(id.greetingTextView)
        greetingTextView.text = "Hello, $name!"

        val dobString = intent.getStringExtra("dob")
        val zodiacSignTextView = findViewById<TextView>(id.zodiacSignTextView)
        val zodiacSign = calculateZodiacSign(dobString)
        zodiacSignTextView.text = "You are $zodiacSign!"
    }

    private fun calculateZodiacSign(dobString: String?): String {
        // Parse date of birth string to Date object
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val dobDate = dateFormat.parse(dobString ?: "") ?: return "Unknown"

        // Get the month and day from the date of birth
        val calendar = Calendar.getInstance()
        calendar.time = dobDate
        val month = calendar.get(Calendar.MONTH) + 1 // Months are zero-based, so add 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Determine zodiac sign based on month and day
        return when (month) {
            1 -> if (day < 20) "Capricorn" else "Aquarius"
            2 -> if (day < 19) "Aquarius" else "Pisces"
            3 -> if (day < 21) "Pisces" else "Aries"
            4 -> if (day < 20) "Aries" else "Taurus"
            5 -> if (day < 21) "Taurus" else "Gemini"
            6 -> if (day < 21) "Gemini" else "Cancer"
            7 -> if (day < 23) "Cancer" else "Leo"
            8 -> if (day < 23) "Leo" else "Virgo"
            9 -> if (day < 23) "Virgo" else "Libra"
            10 -> if (day < 23) "Libra" else "Scorpio"
            11 -> if (day < 22) "Scorpio" else "Sagittarius"
            else -> "Sagittarius" // December
        }
    }
}