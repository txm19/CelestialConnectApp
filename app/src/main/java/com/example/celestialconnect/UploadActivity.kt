package com.example.celestialconnect

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class UploadActivity : AppCompatActivity() {

    private lateinit var uploadButton: FloatingActionButton
    private lateinit var uploadImage: ImageView
    private lateinit var uploadCaption: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var imageUri: Uri
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Images")
    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        uploadButton = findViewById(R.id.uploadButton)
        uploadCaption = findViewById(R.id.uploadCaption)
        uploadImage = findViewById(R.id.uploadImage)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.INVISIBLE

        val activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback<ActivityResult> { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    imageUri = data?.data!!
                    uploadImage.setImageURI(imageUri)
                } else {
                    Toast.makeText(this@UploadActivity, "No Image Selected", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this@UploadActivity, "Please select image", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this@UploadActivity, "Uploaded", Toast.LENGTH_SHORT).show()
                // Redirect to AccountActivity after successful upload
                val intent = Intent(this@UploadActivity, AccountActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            progressBar.visibility = View.INVISIBLE
            Toast.makeText(this@UploadActivity, "Failed: $exception", Toast.LENGTH_SHORT).show()
        }.addOnProgressListener { snapshot ->
            val progress = (100.0 * snapshot.bytesTransferred / snapshot.totalByteCount)
            progressBar.progress = progress.toInt()
        }
    }
    private fun getFileExtension(fileUri: Uri): String? {
        val contentResolver: ContentResolver = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri))
    }
}
