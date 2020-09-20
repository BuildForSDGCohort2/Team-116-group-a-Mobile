package com.farmbuy.auth

import android.content.Intent
import android.content.SharedPreferences
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {

    private var PRIVATE_MODE = 0
    private val PREF_NAME = "farm_buy"
    private var userRef = Firebase.firestore.collection("Users")
    private var mAuth: FirebaseAuth? = null
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    lateinit var mchoice: String
    lateinit var sharedPref:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        sharedPref= getSharedPreferences(PREF_NAME, PRIVATE_MODE)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

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
            register(mEmail, mPassword, mUseranme, mchoice)
        }
    }

    private fun verifyInputs() {
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

    private fun register(email: String, password: String, username: String, userType: String) {
        progressBar.visibility = View.VISIBLE
        mAuth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener(this) {

            if (it.isSuccessful)
            {
                val user = User(username, userType, email, "default")
                registerUserToDb(user)
            }
            else
            {
                progressBar.visibility = View.INVISIBLE
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun registerUserToDb(user: User) = CoroutineScope(Dispatchers.IO).launch {

        try {
//            userRef.document().add()
            userRef.add(user).await()
            withContext(Dispatchers.Main)
            {
                progressBar.visibility = View.INVISIBLE
                if (user.userTpe == "Farmer") {
                    val editor = sharedPref.edit()
                    editor.putString(PREF_NAME,"farmer")
                    editor.apply()
                    val intent = Intent(this@SignUpActivity, FarmersActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                } else {
                    val editor = sharedPref.edit()
                    editor.putString(PREF_NAME,"buyer")
                    editor.apply()
                    val intent = Intent(this@SignUpActivity, BuyersActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            }

        } catch (e: Exception) {
            Toast.makeText(this@SignUpActivity, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}