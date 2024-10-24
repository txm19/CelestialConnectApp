package com.example.celestialconnect

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UploadFragment : Fragment() {

    private lateinit var uploadButton: FloatingActionButton
    private lateinit var uploadImage: ImageView
    private lateinit var uploadCaption: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var imageUri: Uri
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Images")
    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_upload, container, false)
    }
    private fun openFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment) // Replace fragment_container with the id of the container in your activity layout
        transaction.addToBackStack(null)
        transaction.commit()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uploadButton = view.findViewById(R.id.uploadButton)
        uploadCaption = view.findViewById(R.id.uploadCaption)
        uploadImage = view.findViewById(R.id.uploadImage)
        progressBar = view.findViewById(R.id.progressBar)
        progressBar.visibility = View.INVISIBLE

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback<ActivityResult> { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    imageUri = data?.data!!
                    uploadImage.setImageURI(imageUri)
                } else {
                    Toast.makeText(requireActivity(), "No Image Selected", Toast.LENGTH_SHORT).show()
                }
            }
        )

        uploadImage.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_GET_CONTENT)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }

        uploadButton.setOnClickListener {
            if (::imageUri.isInitialized) {
                uploadToFirebase(imageUri)
            } else {
                Toast.makeText(requireActivity(), "Please select image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadToFirebase(uri: Uri) {
        val caption = uploadCaption.text.toString()
        val imageReference = storageReference.child("${System.currentTimeMillis()}.${getFileExtension(uri)}")

        val uploadTask = imageReference.putFile(uri)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            imageReference.downloadUrl.addOnSuccessListener { uri ->
                val helperClass = HelperClass(
                    null,
                    null,
                    null,
                    null,
                    0,
                    null,
                    uri.toString(),
                    caption//,
                    //null
                )
                val key = databaseReference.push().key
                if (key != null) {
                    databaseReference.child(key).setValue(helperClass)
                }
                progressBar.visibility = View.INVISIBLE
                Toast.makeText(requireActivity(), "Uploaded", Toast.LENGTH_SHORT).show()
                // Redirect to AccountActivity after successful upload
                openFragment(HomeFragment())
            }
        }.addOnFailureListener { exception ->
            progressBar.visibility = View.INVISIBLE
            Toast.makeText(requireActivity(), "Failed: $exception", Toast.LENGTH_SHORT).show()
        }.addOnProgressListener { snapshot ->
            val progress = (100.0 * snapshot.bytesTransferred / snapshot.totalByteCount)
            progressBar.progress = progress.toInt()
        }
    }

    private fun getFileExtension(fileUri: Uri): String? {
        val contentResolver: ContentResolver = requireActivity().contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri))
    }
}
