package com.example.team_15_ctis_487

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        val appName = findViewById<View>(R.id.appName)
        val appMotto = findViewById<View>(R.id.appMotto)

        //Splash Screen Animations
        ObjectAnimator.ofFloat(appName, "alpha", 0f, 1f).apply {
            duration = 2000
            start()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)

                    // After app name fades in, fade in the motto
                    appMotto.visibility = View.VISIBLE

                    ObjectAnimator.ofFloat(appMotto, "alpha", 0f, 1f).apply {
                        duration = 2500
                        start()
                        addListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                super.onAnimationEnd(animation)

                                // Navigate to the main content after animations
                                setContentView(R.layout.activity_main)
                                // Initialize Navigation Components AFTER Layout is Set
                                setupNavigation()
                            }
                        })
                    }
                }
                private fun setupNavigation() {
                    // Safely initialize NavHostFragment and BottomNavigationView
                    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
                        ?: throw IllegalStateException("NavHostFragment not found")
                    val navController = navHostFragment.navController

                    val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
                    NavigationUI.setupWithNavController(bottomNavigationView, navController)
                    val secondaryIcon = findViewById<ImageView>(R.id.searchIcon)

                    bottomNavigationView.itemActiveIndicatorColor = ColorStateList.valueOf(Color.Black.toArgb())
                    bottomNavigationView.setOnNavigationItemReselectedListener { item ->
                        when(item.itemId) {
                            R.id.home -> {
                                // Respond to navigation item 1 reselection
                            }
                            R.id.trackWorkout -> {
                                // Respond to navigation item 2 reselection
                            }
                            R.id.nutrition -> {

                            }
                            R.id.goals -> {
                            }
                        }
                    }

                    navController.addOnDestinationChangedListener{_, destination, _ ->
                        when (destination.id) {
                            R.id.home -> {
                                // Respond to navigation item 1 reselection
                                secondaryIcon.visibility = View.GONE
                            }
                            R.id.trackWorkout -> {
                                // Respond to navigation item 2 reselection
                                secondaryIcon.visibility = View.VISIBLE

                            }
                            R.id.nutrition -> {
                                secondaryIcon.visibility = View.VISIBLE

                            }
                            R.id.goals -> {
                                secondaryIcon.visibility = View.GONE
                            }
                        }
                    }
                }
            })
        }
    }
}
