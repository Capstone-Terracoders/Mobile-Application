package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

@Suppress("DEPRECATION")
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val toolbar: Toolbar = findViewById(R.id.HomeToolbar)
        setSupportActionBar(toolbar)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            //This needs to be changed to include a card for notifications
            R.id.Notifications -> {
                val home = Intent(this, HomeActivity::class.java)
                startActivity(home)
                true
            }
            R.id.Settings -> {
                val swipe = Intent(this, SettingsActivity::class.java)
                startActivity(swipe)
                true
            }
            else -> false
        }
    }
}
