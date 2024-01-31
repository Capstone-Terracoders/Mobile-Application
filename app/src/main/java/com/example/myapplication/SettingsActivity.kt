package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.BackButton -> {
                val home = Intent(this, HomeActivity::class.java)
                startActivity(home)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
