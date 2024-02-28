package com.terracode.blueharvest

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import com.terracode.blueharvest.utils.ReadJSONObject
import com.terracode.blueharvest.utils.UnitConverter

class HomeActivity : AppCompatActivity() {

    // Declare variables as var to allow reassignment
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var optimalRakeHeightTextView: TextView
    private lateinit var optimalRakeRPMValueTextView: TextView
    private lateinit var currentBushHeightTextView: TextView
    private lateinit var currentSpeedTextView: TextView

    var bushHeightData: Double? = null
    var rakeHeightData: Double? = null
    var rpmData: Double? = null
    var speedData: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val toolbar: Toolbar = findViewById(R.id.homeToolbar)
        setSupportActionBar(toolbar)

        optimalRakeHeightTextView = findViewById(R.id.optimalRakeHeightValue)
        optimalRakeRPMValueTextView = findViewById(R.id.optimalRakeRPMValue)
        currentBushHeightTextView = findViewById(R.id.currentBushHeightValue)
        currentSpeedTextView = findViewById(R.id.currentSpeedValue)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val toggleValue = sharedPreferences.getBoolean("unitToggleValue", true)
        Log.i("Home activity","HomeActivity is running")


        //Replace with get data from microcontroller.
        val sensorData = ReadJSONObject.fromAsset(this, "SensorDataExample.json")
        sensorData?.apply {
            rpmData = getRPM()
            rakeHeightData = getRakeHeight()
            bushHeightData = getBushHeight()
            speedData = getSpeed()
        }

        optimalRakeHeightTextView.text = if (toggleValue) {
            "${rakeHeightData} cm"
        } else {
            "${UnitConverter.convertHeightToImperial(rakeHeightData)} in"
        }

        optimalRakeRPMValueTextView.text = if (toggleValue) {
            "${rpmData} mph"
        } else {
            //Needs function
            "${UnitConverter.convertSpeedToImperial(rpmData)} kmph"
        }

        currentBushHeightTextView.text = if (toggleValue) {
            "${bushHeightData} cm"
        } else {
            "${UnitConverter.convertHeightToImperial(bushHeightData)} in"
        }

        currentSpeedTextView.text = if (toggleValue) {
            "${speedData} cm"
        } else {
            //Needs function
            "${UnitConverter.convertSpeedToImperial(speedData)} in"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            //This needs to be changed to include a card for notifications
            R.id.notifications -> {
                val home = Intent(this, HomeActivity::class.java)
                startActivity(home)
                true
            }
            R.id.configurationSettings -> {
                val operationSettings = Intent(this, ConfigurationSettingsActivity::class.java)
                startActivity(operationSettings)
                true
            }
            R.id.accessibilitySettings -> {
                val accessibilitySettings = Intent(this, AccessibilitySettingsActivity::class.java)
                startActivity(accessibilitySettings)
                true
            }
            else -> false
        }
    }
}
