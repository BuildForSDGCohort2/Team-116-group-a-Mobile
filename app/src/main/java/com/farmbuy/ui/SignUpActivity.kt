package com.farmbuy.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.farmbuy.R
import com.farmbuy.buyer.ui.BuyersActivity
import com.farmbuy.datamodel.User
import com.farmbuy.farmer.FarmersActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("Users")
    lateinit var mchoice: String
    private var isFarmer = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()


        val choice = resources.getStringArray(R.array.choice)

        if (spinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, choice)
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    mchoice = parent.getItemAtPosition(position).toString()

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Another interface callback
                }
            }
        }

        signup.setOnClickListener {

            verifyInputs()

            val mUseranme = username.text?.trim().toString()
            val mEmail = email.text?.trim().toString()
            val mPassword = password?.text?.trim().toString()
            register(mEmail,mPassword,mUseranme,mchoice)
        }
    }

    fun verifyInputs() {
        if (username.text.isNullOrEmpty()) {
            username.error = "Username is Required"
            username.requestFocus()

        }
        if (email.text.isNullOrEmpty()) {
            email.error = "Email is Required"
            email.requestFocus()
        }

        if (password.text.isNullOrEmpty()) {
            password.error = "Password is Required"
            password.requestFocus()
        }
    }

    private fun register( email: String, password: String, username: String, choice: String) {

        if (choice == "farmer")
        {
            isFarmer = true
        }
        mAuth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener(this) {

            if (it.isSuccessful) {
                val userId = mAuth?.currentUser?.uid
                val user = userId?.let { it1 ->
                    User(
                     username,isFarmer,email,userId,"default"
                    )
                }
                if (userId != null) {
                    myRef.child(userId).setValue(user)
                }

                if (isFarmer)
                {
                    val intent = Intent(this, FarmersActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
                else{
                    val intent = Intent(this, BuyersActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }

            } else {
                Toast.makeText(
                    this,
                    "Cant register User Please Check your email or Password.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}