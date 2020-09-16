package com.farmbuy.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.farmbuy.R
import com.farmbuy.auth.AuthActivity
import com.farmbuy.buyer.ui.BuyersActivity
import com.farmbuy.farmer.FarmersActivity
import com.farmbuy.farmer.FarmersProducts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class OnboardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val fUser = FirebaseAuth.getInstance().currentUser
        if (fUser !== null) {
            val intent = Intent(this, FarmersActivity::class.java)
            startActivity(intent)
        } else {

            Handler().postDelayed({

                startActivity(Intent(this, AuthActivity::class.java))
            }, 2000)
        }
    }


}