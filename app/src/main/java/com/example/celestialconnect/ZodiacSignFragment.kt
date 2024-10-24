package com.example.celestialconnect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlin.text.toInt

import androidx.fragment.app.Fragment
import com.example.celestialconnect.databinding.FragmentZodiacSignBinding

class ZodiacSignFragment : Fragment() {

    private lateinit var binding: FragmentZodiacSignBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentZodiacSignBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the birth date from arguments
        val birthDate = arguments?.getString(BIRTH_DATE_KEY)

        // Determine the Zodiac sign based on birth date
        val zodiacSign = getZodiacSign(birthDate)

        // Display the Zodiac sign in the UI
        binding.zodiacSignTextView.text = zodiacSign
    }

    private fun getZodiacSign(birthDate: String?): String {
        // Implement logic to determine Zodiac sign based on birth date
        // For example, you can use if-else statements to check the birth date range and return the corresponding Zodiac sign
        // Here's a simple example:

        val (month, day) = (birthDate?.split("/") ?: listOf("0", "0")).run {
            getOrNull(0) to getOrNull(1)
        }
        val dayInt = day?.toIntOrNull() ?: 0


        return when (month) {
            "01" -> if (dayInt >= 20) "Aquarius" else "Capricorn"
            "02" -> if (dayInt >= 19) "Pisces" else "Aquarius"
            "03" -> if (dayInt >= 21) "Aries" else "Pisces"
            "04" -> if (dayInt >= 20) "Taurus" else "Aries"
            "05" -> if (dayInt >= 21) "Gemini" else "Taurus"
            "06" -> if (dayInt >= 21) "Cancer" else "Gemini"
            "07" -> if (dayInt >= 23) "Leo" else "Cancer"
            "08" -> if (dayInt >= 23) "Virgo" else "Leo"
            "09" -> if (dayInt >= 23) "Libra" else "Virgo"
            "10" -> if (dayInt >= 23) "Scorpio" else "Libra"
            "11" -> if (dayInt >= 22) "Sagittarius" else "Scorpio"
            "12" -> if (dayInt >= 22) "Capricorn" else "Sagittarius"
            else -> "Unknown"
        }
    }

    companion object {
        private const val BIRTH_DATE_KEY = "BIRTH_DATE"

        fun newInstance(birthDate: String): ZodiacSignFragment {
            val fragment = ZodiacSignFragment()
            val args = Bundle()
            args.putString(BIRTH_DATE_KEY, birthDate)
            fragment.arguments = args
            return fragment
        }
    }
}
