package com.farmbuy.buyer.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.farmbuy.R
import com.farmbuy.auth.LoginActivity
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_buyers.*

class BuyersActivity : AppCompatActivity() {

    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager2? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyers)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        bottom_nav.setupWithNavController(nav_host_fragment_container.findNavController())


//        tabLayout = findViewById(R.id.tabLayout)
//        viewPager = findViewById(R.id.viewpager)
//        viewPager!!.adapter = ViewpagerAdapter(
//            supportFragmentManager,
//            lifecycle
//        )
//
//        TabLayoutMediator(
//            tabLayout!!,
//            viewPager!!,
//            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
//                when (position) {
//                    0 -> tab.text = "Products"
//                    1 -> tab.text = "My Orders"
//                }
//            }).attach()
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
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