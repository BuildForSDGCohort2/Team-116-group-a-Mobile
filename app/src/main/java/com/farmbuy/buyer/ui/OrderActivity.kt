package com.farmbuy.buyer.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navArgs
import com.facebook.drawee.view.SimpleDraweeView
import com.farmbuy.R
import com.farmbuy.datamodel.Products
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_create_order.*
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.activity_order.etLocation
import kotlinx.android.synthetic.main.activity_order.etPrice
import kotlinx.android.synthetic.main.activity_order.etProductName
import kotlinx.android.synthetic.main.activity_order.image
import kotlinx.android.synthetic.main.confirm_dialog.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class OrderActivity : AppCompatActivity() {
    private var dbRef = Firebase.firestore.collection("Orders")
    val args: OrderActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        val products = args.products

        val uri: Uri = Uri.parse(products.imageUrl)
        val draweeView = image as SimpleDraweeView
        draweeView.setImageURI(uri)

        etProductName.text = products.productName
        etLocation.text = products.farmersLoc
        etPrice.text = products.price
        units.text = products.units
        description.text = products.description
        phone.text = products.phone
        date.text = products.dateUploaded


        order.setOnClickListener {

            sendOrderToDb(products)
//            showDialog()

        }


//        phone.setOnClickListener {
//
//            val number = "09033128501"
//
//            //Dialer intent
//            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(number)))
//            startActivity(intent)
//        }
    }


    private fun sendOrderToDb(products: Products) = CoroutineScope(Dispatchers.IO).launch {

        try {

            dbRef.add(products).await()
            withContext(Dispatchers.Main) {
                progressBar.visibility = View.INVISIBLE
                Toast.makeText(this@OrderActivity, "SUCCESS", Toast.LENGTH_SHORT).show()

                findNavController().navigate(R.id.farmersProductsFragment)
            }
        } catch (e: Exception) {
            Toast.makeText(this@OrderActivity, e.message, Toast.LENGTH_SHORT).show()
        }

    }


   private fun showDialog() {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.confirm_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        //show dialog
        val mAlertDialog = mBuilder.show()
        //login button click of custom layout
        mDialogView.confirm.setOnClickListener {
            //When Successfull
            mAlertDialog.dismiss()
            successDialog()

            mAlertDialog.dismiss()
            // When  error occurs
            errorDialog()

        }
        mDialogView.cancel.setOnClickListener {
            mAlertDialog?.dismiss()
        }

    }

   private fun successDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.success_layout, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        //show dialog
        val mAlertDialog = mBuilder.show()
    }


   private fun errorDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.failure_layout, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        //show dialog
        val mAlertDialog = mBuilder.show()
    }
}
