package com.example.celestialconnect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.celestialconnect.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditProfileFragment : Fragment() {

    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editUsername: EditText
    private lateinit var editPassword: EditText
    private lateinit var saveButton: Button
    private lateinit var nameUser: String
    private lateinit var emailUser: String
    private lateinit var usernameUser: String
    private lateinit var passwordUser: String
    private lateinit var reference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        reference = FirebaseDatabase.getInstance().getReference("users")

        editName = view.findViewById(R.id.editName)
        editEmail = view.findViewById(R.id.editEmail)
        editUsername = view.findViewById(R.id.editUsername)
        editPassword = view.findViewById(R.id.editPassword)
        saveButton = view.findViewById(R.id.saveButton)

        showData()

        saveButton.setOnClickListener {
            if (isNameChanged() || isPasswordChanged() || isEmailChanged()) {
                Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "No Changes Found", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun isNameChanged(): Boolean {
        return if (nameUser != editName.text.toString()) {
            reference.child(usernameUser).child("name").setValue(editName.text.toString())
            nameUser = editName.text.toString()
            true
        } else {
            false
        }
    }

    private fun isEmailChanged(): Boolean {
        return if (emailUser != editEmail.text.toString()) {
            reference.child(usernameUser).child("email").setValue(editEmail.text.toString())
            emailUser = editEmail.text.toString()
            true
        } else {
            false
        }
    }

    private fun isPasswordChanged(): Boolean {
        return if (passwordUser != editPassword.text.toString()) {
            reference.child(usernameUser).child("password").setValue(editPassword.text.toString())
            passwordUser = editPassword.text.toString()
            true
        } else {
            false
        }
    }

    private fun showData() {
        nameUser = arguments?.getString("name") ?: ""
        emailUser = arguments?.getString("email") ?: ""
        usernameUser = arguments?.getString("username") ?: ""
        passwordUser = arguments?.getString("password") ?: ""

        editName.setText(nameUser)
        editEmail.setText(emailUser)
        editUsername.setText(usernameUser)
        editPassword.setText(passwordUser)
    }
}
