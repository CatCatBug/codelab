package cc.fastcv.bluetoothdemo.bt.client

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cc.fastcv.bluetoothdemo.APP
import cc.fastcv.bluetoothdemo.R
import cc.fastcv.bluetoothdemo.bt.BtBase
import cc.fastcv.bluetoothdemo.bt.BtClient
import java.io.File

@SuppressLint("MissingPermission")
class BTDeviceActivity : AppCompatActivity(), BtBase.Listener {

    companion object {
        fun startActivity(context: Context, address: String) {
            context.startActivity(Intent(context, BTDeviceActivity::class.java).apply {
                putExtra("address", address)
            })
        }
    }

    private lateinit var mTips: TextView
    private lateinit var mInputMsg: EditText
    private lateinit var mInputFile: EditText
    private lateinit var mLogs: TextView

    private val mClient = BtClient(this)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bt_device)

        mTips = findViewById(R.id.tv_show)

        val manager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

        val btConnect = findViewById<Button>(R.id.bt_connect)
        btConnect.setOnClickListener {
            intent?.getStringExtra("address")?.let {
                val remoteDevice = manager.adapter.getRemoteDevice(it)
                connect(remoteDevice)
            }
        }

        val btDisConnect = findViewById<Button>(R.id.bt_disconnect)
        btDisConnect.setOnClickListener {
            mClient.close()
        }


        mInputMsg = findViewById(R.id.input_msg)
        mInputFile = findViewById(R.id.input_file)

        mInputFile.setText(Environment.getExternalStorageDirectory().absolutePath +"/niee.apk")
        mLogs = findViewById(R.id.tv_log)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            startActivity(Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION))
        }
    }

    private fun connect(dev: BluetoothDevice) {
        if (mClient.isConnected(dev)) {
            APP.toast("已经连接了", 0)
            return
        }
        mClient.connect(dev)
        APP.toast("正在连接...", 0)
        mTips.text = "正在连接..."
    }

    fun sendMsg(v: View) {
        if (mClient.isConnected(null)) {
            val msg = mInputMsg.text.toString()
            if (TextUtils.isEmpty(msg)) {
                APP.toast("消息不能空", 0)
            } else {
                mClient.sendMsg(msg)
            }
        } else {
            APP.toast("没有连接", 0)
        }
    }

    fun sendFile(v: View) {
        if (mClient.isConnected(null)) {
            val filePath = mInputFile.text.toString()
            if (TextUtils.isEmpty(filePath) || !File(filePath).isFile) {
                APP.toast(
                    "文件无效",
                    0
                )
            } else {
                mClient.sendFile(filePath)
            }
        } else {
            APP.toast("没有连接", 0)
        }
    }

    override fun socketNotify(state: Int, obj: Any?) {
        if (isDestroyed) return
        var msg: String? = null
        when (state) {
            BtBase.Listener.CONNECTED -> {
                val dev = obj as BluetoothDevice
                msg = String.format("与%s(%s)连接成功", dev.name, dev.address)
                mTips.text = msg
            }
            BtBase.Listener.DISCONNECTED -> {
                msg = "连接断开"
                mTips.text = msg
            }
            BtBase.Listener.MSG -> {
                msg = String.format("\n%s", obj)
                mLogs.append(msg)
            }
        }
        APP.toast(msg, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        mClient.unListener()
        mClient.close()
    }

}