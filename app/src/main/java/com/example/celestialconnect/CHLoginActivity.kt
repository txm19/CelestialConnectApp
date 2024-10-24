package com.example.celestialconnect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class CHLoginActivity : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chlogin)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnLogin = findViewById(R.id.btnLogin)
        btnSignUp = findViewById(R.id.btnSignUp)


        //Redirect to the Sign Up activity
        btnSignUp.setOnClickListener {
            val intent =  Intent(this, CHSignUpActivity::class.java)//Jump from currentActivity to Signup
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            login(email, password)
        }

    }

    private fun login(email: String, password: String){
        //logic for logging in user
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // code for logging in user
                    val intent = Intent(this@CHLoginActivity, CHMainActivity::class.java)
                    //finish()
                    startActivity(intent)
                } else {
                    Toast.makeText(this@CHLoginActivity, "User doesn't exist", Toast.LENGTH_SHORT).show()
                }
            }

    }
}

