package cc.fastcv.bluetoothdemo.bt.server

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cc.fastcv.bluetoothdemo.APP
import cc.fastcv.bluetoothdemo.R
import cc.fastcv.bluetoothdemo.bt.BtBase
import cc.fastcv.bluetoothdemo.bt.BtServer
import cc.fastcv.bluetoothdemo.lib.BluetoothTools
import java.io.File

@SuppressLint("MissingPermission")
class BTServerActivity : AppCompatActivity(), BtBase.Listener {

    private var mTips: TextView? = null
    private var mInputMsg: EditText? = null
    private var mInputFile: EditText? = null
    private var mLogs: TextView? = null
    private var mServer: BtServer? = null


    private var close = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bt_server)

        mTips = findViewById(R.id.tv_tips)

        findViewById<Button>(R.id.bt_start).setOnClickListener {
            mServer!!.listen()
        }

        findViewById<Button>(R.id.bt_stop).setOnClickListener {
            close = true
            mServer!!.close()
        }

        mInputMsg = findViewById(R.id.input_msg)
        mInputFile = findViewById(R.id.input_file)

        mInputFile!!.setText(Environment.getExternalStorageDirectory().absolutePath +"/niee.apk")
        mLogs = findViewById(R.id.tv_log)
        mServer = BtServer(this)

        BluetoothTools.discoverable(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mServer!!.unListener()
        mServer!!.close()
    }

    fun sendMsg(view: View?) {
        if (mServer!!.isConnected(null)) {
            val msg = mInputMsg!!.text.toString()
            if (TextUtils.isEmpty(msg)) {
                APP.toast("消息不能空", 0)
            } else {
                mServer!!.sendMsg(msg)
            }
        } else {
            APP.toast("没有连接", 0)
        }
    }

    fun sendFile(view: View?) {
        if (mServer!!.isConnected(null)) {
            val filePath = mInputFile!!.text.toString()
            if (TextUtils.isEmpty(filePath) || !File(filePath).isFile) {
                APP.toast("文件无效", 0)
            } else {
                mServer!!.sendFile(filePath)
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
                mTips!!.text = msg
            }
            BtBase.Listener.DISCONNECTED -> {
                msg =  if (!close) {
                    mServer!!.listen()
                    "连接断开,正在重新监听..."
                } else {
                    close = false
                    "服务未启动..."
                }
                mTips!!.text = msg
            }
            BtBase.Listener.MSG -> {
                msg = String.format("\n%s", obj)
                mLogs!!.append(msg)
            }
        }
        APP.toast(msg, 0)
    }

}