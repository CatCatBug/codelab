package cc.fastcv.bluetoothdemo.bt.client

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import cc.fastcv.bluetoothdemo.R
import cc.fastcv.bluetoothdemo.ble.client.adapter.DeviceAdapter
import cc.fastcv.bluetoothdemo.lib.BTScanner
import cc.fastcv.bluetoothdemo.lib.BleDeviceProxy
import cc.fastcv.bluetoothdemo.lib.BtReceiver

class BTClientActivity : AppCompatActivity(), BtReceiver.BTCallback,
    DeviceAdapter.OnItemClickListener {

    private val scanner = BTScanner(this)

    private val adapter:DeviceAdapter = DeviceAdapter()

    private val addressList = arrayListOf<String>()
    private val devices = arrayListOf<BleDeviceProxy>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bt_client)

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

    override fun onFoundDev(deviceProxy: BleDeviceProxy) {
        if (!addressList.contains(deviceProxy.getAddress()) && deviceProxy.getName() != null) {
            devices.add(deviceProxy)
            addressList.add(deviceProxy.getAddress())
            adapter.updateList(devices)
        }
    }

    override fun onItemClick(dev: BleDeviceProxy) {
        BTDeviceActivity.startActivity(this,dev.getAddress())
    }
}