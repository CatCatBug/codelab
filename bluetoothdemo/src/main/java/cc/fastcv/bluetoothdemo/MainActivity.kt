package cc.fastcv.bluetoothdemo

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import cc.fastcv.bluetoothdemo.ble.BLEMainActivity
import cc.fastcv.bluetoothdemo.bt.BTMainActivity
import cc.fastcv.bluetoothdemo.lib.BluetoothTools

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvInfo = findViewById<TextView>(R.id.tv_show)
        tvInfo.text = getBlueToothInfo()


        val btBT = findViewById<Button>(R.id.bt_bluetooth)
        if (!BluetoothTools.isSupportBT(this)) {
            btBT.visibility = View.GONE
        }
        btBT.setOnClickListener {
            startActivity(Intent(this,BTMainActivity::class.java))
        }

        val btBLE = findViewById<Button>(R.id.bt_ble)
        if (!BluetoothTools.isSupportBLE(this)) {
            btBLE.visibility = View.GONE
        }
        btBLE.setOnClickListener {
            startActivity(Intent(this,BLEMainActivity::class.java))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(BluetoothTools.getBluetoothPermissionArray(),200)
        }
    }

    private fun getBlueToothInfo(): String {
        return "当前设备\n" +
                "是否支持经典蓝牙：${BluetoothTools.isSupportBT(this)}\n" +
                "是否支持低功耗蓝牙：${BluetoothTools.isSupportBLE(this)}\n" +
                "本机蓝牙名称：${BluetoothTools.getDeviceName(this)}\n" +
                "本机蓝牙地址：${BluetoothTools.getDeviceAddress(this)}\n"
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 200) {
            for (grantResult in grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("请开启相关权限后再试！！")
                        .setCancelable(false)
                        .setNegativeButton("确定"
                        ) { _, _ -> finish() }
                        .create().show()
                }
            }
        }
    }
}