package com.farmbuy.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.farmbuy.R
import com.farmbuy.farmer.FarmersActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private  var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance();
        loginbtn.setOnClickListener {
            verifyInputs()
            val mEmail = email.text?.trim().toString()
            val mPassword = password?.text?.trim().toString()
            login(mEmail,mPassword)
        }


    }

    private fun verifyInputs()
    {
        if(email.text.isNullOrEmpty())
        {
            email.error = "Email is Required"
            email.requestFocus()
        }

        if(password.text.isNullOrEmpty())
        {
            password.error = "Password is Required"
            password.requestFocus()
        }

    }


    private fun login(email:String, password:String)
    {

        mAuth?.signInWithEmailAndPassword(email,password)?.addOnCompleteListener(this){
            if (it.isSuccessful)
            {
                val id = mAuth!!.currentUser?.uid


                val intent = Intent(this, FarmersActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }

            else{
                Toast.makeText(this@LoginActivity, "Authentication failed PLease check your Email or Password",
                    Toast.LENGTH_LONG).show() }

        }

    }
}