package com.terracode.blueharvest.listeners.toolbarListeners

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.View.OnClickListener
import com.terracode.blueharvest.HomeActivity

class BackButtonListener(private val activity: Activity) : OnClickListener {

    override fun onClick(view: View?) {
        val homeActivityIntent = Intent(activity, HomeActivity::class.java)
        activity.startActivity(homeActivityIntent)
    }
}
