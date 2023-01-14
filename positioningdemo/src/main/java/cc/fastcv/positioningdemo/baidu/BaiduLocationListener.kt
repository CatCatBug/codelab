package cc.fastcv.positioningdemo.baidu

import android.util.Log
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.mapapi.map.MyLocationData


class BaiduLocationListener(private val callback: MyLocationDataCallback) :
    BDAbstractLocationListener() {
    companion object {
        private const val TAG = "BaiduLocationListener"
    }

    override fun onReceiveLocation(location: BDLocation?) {
        //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
        //以下只列举部分获取经纬度相关（常用）的结果信息
        //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

        location?.let {
            //获取纬度信息
            val latitude: Double = it.latitude
            //获取经度信息
            val longitude: Double = it.longitude
            //获取定位精度，默认值为0.0f
            val radius: Float = it.radius
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            val coorType: String = it.coorType
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            val errorCode: Int = it.locType


            val locData = MyLocationData.Builder()
                .accuracy(it.radius) // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(it.direction)
                .latitude(it.latitude)
                .longitude(it.longitude)
                .build()
            callback.onLocationDataCallback(locData)

            Log.d(
                TAG,
                "onReceiveLocation: latitude = $latitude longitude = $longitude radius = $radius coorType = $coorType errorCode = $errorCode"
            )


            val addr = location.addrStr //获取详细地址信息

            val country = location.country //获取国家

            val province = location.province //获取省份

            val city = location.city //获取城市

            val district = location.district //获取区县

            val street = location.street //获取街道信息

            val adcode = location.adCode //获取adcode

            val town = location.town //获取乡镇信息

            Log.d(
                TAG,
                "详细地址信息:$addr" +
                        "\n国家:$country" +
                        "\n省份:$province" +
                        "\n城市:$city" +
                        "\n区县:$district" +
                        "\n街道信息:$street" +
                        "\nadcode:$adcode" +
                        "\n乡镇信息:$town"
            )

            val locationDescribe = location.locationDescribe //获取位置描述信息
            Log.d(TAG, "位置描述信息: $locationDescribe")


            Log.d(TAG, "获取POI信息")
            val poiList = location.poiList
            for (poi in poiList) {
                val poiName: String = poi.name //获取POI名称
                val poiTags: String = poi.tags //获取POI类型
                val poiAddress: String = poi.addr //获取POI地址 //获取周边POI信息
                val poiRegion = location.poiRegion
                val poiRegionDerectionDesc = poiRegion.derectionDesc //获取PoiRegion位置关系
                val poiRegionName = poiRegion.name //获取PoiRegion名称
                val poiRegionTags = poiRegion.tags //获取PoiRegion类型
                Log.d(
                    TAG, "poiName = $poiName  poiTags = $poiTags  poiAddress = $poiAddress" +
                            "\npoiRegionName = $poiRegionName  poiRegionTags = $poiRegionTags  poiRegionDerectionDesc = $poiRegionDerectionDesc"
                )
            }

            Log.d(TAG, "获取室内信息")
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            if (location.floor != null) {
                // 当前支持高精度室内定位
                val buildingID = location.buildingID // 百度内部建筑物ID
                val buildingName = location.buildingName // 百度内部建筑物缩写
                val floor = location.floor // 室内定位的楼层信息，如 f1,f2,b1,b2
                Log.d(TAG, "内部建筑物ID:$buildingID  内部建筑物缩写:$buildingName  楼层信息:$floor")
            }
        }

    }

    interface MyLocationDataCallback {
        fun onLocationDataCallback(data: MyLocationData)
    }

}