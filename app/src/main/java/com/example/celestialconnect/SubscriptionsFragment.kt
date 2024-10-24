package com.example.celestialconnect

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

//
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SubscriptionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubscriptionsFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_subscriptions, container, false)
        val buttonCalculateCompatibility: Button = view.findViewById(R.id.buttonCalculateCompatibility)

        buttonCalculateCompatibility.setOnClickListener {
            // Handle button click
            val intent = Intent(activity, Compatibility::class.java)
            startActivity(intent)

        }
        val buttonManuallyAddConnection:Button = view.findViewById(R.id.buttonManuallyAddConnection)
        buttonManuallyAddConnection.setOnClickListener {
            // Handle button click
            val intent = Intent(activity, NatalChart::class.java)
            startActivity(intent)

        }
        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SubscriptionsFragment.
         */
        //
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SubscriptionsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}