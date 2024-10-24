package com.example.celestialconnect

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignupActivity : AppCompatActivity() {

    private lateinit var signupName: EditText
    private lateinit var signupUsername: EditText
    private lateinit var signupEmail: EditText
    private lateinit var signupPassword: EditText
    private lateinit var loginRedirectText: TextView
    private lateinit var signupButton: Button
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signupName = findViewById(R.id.signup_name)
        signupEmail = findViewById(R.id.signup_email)
        signupUsername = findViewById(R.id.signup_username)
        signupPassword = findViewById(R.id.signup_password)
        loginRedirectText = findViewById(R.id.loginRedirectText)
        signupButton = findViewById(R.id.signup_button)

        signupButton.setOnClickListener {
            database = FirebaseDatabase.getInstance()
            reference = database.getReference("users")

            val name = signupName.text.toString()
            val email = signupEmail.text.toString()
            val username = signupUsername.text.toString()
            val password = signupPassword.text.toString()
            val followers = 0 // Initial followers count
            val bio = "" // Initial bio
            val profilePic = ""

            var error = false

            // Check if email contains '@' symbol
            if (!email.contains('@')) {
                signupEmail.error = "Email must contain '@' symbol."
                error = true
            }

            // Check if password meets the requirements
            if (!isPasswordValid(password)) {
                signupPassword.error = "Password must contain special characters and numbers."
                error = true
            }

            // Check if username is already taken
            reference.child(username).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        signupUsername.error = "Username is already taken."
                        error = true
                    }

                    // If there are any errors, don't proceed further
                    if (error) return

                    // Proceed with signup
                    val helperClass = HelperClass(name, email, username, password, followers, bio, null, null)//,profilePic)
                    reference.child(username).setValue(helperClass)

                    val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                    startActivity(intent)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }
            })
        }

        loginRedirectText.setOnClickListener {
            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    // Function to check if password meets the requirements
    private fun isPasswordValid(password: String): Boolean {
        val specialCharacters = setOf('!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+', '=')
        var hasSpecialChar = false
        var hasNumber = false

        for (char in password) {
            if (specialCharacters.contains(char)) {
                hasSpecialChar = true
            }
            if (char.isDigit()) {
                hasNumber = true
            }
        }

        return hasSpecialChar && hasNumber
    }
}