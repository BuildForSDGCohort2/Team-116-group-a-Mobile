package com.farmbuy.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cuneytayyildiz.onboarder.OnboarderActivity
import com.cuneytayyildiz.onboarder.model.*
import com.cuneytayyildiz.onboarder.utils.OnboarderPageChangeListener
import com.cuneytayyildiz.onboarder.utils.color
import com.farmbuy.R
import com.farmbuy.auth.AuthActivity
import com.mahmoud.onboardingview.OnBoardingScreen
import com.mahmoud.onboardingview.OnBoardingView

class Onboarder : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarder)

        val onBoardingView=findViewById<OnBoardingView>(R.id.onboardingView)
        var screens= arrayListOf<OnBoardingScreen>()
        screens.add(OnBoardingScreen(titleText = "Hunger",
            subTitleText = "introducing awesome new feature  1 ",
            screenBGColor = Color.RED,
            drawableResId = R.drawable.profile
        ))
        screens.add(OnBoardingScreen(
            titleText = "Title 2",
            subTitleText = "introducing awesome new feature  2 ",
            drawableResId = R.drawable.farmproducts,
            screenBGColor =  Color.parseColor("#FE6F0C")
        ))
        screens.add(OnBoardingScreen(
            titleText = "Title 3",
            subTitleText = "introducing awesome new feature  3 ",
            drawableResId = R.drawable.profile,
            screenBGColor =Color.MAGENTA
        ))
        screens.add(OnBoardingScreen(
            titleText = "Title 4",
            subTitleText = "introducing awesome new feature  4 ",
            screenBGColor = Color.RED,
            drawableResId = R.drawable.productimage
        ))
        onBoardingView.setScreens(screens)

        onBoardingView.onEnd {
          val intent = Intent(this,AuthActivity::class.java)
            startActivity(intent)
        }

        onBoardingView.onFinish {
            Toast.makeText(this,"OnBoarding last screen",Toast.LENGTH_SHORT).show()
            /* return false will not trigger this action again(on swipe back ) ,
            true will trigger it with every swipe to last screen ,
            may used for showing some animation or something */
            return@onFinish false

        }


    }


}