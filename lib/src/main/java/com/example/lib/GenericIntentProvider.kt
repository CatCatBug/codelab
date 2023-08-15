package com.example.lib

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

object GenericIntentProvider {

    /**
     * 跳转google商店
     */
    fun getGooglePlayIntent(packageName: String): Intent {
        val uri = Uri.parse("market://details?id=$packageName")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.android.vending")
        return intent
    }

    /**
     * 从后台拉起 或者 启动app
     * 高版本似乎只能拉起自己
     */
    fun getBackAppIntent(packageManager: PackageManager, packageName: String): Intent? {
        return packageManager.getLaunchIntentForPackage(packageName)
    }

    /**
     * 跳转拨号盘并携带号码
     */
    fun getCallPhoneIntent(contactPhone: String): Intent {
        return Intent(
            Intent.ACTION_DIAL,
            Uri.parse("tel:$contactPhone")
        )
    }


}