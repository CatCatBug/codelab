package cc.fastcv.nativegnss

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.GnssStatus
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var permissionContracts: ActivityResultContract<Array<String>, Map<String, Boolean>>
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    private lateinit var mTvLatitude: TextView
    private lateinit var mTvLongitude: TextView
    private lateinit var mTvSatelliteCount: TextView
    private lateinit var mTvBdTotal: TextView
    private lateinit var mTvBdLocation: TextView

    private val callback = ActivityResultCallback<Map<String, Boolean>> {
        if (!it.values.contains(false)) {

            initLocationManager()
        } else {
            Toast.makeText(this, "请同意全部权限后重启app再试", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n", "MissingPermission")
    private fun initLocationManager() {
        val mLocationManager =
            this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "请开启GPS导航后重启app再试", Toast.LENGTH_SHORT).show()
            return
        }

        val locationListener = LocationListener { location ->
            // callback func when the location changed
            mTvLatitude.text = "纬度：" + location.latitude
            mTvLongitude.text = "经度：" + location.longitude
        }
        mLocationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000,
            0.0f,
            locationListener
        )


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val mGNSSCallback: GnssStatus.Callback = object : GnssStatus.Callback() {
                override fun onSatelliteStatusChanged(status: GnssStatus) {
                    super.onSatelliteStatusChanged(status)

                    val satelliteCount = status.satelliteCount
                    // get satellite count
                    mTvSatelliteCount.text = "共收到卫星信号：" + satelliteCount.toString() + "个"

                    var mBDSatelliteCount = 0
                    var mBDLocationCount = 0
                    if (satelliteCount > 0) {
                        for (i in 0 until satelliteCount) {
                            val type = status.getConstellationType(i)
                            if (GnssStatus.CONSTELLATION_BEIDOU == type) {
                                mBDSatelliteCount++

                                if (status.usedInFix(i)) {
                                    mBDLocationCount++
                                }
                            }
                        }
                    }

                    mTvBdTotal.text = "北斗卫星信号个数：$mBDSatelliteCount 个"
                    mTvBdLocation.text = "用于定位的北斗卫星个数：$mBDSatelliteCount 个"

                }
            }

            mLocationManager.registerGnssStatusCallback(mGNSSCallback, Handler(Looper.getMainLooper()))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTvLatitude = findViewById(R.id.mTvLatitude)
        mTvLongitude = findViewById(R.id.mTvLongitude)
        mTvSatelliteCount = findViewById(R.id.mTvSatelliteCount)
        mTvBdTotal = findViewById(R.id.mTvBdTotal)
        mTvBdLocation = findViewById(R.id.mTvBdLocation)

        permissionContracts = ActivityResultContracts.RequestMultiplePermissions()
        permissionLauncher = registerForActivityResult(permissionContracts, callback)

        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }
}