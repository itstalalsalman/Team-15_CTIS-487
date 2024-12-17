package com.example.team_15_ctis_487

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        val appName = findViewById<View>(R.id.appName)
        val appMotto = findViewById<View>(R.id.appMotto)

        ObjectAnimator.ofFloat(appName, "alpha", 0f, 1f).apply {
            duration = 2000
            start()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)

                    // After app name fades in, fade in the motto
                    appName.visibility = View.INVISIBLE // Optional, remove if you want app name to remain
                    appMotto.visibility = View.VISIBLE

                    ObjectAnimator.ofFloat(appMotto, "alpha", 0f, 1f).apply {
                        duration = 2500
                        start()
                        addListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                super.onAnimationEnd(animation)

                                // Navigate to the main content after animations
                                setContentView(R.layout.activity_main)
                            }
                        })
                    }
                }
            })
        }


    }
}
