package com.example.celestialconnect

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar
import androidx.navigation.fragment.findNavController



class NatalChart : AppCompatActivity()
{
    private val apiKey = "AIzaSyD0gaoxu2O40mZyYTqmpZalI0pW67UeOaU"
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_natalchart)

        if(!Places.isInitialized()){
            Places.initialize(applicationContext, apiKey)
        }

        val autocompleteSupportFragment = (supportFragmentManager.findFragmentById(R.id.autoCompleteTextViewPlace) as AutocompleteSupportFragment).setPlaceFields(
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS_COMPONENTS)
        )

        autocompleteSupportFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // Get the selected place name
                val selectedPlaceName = place.name
                var selectedPlaceAddress = ""

                // Extract city, state, and country from the address components
                val addressComponents = place.addressComponents
                val city = addressComponents?.asList()?.firstOrNull { it.types.contains("locality") }?.name
                val state = addressComponents?.asList()?.firstOrNull { it.types.contains("administrative_area_level_1") }?.name
                val country = addressComponents?.asList()?.firstOrNull { it.types.contains("country") }?.name

                // Format the address
                selectedPlaceAddress = if (!city.isNullOrBlank()) {
                    "$city"
                } else {
                    ""
                }

                if (!state.isNullOrBlank()) {
                    selectedPlaceAddress += if (selectedPlaceAddress.isNotEmpty()) ", $state" else state
                }

                if (!country.isNullOrBlank()) {
                    selectedPlaceAddress += if (selectedPlaceAddress.isNotEmpty()) ", $country" else country
                }

                // Update the TextInputEditText with the selected place address
                findViewById<TextInputEditText>(R.id.TextInputEditTextPlace).setText(selectedPlaceAddress)
            }

            override fun onError(status: Status) {
                // Handle error
                Log.e(TAG, "An error occurred: $status")
            }
        })

        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            if (validateInputs()) {
                val name = findViewById<TextInputEditText>(R.id.TextInputEditText).text.toString()
                val dateOfBirth = findViewById<TextInputEditText>(R.id.TextInputEditTextDateOfBirth).text.toString()
                val intent = Intent(this, NatalChartResult::class.java)
                intent.putExtra("name", name)
                intent.putExtra("dob", dateOfBirth)
                startActivity(intent)
            } else {
                // Display error message to user (optional)
            }
        }

    }

    private fun validateInputs(): Boolean {
        val name = findViewById<TextInputEditText>(R.id.TextInputEditText).text.toString()
        val placeOfBirth = findViewById<TextInputEditText>(R.id.TextInputEditTextPlace).text.toString()
        val dateOfBirth = findViewById<TextInputEditText>(R.id.TextInputEditTextDateOfBirth).text.toString()
        val timeOfBirth = findViewById<TextInputEditText>(R.id.TextInputEditTextTimeOfBirth).text.toString()

        return name.isNotBlank() && placeOfBirth.isNotBlank() && dateOfBirth.isNotBlank() && timeOfBirth.isNotBlank()
    }

    companion object {
        private const val TAG = "NatalChart"
    }

    fun showDatePicker(view: View) {
        val editTextDateOfBirth = findViewById<EditText>(R.id.TextInputEditTextDateOfBirth)

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format(
                    "%02d/%02d/%04d",
                    selectedMonth + 1,
                    selectedDay,
                    selectedYear
                )
                editTextDateOfBirth.setText(formattedDate)
            },
            year,
            month,
            day
        )

        // Set the maximum date to today's date
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        // Set the minimum date to many years back
        val maxYear = calendar.get(Calendar.YEAR)
        val minYear = maxYear - 100 // Adjust this value as needed
        datePickerDialog.datePicker.minDate = getMillisForDate(1, 1, minYear)

        datePickerDialog.show()
    }

    // Function to convert date to milliseconds
    private fun getMillisForDate(day: Int, month: Int, year: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day)
        return calendar.timeInMillis
    }

    fun showTimePicker(view: View) {
        val editTextTimeOfBirth = findViewById<EditText>(R.id.TextInputEditTextTimeOfBirth)

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                editTextTimeOfBirth.setText(formattedTime)
            },
            hour,
            minute,
            false // Set to true for 24-hour time format, false for AM/PM format
        )
        timePickerDialog.show()
    }
}