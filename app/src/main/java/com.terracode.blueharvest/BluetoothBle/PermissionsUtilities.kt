package com.terracode.blueharvest.BluetoothBle

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat


object PermissionsUtilities {
    private var TAG = this::class.java.simpleName
    //private val DEBUG = BuildConfig.DEBUG

    /**
     * Checks for a set of permissions. If not granted, the user is asked to grant them.
     *
     * @param activity The activity that is requesting the permissions
     * @param permissions The permissions to be checked
     * @param requestCode The request code to be used when requesting the permissions
     */
    fun checkPermissions(
        activity: Activity, permissions: Array<out String>, requestCode: Int
    ) {
        val permissionsNotGranted = permissions.filter { permission ->
            ActivityCompat.checkSelfPermission(
                activity.baseContext, permission
            ) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsNotGranted.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionsNotGranted, requestCode)
        }
    }

    /**
     * Checks whether a set of permissions is granted or not
     *
     * @param context The context to be used for checking the permissions
     * @param permissions The permissions to be checked
     *
     * @return true if all permissions are granted, false otherwise
     */
    fun checkPermissionsGranted(context: Context, permissions: Array<out String>): Boolean {
        for (permission in permissions) {
            if (!checkPermissionGranted(context, permission)) return false
        }
        return true
    }

    /**
     * Checks the result of a permission request, and dispatches the appropriate action
     *
     * @param requestCode The request code of the permission request
     * @param grantResults The results of the permission request
     * @param onGrantedMap maps the request code to the action to be performed if the permissions are granted
     * @param onDeniedMap maps the request code to the action to be performed if the permissions are not granted
     */

    fun dispatchOnRequestPermissionsResult(
        requestCode: Int, grantResults: IntArray,
        onGrantedMap: Map<Int, () -> Unit>, onDeniedMap: Map<Int, () -> Unit>
    ) {
        when (checkGrantResults(grantResults)) {
            true -> onGrantedMap[requestCode]?.invoke()
            false -> onDeniedMap[requestCode]?.invoke()
        }
    }

    /**
     * Checks whether a permission is granted in the context
     *
     * @param context The context to be used for checking the permission
     * @param permission The permission to be checked
     *
     * @return true if the permission is granted, false otherwise
     */
    private fun checkPermissionGranted(context: Context, permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(
            context, permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Checks the results of a permission request
     *
     * @param grantResults The results of the permission request
     *
     * @return true if all permissions were granted, false otherwise
     */
    private fun checkGrantResults(grantResults: IntArray): Boolean {
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) return false
        }
        return true
    }
}