package com.terracode.blueharvest

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

@Suppress("DEPRECATION")
class AccessibilitySettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accessibility_settings)

        val toolbar: Toolbar = findViewById(R.id.SettingsToolbar)
        setSupportActionBar(toolbar)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            //This needs to be changed to include a card for notifications
            R.id.BackButton -> {
                val home = Intent(this, HomeActivity::class.java)
                startActivity(home)
                true
            }
            R.id.Operation_Settings -> {
                val operationSettings = Intent(this, OperationSettingsActivity::class.java)
                startActivity(operationSettings)
                true
            }
            R.id.Accessibility_Settings -> {
                val accessibilitySettings = Intent(this, AccessibilitySettingsActivity::class.java)
                startActivity(accessibilitySettings)
                true
            }
            else -> false
        }
    }
}
