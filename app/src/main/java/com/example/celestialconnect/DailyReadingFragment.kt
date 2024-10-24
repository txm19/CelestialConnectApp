package com.example.celestialconnect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import java.util.*
import com.google.firebase.database.*
import com.google.firebase.auth.FirebaseAuth
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DailyReadingFragment : Fragment() {
    //
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var greetingTextView: TextView
    private var firstName: String? = null
    private var titleName: String? = null

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Images")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.activity_dailyreading, container, false)
        greetingTextView = view.findViewById(R.id.greetingTextView)

        showAllUserData()

        greetingTextView.text = getGreeting(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) + ", ${titleName ?: "User"}!"

        val spinner = view.findViewById<Spinner>(R.id.zodiacSpinner)
        val submitButton = view.findViewById<Button>(R.id.submitButton)

        // Initialize spinner with zodiac signs
        val zodiacSigns = resources.getStringArray(R.array.zodiac_signs)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, zodiacSigns)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter


        // Handle submit button click
        submitButton.setOnClickListener {
            val selectedSign = spinner.selectedItem.toString().lowercase()
            fetchHoroscope(selectedSign)
        }
        return view
    }

    private fun showAllUserData() {
        val intent = requireActivity().intent
        val nameUser = intent.getStringExtra("name")?.split(" ")?.getOrNull(0)
        titleName = nameUser ?: ""
    }

    // Function to get the appropriate greeting based on the time of day
    private fun getGreeting(hour: Int): String {
        return when (hour) {
            in 0..11 -> "Good morning"
            in 12..15 -> "Good afternoon"
            in 16..20 -> "Good evening"
            else -> "Good night"
        }
    }

    private fun fetchHoroscope(zodiacSign: String) {
        val url = "https://daily-horoscope-api.p.rapidapi.com/api/Daily-Horoscope-English/?zodiacSign=$zodiacSign&timePeriod=today"
        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("X-RapidAPI-Key", "92d35972ebmsh4d44f1016585c65p173389jsnee75bd1490dd")
            .addHeader("X-RapidAPI-Host", "daily-horoscope-api.p.rapidapi.com")
            .build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    activity?.runOnUiThread {
                        // Update UI on the main thread
                        //val userName = firstName                 //greetingTextView.text.toString()
                        //view?.findViewById<TextView>(R.id.textView3)?.text = responseData

                        val horoscopeText = responseData?.let { parseHoroscopeResponse(it) }
                        val fullMessage = "$titleName, $horoscopeText"
                        view?.findViewById<TextView>(R.id.textView3)?.text = fullMessage
                    }
                }
            }
        })
    }

    private fun parseHoroscopeResponse(response: String): String {
        // Parse the JSON response and extract the prediction
        try {
            val jsonResponse = JSONObject(response)
            val prediction = jsonResponse.getString("prediction")
            // Remove unnecessary characters
            val formattedPrediction = prediction.replace("\"", "").replace("status:true,", "")
            return formattedPrediction
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return ""
    }

}