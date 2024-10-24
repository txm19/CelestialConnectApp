package com.example.celestialconnect

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.io.IOException

class ProfileFragment : Fragment() {

    private lateinit var profileName: TextView
    private lateinit var profileUsername: TextView
    private lateinit var titleName: TextView
    private lateinit var titleUsername: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profilePassword: TextView
    private lateinit var bio: TextView
    private lateinit var changeProfileImg: Button
    private lateinit var followersCount: TextView
    private lateinit var followButton: Button
    private lateinit var editProfile: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<HelperClass>
    private lateinit var adapter: MyAdapter
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Images")

    private lateinit var profileImage: ImageView
    private var profilePicUri: Uri? = null

    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        titleName = view.findViewById(R.id.titleName)
        titleUsername = view.findViewById(R.id.titleUsername)
        bio = view.findViewById(R.id.bio)
        followersCount = view.findViewById(R.id.followersCount)
        followButton = view.findViewById(R.id.followButton)
        editProfile = view.findViewById(R.id.editButton)
        profileImage = view.findViewById(R.id.profileImage)

        changeProfileImg = view.findViewById(R.id.changeProfileImg)

        changeProfileImg.setOnClickListener {
            openFileChooser()
        }

        recyclerView = view.findViewById(R.id.profileRecyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        dataList = ArrayList()
        adapter = MyAdapter(requireContext(), dataList)
        recyclerView.adapter = adapter
        showAllUserData()

        editProfile.setOnClickListener {
            passUserData()
        }

        // Restore selected image URI if it was previously selected
        if (savedInstanceState != null && savedInstanceState.containsKey("profilePicUri")) {
            profilePicUri = savedInstanceState.getParcelable("profilePicUri")
            profilePicUri?.let { uri ->
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
                    profileImage.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        profilePicUri?.let {
            outState.putParcelable("profilePicUri", it)
        }
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            profilePicUri = data.data
            profilePicUri?.let { uri ->
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
                    profileImage.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun showAllUserData() {
        val intent = requireActivity().intent
        val nameUser = intent.getStringExtra("name")
        val usernameUser = intent.getStringExtra("username")
        val bioUser = intent.getStringExtra("bio")
        val followersUser = intent.getIntExtra("followers", 0)

        titleName.text = nameUser
        titleUsername.text = usernameUser
        bio.text = bioUser
        followersCount.text = "Followers: $followersUser"
    }

    override fun onStart() {
        super.onStart()
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (dataSnapshot in snapshot.children) {
                    val helperClass: HelperClass? = dataSnapshot.getValue(HelperClass::class.java)
                    helperClass?.let { dataList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun passUserData() {
        val userUsername = titleUsername.text.toString().trim()

        val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
        val checkUserDatabase: Query = reference.orderByChild("username").equalTo(userUsername)

        checkUserDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val helperClass = snapshot.children.first().getValue(HelperClass::class.java)

                    val intent = Intent(requireActivity(), EditProfileActivity::class.java)
                    intent.putExtra("name", helperClass?.name)
                    intent.putExtra("username", helperClass?.username)
                    intent.putExtra("bio", helperClass?.bio)
                    intent.putExtra("followers", helperClass?.followers)
                    intent.putExtra("email", helperClass?.email)
                    intent.putExtra("password", helperClass?.password)
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
