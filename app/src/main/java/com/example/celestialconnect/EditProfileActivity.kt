package com.example.celestialconnect

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditProfileActivity : AppCompatActivity() {

    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editUsername: EditText
    private lateinit var editPassword: EditText
    private lateinit var editBio: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var nameUser: String
    private lateinit var bio: String
    private lateinit var emailUser: String
    private lateinit var usernameUser: String
    private lateinit var passwordUser: String
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_edit_profile)

        reference = FirebaseDatabase.getInstance().getReference("users")

        editName = findViewById(R.id.editName)
        editBio = findViewById(R.id.editBio)
        editEmail = findViewById(R.id.editEmail)
        editUsername = findViewById(R.id.editUsername)
        editPassword = findViewById(R.id.editPassword)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton) // Initialize cancelButton

        showData()

        saveButton.setOnClickListener {
            if (isNameChanged() || isPasswordChanged() || isEmailChanged() || isUsernameChanged() || isBioChanged()){
                Toast.makeText(this@EditProfileActivity, "Saved", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@EditProfileActivity, AccountActivity::class.java).apply {
                    putExtra("name", nameUser)
                    putExtra("email", emailUser)
                    putExtra("username", usernameUser)
                    putExtra("password", passwordUser)
                    putExtra("bio", bio)
                }
                startActivity(intent)
                finish() // Optional, depending on whether you want to close EditProfileActivity
            } else {
                Toast.makeText(this@EditProfileActivity, "No Changes Found", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            Toast.makeText(this@EditProfileActivity, "No Changes made", Toast.LENGTH_SHORT).show()
            finish() // Close EditProfileActivity when cancel button is clicked
        }
    }

    private fun isNameChanged(): Boolean {
        return if (nameUser != editName.text.toString()){
            reference.child(usernameUser).child("name").setValue(editName.text.toString())
            nameUser = editName.text.toString()
            true
        } else {
            false
        }
    }

    private fun isBioChanged(): Boolean {
        return if (bio != editBio.text.toString()) {
            val userRef = reference.child(usernameUser)
            userRef.child("bio").setValue(editBio.text.toString())
            bio = editBio.text.toString()
            true
        } else {
            false
        }
    }

    private fun isEmailChanged(): Boolean {
        return if (emailUser != editEmail.text.toString()){
            reference.child(usernameUser).child("email").setValue(editEmail.text.toString())
            emailUser = editEmail.text.toString()
            true
        } else {
            false
        }
    }

    private fun isPasswordChanged(): Boolean {
        return if (passwordUser != editPassword.text.toString()){
            reference.child(usernameUser).child("password").setValue(editPassword.text.toString())
            passwordUser = editPassword.text.toString()
            true
        } else {
            false
        }
    }

    private fun isUsernameChanged(): Boolean {
        return if (usernameUser != editUsername.text.toString()){
            reference.child(usernameUser).child("username").setValue(editUsername.text.toString())
            usernameUser = editUsername.text.toString()
            true
        } else {
            false
        }
    }

    private fun showData(){
        val intent = intent

        nameUser = intent.getStringExtra("name") ?: ""
        bio = intent.getStringExtra("bio") ?: ""
        emailUser = intent.getStringExtra("email") ?: ""
        usernameUser = intent.getStringExtra("username") ?: ""
        passwordUser = intent.getStringExtra("password") ?: ""

        editName.setText(nameUser)
        editBio.setText(bio)
        editEmail.setText(emailUser)
        editUsername.setText(usernameUser)
        editPassword.setText(passwordUser)
    }
}
