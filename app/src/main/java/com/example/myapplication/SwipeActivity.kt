package com.example.myapplication

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

@Suppress("DEPRECATION")
class SwipeActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe)

        bottomNavigationView = findViewById(R.id.BottomNav)
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

    // To Do Info button isn't working
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.Info -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Hello World")
                builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                builder.create().show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
