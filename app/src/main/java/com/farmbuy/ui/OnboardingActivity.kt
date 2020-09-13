package com.farmbuy.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.farmbuy.R
import com.farmbuy.buyer.ui.BuyersActivity
import com.farmbuy.datamodel.User
import com.farmbuy.farmer.FarmersActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class OnboardingActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val fUser = FirebaseAuth.getInstance().currentUser
        if (fUser != null) {
            val id = fUser.uid
            database = FirebaseDatabase.getInstance().getReference("Users").child(id!!)
            database.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(
                        this@OnboardingActivity,
                        "Sorry An error occured",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue(User::class.java)
                    if (data != null) {
                        if (data.isFarmer) {
                            val it = Intent(this@OnboardingActivity, FarmersActivity::class.java)
                            startActivity(it)
                            finish()
                        } else {
                            val it = Intent(this@OnboardingActivity, BuyersActivity::class.java)
                            startActivity(it)
                            finish()
                        }


                    }
                }

            })

        }

        Handler().postDelayed({

            startActivity(Intent(this, AuthActivity::class.java))
        }, 2000)
    }


}