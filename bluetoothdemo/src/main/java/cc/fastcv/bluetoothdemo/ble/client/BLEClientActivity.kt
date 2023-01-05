package cc.fastcv.bluetoothdemo.ble.client

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import cc.fastcv.bluetoothdemo.R
import cc.fastcv.bluetoothdemo.ble.BleDev
import cc.fastcv.bluetoothdemo.lib.BleScanner

class BLEClientActivity : AppCompatActivity(), DeviceAdapter.OnItemClickListener,
    BleScanner.BTCallback {

    private val scanner = BleScanner(this)

    private val adapter: DeviceAdapter = DeviceAdapter()

    private val addressList = arrayListOf<String>()
    private val devices = arrayListOf<BleDev>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ble_client)

        val btScan = findViewById<Button>(R.id.bt_scan)
        btScan.setOnClickListener {
            addressList.clear()
            devices.clear()
            adapter.updateList(devices)
            scanner.startScan(this)
        }

        val rvDevices = findViewById<RecyclerView>(R.id.rv_devices)
        rvDevices.adapter = adapter
        adapter.setOnItemClickListener(this)
    }

    override fun onFoundDev(dev: BleDev) {
        if (!addressList.contains(dev.getAddress()) && dev.getName() != null) {
            devices.add(dev)
            addressList.add(dev.getAddress())
            adapter.updateList(devices)
        }
    }

    override fun onItemClick(dev: BleDev) {
        BLEDeviceActivity.startActivity(this,dev.getAddress())
    }

}