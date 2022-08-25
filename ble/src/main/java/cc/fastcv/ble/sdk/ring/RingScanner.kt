package cc.fastcv.ble.sdk.ring

import cc.fastcv.ble.sdk.log.Logger
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanFilter
import no.nordicsemi.android.support.v18.scanner.ScanSettings
import java.util.ArrayList

class RingScanner(private val call: ScanCallback) {

    private var settings: ScanSettings = ScanSettings.Builder()
        .setLegacy(false)
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .setReportDelay(1000)
        .setUseHardwareBatchingIfSupported(false) //默认为true，表示如果他们支持硬件分流批处理的话，使用硬件分流批处理;false表示兼容机制
        .build()

    private val filters: MutableList<ScanFilter> = ArrayList()

    private var isScanning = false

    private val mScanner = BluetoothLeScannerCompat.getScanner()

    /**
     * 开始扫描
     */
    fun startScan() {
        if (isScanning) return
        Logger.log("开始扫描-----")
        //后台扫描跟前台扫描的方式不一样
        mScanner.startScan(filters, settings, call)
        isScanning = true
    }

    /**
     * 停止扫描
     */
    fun stopScan() {
        Logger.log("停止扫描-----")
        mScanner.stopScan(call) //停止扫描
        isScanning = false
    }
}