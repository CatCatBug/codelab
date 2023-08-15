package com.example.lib

import android.R
import android.R.attr.radius
import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.annotation.WorkerThread
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import jp.wasabeef.glide.transformations.BlurTransformation
import java.io.File


/**
 * 结合三方框架 提供给ImageView的方法
 *
 * Glide 图片处理框架
 * implementation 'com.github.bumptech.glide:glide:4.12.0'
 * kapt 'com.github.bumptech.glide:compiler:4.12.0'
 */
object ImageViewUtil {

    /**
     * 显示在线图片
     */
    fun loadImage(imgUrl: String, imageview: ImageView, placeholder: Int, error: Int) {
        val options = RequestOptions.centerCropTransform()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(placeholder)
            .error(error)
        Glide.with(imageview.context).load(imgUrl).apply(options)
            .into(imageview)
    }

    /**
     * 显示文件
     */
    fun loadImage(file: File, imageview: ImageView, placeholder: Int, error: Int) {
        val options = RequestOptions.centerCropTransform()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(placeholder)
            .error(error)
        Glide.with(imageview.context).load(file).apply(options)
            .into(imageview)
    }

    /**
     * 显示在线图片为圆形图片
     */
    fun loadCircleImage(
        imgUrl: String,
        imageview: ImageView,
        placeholder: Int,
        error: Int
    ) {
        val options: RequestOptions = RequestOptions.bitmapTransform(CircleCrop())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(placeholder)
            .fallback(error)
            .error(error)
            .priority(Priority.HIGH)

        Glide.with(imageview.context).load(imgUrl).apply(options)
            .into(imageview)
    }

    /**
     * 显示在线图片为圆形图片
     */
    fun loadCircleImage(
        file: File,
        imageview: ImageView,
        placeholder: Int,
        error: Int
    ) {
        val options: RequestOptions = RequestOptions.bitmapTransform(CircleCrop())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(placeholder)
            .fallback(error)
            .error(error)
            .priority(Priority.HIGH)

        Glide.with(imageview.context).load(file).apply(options)
            .into(imageview)
    }

    /**
     * 获取在线图片
     * 转存为Bitmap 并获取Bitmap对象
     * 耗时操作  请勿再主线程操作
     */
    @WorkerThread
    fun getBitmapByImgUrl(context: Context, url: String): Bitmap {
        return Glide.with(context)
            .asBitmap()
            .load(url)
            .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .get()
    }

    fun loadBlurImage(
        imgUrl: String,
        imageview: ImageView,
        radius: Int,
        placeholder: Int,
        error: Int
    ) {
        val options: RequestOptions =
            RequestOptions.bitmapTransform(BlurTransformation(imageview.context, radius))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeholder)
                .fallback(error)
                .error(error)
                .priority(Priority.HIGH)

        Glide.with(imageview.context).load(imgUrl).apply(options)
            .into(imageview)
    }


    fun loadBlurImage(
        file: File,
        imageview: ImageView,
        radius: Int,
        placeholder: Int,
        error: Int
    ) {
        val options: RequestOptions =
            RequestOptions.bitmapTransform(BlurTransformation(imageview.context, radius))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeholder)
                .fallback(error)
                .error(error)
                .priority(Priority.HIGH)

        Glide.with(imageview.context).load(file).apply(options)
            .into(imageview)
    }
}