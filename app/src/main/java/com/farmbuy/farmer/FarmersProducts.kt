package com.farmbuy.farmer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.farmbuy.R
import com.farmbuy.datamodel.Products
import com.farmbuy.ui.LoginActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_create_order.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FarmersActivity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 100
    private var filePath: Uri? = null
    private lateinit var dbReference: DatabaseReference
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    lateinit var currentUser: String
    lateinit var downloadUri:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farmers_order)
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        dbReference = FirebaseDatabase.getInstance().getReference("Orders")
        currentUser = FirebaseAuth.getInstance().currentUser!!.uid

        downloadUri = ""

        image.setOnClickListener {

            openGallery()
        }

        create_order.setOnClickListener {

            val pname = productName.text.toString()
            val pdescription = pDescription.text.toString()
            val price = price.text.toString()
            val units = pUits.text.toString()
            val address = location.text.toString()
            val contact_no = contact.text.toString()
            val current_date  = SimpleDateFormat("EEE, d MMM yyyy ", Locale.getDefault()).format(Date())
            val farmers_id = FirebaseAuth.getInstance().currentUser?.uid
            val productId = UUID.randomUUID().toString()

        }
    }


    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                image.setImageBitmap(bitmap)
                uploadImage()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    private fun uploadImage() {
        if (filePath != null) {
            val ref = storageReference?.child("uploads/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)

            val urlTask =
                uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation ref.downloadUrl
                })?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //this is the url
                        downloadUri = task.result.toString()
//                        addUploadRecordToDb(downloadUri.toString())
                    } else {
                        // Handle failures
                    }
                }?.addOnFailureListener {

                }
        } else {
            Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }



    private fun createOrder(
        id: String,
        pName: String,
        description: String,
        price: String,
        units: String,
        imageUrl: String,
        location:String,
        date_uploaded:String,
        phone:String,
        farmers_id:String
    ) {

        if (imageUrl !== "")
        {
            val products = Products(id,pName,description,units,price,location,imageUrl,farmers_id,date_uploaded,phone)
            var reference: DatabaseReference = FirebaseDatabase.getInstance().reference
            val key = reference.child("Orders").push().key
            if (key != null) {
                reference.child("Orders").child(key).setValue(products)
            }
        }
        else{
            Toast.makeText(this,"SORRY IMAGE NOT UPLOADED",Toast.LENGTH_SHORT).show()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.farmersmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                val it = Intent(this, LoginActivity::class.java)
                startActivity(it)
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}