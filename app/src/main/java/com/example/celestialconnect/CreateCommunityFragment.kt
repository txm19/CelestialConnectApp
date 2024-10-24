package com.example.celestialconnect


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class CreateCommunityFragment : Fragment() {
    private lateinit var name: EditText
    private lateinit var community_agenda: EditText
    private lateinit var community_guidelines: EditText
    private lateinit var login_button: Button
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_community, container, false)
        reference = FirebaseDatabase.getInstance().getReference("Communities")

        name = view.findViewById(R.id.name)
        community_agenda = view.findViewById(R.id.community_agenda)
        community_guidelines = view.findViewById(R.id.community_guidelines)
        login_button = view.findViewById(R.id.login_button)



        login_button.setOnClickListener {
            database = FirebaseDatabase.getInstance()
            reference = database.getReference("community")

            val community_name = name.text.toString()
            val agenda = community_agenda.text.toString()
            val guidelines = community_guidelines.text.toString()


            val helperClass = CommunityHelper(community_name, agenda, guidelines)
            reference.child(community_name).setValue(helperClass)

            Toast.makeText(requireContext(), "You have created a community successfully! You are the admin of this community", Toast.LENGTH_SHORT).show()

            // Find the create button


            // Set OnClickListener to the create button
            login_button.setOnClickListener {
                // Create an instance of CreateCommunityFragment
                val newFragment = ShortsFragment()

                // Get the FragmentManager
                val fragmentManager = requireActivity().supportFragmentManager

                // Begin a FragmentTransaction
                val transaction = fragmentManager.beginTransaction()

                // Replace the current fragment with the new fragment
                transaction.replace(R.id.fragment_container, newFragment)

                // Add the transaction to the back stack
                transaction.addToBackStack(null)

                // Commit the transaction
                transaction.commit()
            }
        }

        return view


    }

}