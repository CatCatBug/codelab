package com.example.lib

import org.json.JSONException
import org.json.JSONObject
import kotlin.math.*

/**
 * 坐标系转换方法类
 */
object CoordinateConversionUtils {

    private const val EARTH_RADIUS = 6378137f // 地球半径，单位：米

    private const val X_PI = Math.PI * 3000.0 / 180.0

    private const val A = 6378245.0

    private const val EE = 0.00669342162296594323

    /**
     * 将84坐标系（WGS84）转换为02坐标系（GCJ02）
     *
     * @param lng 经度
     * @param lat 纬度
     * @return 转换后的经纬度
     */
    @Throws(JSONException::class)
    fun wgs84ToGcj02(lng: Double, lat: Double): JSONObject {
        val result = JSONObject()
        var dLat: Double = transformLat(lng - 105.0, lat - 35.0)
        var dLng: Double = transformLng(lng - 105.0, lat - 35.0)
        val radLat = lat / 180.0 * Math.PI
        var magic = sin(radLat)
        magic = 1 - EE * magic * magic
        val sqrtMagic = sqrt(magic)
        dLat = dLat * 180.0 / (EARTH_RADIUS * (1 - EE) / (magic * sqrtMagic) * Math.PI)
        dLng = dLng * 180.0 / (EARTH_RADIUS / sqrtMagic * cos(radLat) * Math.PI)
        val mgLat = lat + dLat
        val mgLng = lng + dLng
        result.put("lng", mgLng)
        result.put("lat", mgLat)
        return result
    }

    /**
     * 将02坐标系（GCJ02）转换为84坐标系（WGS84）
     *
     * @param lng 经度
     * @param lat 纬度
     * @return 转换后的经纬度
     */
    @Throws(JSONException::class)
    fun gcj02ToWgs84(lng: Double, lat: Double): JSONObject {
        val result = JSONObject()
        var dLat = transformLat(lng - 105.0, lat - 35.0)
        var dLng = transformLng(lng - 105.0, lat - 35.0)
        val radLat = lat / 180.0 * Math.PI
        var magic = sin(radLat)
        magic = 1 - EE * magic * magic
        val sqrtMagic = sqrt(magic)
        dLat = dLat * 180.0 / (EARTH_RADIUS * (1 - EE) / (magic * sqrtMagic) * Math.PI)
        dLng = dLng * 180.0 / (EARTH_RADIUS / sqrtMagic * cos(radLat) * Math.PI)
        val mgLat = lat + dLat
        val mgLng = lng + dLng
        result.put("lng", lng * 2 - mgLng)
        result.put("lat", lat * 2 - mgLat)
        return result
    }

    /**
     * 将02坐标系（GCJ02）转换为百度坐标系（BD09）
     *
     * @param lng 经度
     * @param lat 纬度
     * @return 转换后的经纬度
     */
    @Throws(JSONException::class)
    fun gcj02ToBd09(lng: Double, lat: Double): JSONObject {
        val result = JSONObject()
        val z = sqrt(lng * lng + lat * lat) + 0.00002 * sin(lat * X_PI)
        val theta = atan2(lat, lng) + 0.000003 * cos(lng * X_PI)
        val bdLng = z * cos(theta) + 0.0065
        val bdLat = z * sin(theta) + 0.006
        result.put("lng", bdLng)
        result.put("lat", bdLat)
        return result
    }

    /**
     * 将百度坐标系（BD09）转换为02坐标系（GCJ02）
     *
     * @param lng 经度
     * @param lat 纬度
     * @return 转换后的经纬度
     */
    @Throws(JSONException::class)
    fun bd09ToGcj02(lng: Double, lat: Double): JSONObject {
        val result = JSONObject()
        val x = lng - 0.0065
        val y = lat - 0.006
        val z = sqrt(x * x + y * y) - 0.00002 * sin(y * X_PI)
        val theta = atan2(y, x) - 0.000003 * cos(x * X_PI)
        val gcjLng = z * cos(theta)
        val gcjLat = z * sin(theta)
        result.put("lng", gcjLng)
        result.put("lat", gcjLat)
        return result
    }

    /**
     * 将02坐标系（GCJ02）转换为腾讯坐标系（QQ）
     *
     * @param lng 经度
     * @param lat 纬度
     * @return 转换后的经纬度
     */
    @Throws(JSONException::class)
    fun gcj02ToQQ(lng: Double, lat: Double): JSONObject {
        val result = JSONObject()
        val z = sqrt(lng * lng + lat * lat) + 0.00002 * sin(lat * X_PI)
        val theta = atan2(lat, lng) + 0.000003 * cos(lng * X_PI)
        val qqLng = z * cos(theta) + 0.00330
        val qqLat = z * sin(theta) + 0.00150
        result.put("lng", qqLng)
        result.put("lat", qqLat)
        return result
    }

    /**
     * 将腾讯坐标系（QQ）转换为02坐标系（GCJ02）
     *
     * @param lng 经度
     * @param lat 纬度
     * @return 转换后的经纬度
     */
    @Throws(JSONException::class)
    fun qqToGcj02(lng: Double, lat: Double): JSONObject {
        val result = JSONObject()
        val x = lng - 0.00330
        val y = lat - 0.00150
        val z = sqrt(x * x + y * y) - 0.00002 * sin(y * X_PI)
        val theta = atan2(y, x) - 0.000003 * cos(x * X_PI)
        val gcjLng = z * cos(theta)
        val gcjLat = z * sin(theta)
        result.put("lng", gcjLng)
        result.put("lat", gcjLat)
        return result
    }

    /**
     * 判断是否超出中国范围
     *
     * @param lng 经度
     * @param lat 纬度
     * @return 是否超出中国范围
     */
    fun outOfChina(lng: Double, lat: Double): Boolean {
        return lng < 72.004 || lng > 137.8347 || lat < 0.8293 || lat > 55.8271
    }

    /**
     * 转换经度
     *
     * @param lng 经度
     * @param lat 纬度
     * @return 转换后的经度
     */
    private fun transformLng(lng: Double, lat: Double): Double {
        var ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * sqrt(abs(lng))
        ret += (20.0 * sin(6.0 * lng * Math.PI) + 20.0 * sin(2.0 * lng * Math.PI)) * 2.0 / 3.0
        ret += (20.0 * sin(lng * Math.PI) + 40.0 * sin(lng / 3.0 * Math.PI)) * 2.0 / 3.0
        ret += (150.0 * sin(lng / 12.0 * Math.PI) + 300.0 * sin(lng / 30.0 * Math.PI)) * 2.0 / 3.0
        return ret
    }

    /**
     * 转换纬度
     *
     * @param lat 纬度
     * @return 转换后的纬度
     */
    private fun transformLat(lng: Double, lat: Double): Double {
        var ret =
            -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * sqrt(abs(lng))
        ret += (20.0 * sin(6.0 * lng * Math.PI) + 20.0 * sin(2.0 * lng * Math.PI)) * 2.0 / 3.0
        ret += (20.0 * sin(lat * Math.PI) + 40.0 * sin(lat / 3.0 * Math.PI)) * 2.0 / 3.0
        ret += (160.0 * sin(lat / 12.0 * Math.PI) + 320 * sin(lat / 30.0 * Math.PI)) * 2.0 / 3.0
        return ret
    }

}