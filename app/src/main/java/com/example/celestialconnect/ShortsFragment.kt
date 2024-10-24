package com.example.celestialconnect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import android.widget.Button




// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShortsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShortsFragment : Fragment() {
    //
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shorts, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Find the create button
        val createButton: Button = view.findViewById(R.id.btn_create_community)

        // Set OnClickListener to the create button
        createButton.setOnClickListener {
            // Create an instance of CreateCommunityFragment
            val newFragment = CreateCommunityFragment()

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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ShortsFragment.
         */
        //
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShortsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}