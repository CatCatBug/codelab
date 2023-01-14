package cc.fastcv.positioningdemo.baidu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cc.fastcv.positioningdemo.R
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.MapView
import com.baidu.mapapi.map.MyLocationConfiguration
import com.baidu.mapapi.map.MyLocationData


class BaiduMapActivity : AppCompatActivity(), BaiduLocationListener.MyLocationDataCallback {

    private var mLocationClient: LocationClient? = null
    private val listener: BaiduLocationListener = BaiduLocationListener(this)

    private var mMapView: MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_baidu)

        //获取地图控件引用
        mMapView = findViewById(R.id.bmapView)

        mMapView!!.map.isMyLocationEnabled = true
        mMapView!!.map.setMyLocationConfiguration(
            MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING,
                true,
                null,
                0,
                0
            )
        )


        try {
            mLocationClient = LocationClient(applicationContext)
            mLocationClient!!.registerLocationListener(listener)

            //mLocationClient为第二步初始化过的LocationClient对象
            //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
            //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
            mLocationClient!!.locOption = buildOption()
            //mLocationClient为第二步初始化过的LocationClient对象
            //调用LocationClient的start()方法，便可发起定位请求
            mLocationClient!!.start()
            // 开启室内定位模式（重复调用也没问题），开启后，定位SDK会融合各种定位信息（Gnss,WI-FI，蓝牙，传感器等）连续平滑的输出定位结果；
            mLocationClient!!.startIndoorMode()
        } catch (e: Exception) {
        }
    }

    private fun buildOption(): LocationClientOption {
        val option = LocationClientOption()

        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；
        //LocationMode.Fuzzy_Locating, 模糊定位模式；v9.2.8版本开始支持，可以降低API的调用频率，但同时也会降低定位精度；
        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy

        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标
        option.setCoorType("bd09ll")


        //可选，首次定位时可以选择定位的返回是准确性优先还是速度优先，默认为速度优先
        //可以搭配setOnceLocation(Boolean isOnceLocation)单次定位接口使用，当设置为单次定位时，setFirstLocType接口中设置的类型即为单次定位使用的类型
        //FirstLocType.SPEED_IN_FIRST_LOC:速度优先，首次定位时会降低定位准确性，提升定位速度；
        //FirstLocType.ACCUARACY_IN_FIRST_LOC:准确性优先，首次定位时会降低速度，提升定位准确性；
        option.setFirstLocType(LocationClientOption.FirstLocType.SPEED_IN_FIRST_LOC)

        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效
        option.setScanSpan(2 * 60 * 1000)

        //可选，设置是否使用卫星定位，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true
        option.isOpenGnss = true

        //可选，设置是否当卫星定位有效时按照1S/1次频率输出卫星定位结果，默认false
        option.isLocationNotify = true

        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        option.setIgnoreKillProcess(false)

        //可选，设置是否收集Crash信息，默认收集，即参数为false
        option.SetIgnoreCacheException(false)

        //可选，V7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位
        option.setWifiCacheTimeOut(20 * 1000)

        //可选，设置是否需要过滤卫星定位仿真结果，默认需要，即参数为false
        option.setEnableSimulateGnss(false)

        //可选，设置是否需要最新版本的地址信息。默认需要，即参数为true
        option.setNeedNewVersionRgc(true)

        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true
        option.setIsNeedAddress(true)

        //可选，设置是否需要最新版本的地址信息。默认需要，即参数为true
        option.setNeedNewVersionRgc(true)

        //可选，是否需要位置描述信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的位置信息，此处必须为true
        option.setIsNeedLocationDescribe(true)

        //可选，是否需要周边POI信息，默认为不需要，即参数为false
        //如果开发者需要获得周边POI信息，此处必须为true
        option.setIsNeedLocationPoiList(true)

        return option
    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView?.onDestroy()
        mLocationClient?.stopIndoorMode()
        mLocationClient?.unRegisterLocationListener(listener)
    }

    override fun onLocationDataCallback(data: MyLocationData) {
        mMapView?.map?.setMyLocationData(data)
    }

}