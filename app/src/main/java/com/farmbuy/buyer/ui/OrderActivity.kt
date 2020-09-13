package com.farmbuy.buyer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.farmbuy.R
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.confirm_dialog.view.*

class OrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        order.setOnClickListener {


        }
    }


    fun showDialog(email: String, name: String, lastname: String, link: String) {
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

    fun successDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.success_layout, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        //show dialog
        val mAlertDialog = mBuilder.show()
    }


    fun errorDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.failure_layout, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        //show dialog
        val mAlertDialog = mBuilder.show()
    }
}
