package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    //Setting view for start of activity (Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.BottomNav)

        //This will show how many messages a user has on read once we figure out that number:
        //@Dharani - Use this for notifications as start?
//        val badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.Chat)
//        badgeDrawable.isVisible = true
//        badgeDrawable.number = 10


        //Code for bottom navigation.
        val home = Intent(this, HomeActivity::class.java)
        startActivity(home)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Home -> {
                    val home = Intent(this, HomeActivity::class.java)
                    startActivity(home)
                    true
                }
                R.id.Swipe -> {
                    val swipe = Intent(this, SwipeActivity::class.java)
                    startActivity(swipe)
                    true
                }
                else -> false
            }
        }
    }

    override fun onStart() {
        super.onStart()
            startActivity(Intent(this, HomeActivity::class.java))
            finish() // Close the current activity
    }
}