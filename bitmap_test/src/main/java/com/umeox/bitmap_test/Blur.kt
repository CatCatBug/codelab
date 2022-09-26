package com.umeox.bitmap_test

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.text.format.Formatter
import android.util.Log

var scaleFactor = 2

fun blur(id: Int, context: Context, radius: Float = 24.0f): Bitmap? {
    val bkg = BitmapFactory.decodeResource(context.resources, id)
    Log.d("xcl_debug", "bkg.width: ${bkg.width}    bkg.height: ${bkg.height} ")
    var overlay =
        Bitmap.createScaledBitmap(bkg, bkg.width / scaleFactor, bkg.height / scaleFactor, false)
    Log.d("xcl_debug", "blur: ${Formatter.formatFileSize(context,
        overlay.allocationByteCount.toLong()
    )}")
    return blur(context, overlay, radius) //高斯模糊
}

private fun blur(context: Context, image: Bitmap, radius: Float): Bitmap? {
    val rs = RenderScript.create(context)
    val outputBitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_4444)
    val `in` = Allocation.createFromBitmap(rs, image)
    val out = Allocation.createFromBitmap(rs, outputBitmap)
    val intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
    intrinsicBlur.setRadius(radius)
    intrinsicBlur.setInput(`in`)
    intrinsicBlur.forEach(out)
    out.copyTo(outputBitmap)
    image.recycle()
    rs.destroy()
    return outputBitmap
}