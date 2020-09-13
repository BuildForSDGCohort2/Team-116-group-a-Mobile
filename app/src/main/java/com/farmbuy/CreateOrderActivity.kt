package com.farmbuy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_create_order.*

class CreateOrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_order)



        progressBar.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE
    }
}