package com.farmbuy.auth

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.farmbuy.R
import com.farmbuy.buyer.ui.BuyersActivity
import com.farmbuy.datamodel.User
import com.farmbuy.farmer.FarmersActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "farm_buy"
    lateinit var sharedPref: SharedPreferences
    private var userRef = Firebase.firestore.collection("Users")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sharedPref = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        mAuth = FirebaseAuth.getInstance();
        loginbtn.setOnClickListener {
            verifyInputs()
            val mEmail = email.text?.trim().toString()
            val mPassword = phone?.text?.trim().toString()

            login(mEmail, mPassword)

        }


    }

    private fun verifyInputs() {
        if (email.text.isNullOrEmpty()) {
            email.error = "Email is Required"
            email.requestFocus()
        }

        if (phone.text.isNullOrEmpty()) {
            phone.error = "Password is Required"
            phone.requestFocus()
        }

    }


    private fun login(email: String, password: String) {
        progressBar.visibility = View.VISIBLE
        mAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener(this) {
            if (it.isSuccessful) {
                checkUserType()
                progressBar.visibility = View.INVISIBLE

            } else {
                Toast.makeText(
                    this@LoginActivity, "Authentication failed PLease check your Email or Password",
                    Toast.LENGTH_LONG
                ).show()
                progressBar.visibility = View.INVISIBLE
            }

        }

    }

    private fun checkUserType() {
        val id = FirebaseAuth.getInstance().currentUser?.uid
        userRef.whereEqualTo("id", id)
            .addSnapshotListener { value: QuerySnapshot?, error: FirebaseFirestoreException? ->
                error?.let {
                    Toast.makeText(this, "Sorry can't get User at this time", Toast.LENGTH_SHORT)
                        .show()
                    return@addSnapshotListener
                }

                value?.let {
                    for (documents in value.documents) {
                        val user = documents.toObject<User>()
                        if (user != null) {
                            if (user.userTpe == "Farmer") {
                                val editor = sharedPref.edit()
                                editor.putString(PREF_NAME, "farmer")
                                editor.apply()
                                val intent = Intent(this, FarmersActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                startActivity(intent)
                            } else if (user.userTpe == "Buyer") {
                                val editor = sharedPref.edit()
                                editor.putString(PREF_NAME, "buyer")
                                editor.apply()
                                val intent = Intent(this, BuyersActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                startActivity(intent)
                            } else {
                                TODO()
                            }

                        } else {
                            Toast.makeText(this, "An error Occured", Toast.LENGTH_SHORT).show()
                        }

                    }
                }


            }
    }
}