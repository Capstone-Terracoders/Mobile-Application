import android.app.Service
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.terracode.blueharvest.BluetoothBle.BleDevice
import com.terracode.blueharvest.BluetoothBle.BleDeviceAdapter
import com.terracode.blueharvest.BluetoothBle.BleScanCallback
import com.terracode.blueharvest.BluetoothBle.BleScanManager
import com.terracode.blueharvest.BluetoothBle.BleScanRequiredPermissions
import com.terracode.blueharvest.BluetoothBle.BluetoothBLEActivity
import com.terracode.blueharvest.BluetoothBle.PermissionsUtilities
import com.terracode.blueharvest.BluetoothBle.PermissionsUtilities.checkPermissionsGranted
import com.terracode.blueharvest.BluetoothBle.PermissionsUtilities.dispatchOnRequestPermissionsResult
//import com.terracode.blueharvest.BluetoothBle.PermissionsUtilities.onRequestPermissionsResult
import com.terracode.blueharvest.R


class serviceBLE : Service() {

    //var isBound = isServiceBound()
    private lateinit var bleScanManager: BleScanManager
    private lateinit var foundDevices: MutableList<BleDevice>

    override fun onCreate() {
        super.onCreate()
        initBleScanManager()

    }

    override fun onBind(intent: Intent?): IBinder {
        initBleScanManager() // Ensure initialization when bound
        return  LocalBinder()
    }
    inner class LocalBinder : Binder() {
        val service: serviceBLE
            get() = this@serviceBLE
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    fun initBleScanManager()
    {
        val btManager = getSystemService(BluetoothManager::class.java)
        bleScanManager = BleScanManager(btManager, 5000, scanCallback = BleScanCallback({
            val name = it?.device?.address
            if (name.isNullOrBlank()) return@BleScanCallback
            val adapter = BleDeviceAdapter(foundDevices)
            val device = BleDevice(name)
            if (!foundDevices.contains(device)) {
                foundDevices.add(device)
                adapter.notifyItemInserted(foundDevices.size - 1)
            }
        }))
    }
//for state management, update and retrive value of isboun
/*you can access the binding state from outside the service using serviceBLE.isServiceBound()
but cannot directly modify it from outside. To update the state, call serviceBLE.setBound(true)
within your service when necessary. */
companion object {
    private var isBound: Boolean = false

    fun isServiceBound(): Boolean {
        return isBound
    }

    fun setBound(bound: Boolean) {
        isBound = bound
    }
}
}
    // ... (other service methods)

