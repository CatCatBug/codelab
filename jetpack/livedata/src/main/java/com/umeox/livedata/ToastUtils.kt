package com.umeox.livedata

import android.R
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.*
import androidx.core.widget.TextViewCompat
import java.lang.ref.WeakReference


object ToastUtils {
    private const val COLOR_DEFAULT = -0x1000001
    private val HANDLER = Handler(Looper.getMainLooper())
    private var sToast: Toast? = null
    private var sViewWeakReference: WeakReference<View>? = null
    private var sLayoutId = -1
    private var gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
    private var xOffset = 0
    private var yOffset =
        (64 * Utils.getApp().getResources().getDisplayMetrics().density + 0.5) as Int
    private var bgColor = COLOR_DEFAULT
    private var bgResource = -1
    private var msgColor = COLOR_DEFAULT

    /**
     * 设置吐司位置
     *
     * @param gravity 位置
     * @param xOffset x偏移
     * @param yOffset y偏移
     */
    fun setGravity(gravity: Int, xOffset: Int, yOffset: Int) {
        this.gravity = gravity
        this.xOffset = xOffset
        this.yOffset = yOffset
    }

    /**
     * 设置背景颜色
     */
    fun setBgColor(@ColorInt backgroundColor: Int) {
        bgColor = backgroundColor
    }

    /**
     * 设置背景资源
     *
     * @param bgResource 背景资源
     */
    fun setBgResource(@DrawableRes bgResource: Int) {
        this.bgResource = bgResource
    }

    /**
     * 设置消息颜色
     *
     * @param msgColor 颜色
     */
    fun setMsgColor(@ColorInt msgColor: Int) {
        this.msgColor = msgColor
    }

    /**
     * 安全地显示短时吐司
     *
     * @param text 文本
     */
    fun showShort(@NonNull text: CharSequence?) {
        text?.let {
            show(it, Toast.LENGTH_SHORT)
        }
    }

    /**
     * 安全地显示短时吐司
     *
     * @param resId 资源Id
     */
    fun showShort(@StringRes resId: Int) {
        show(resId, Toast.LENGTH_SHORT)
    }

    /**
     * 安全地显示短时吐司
     *
     * @param resId 资源Id
     * @param args  参数
     */
    fun showShort(@StringRes resId: Int, vararg args: Any) {
        show(resId, Toast.LENGTH_SHORT, *args)
    }

    /**
     * 安全地显示短时吐司
     *
     * @param format 格式
     * @param args   参数
     */
    fun showShort(format: String, vararg args: Any) {
        show(format, Toast.LENGTH_SHORT, *args)
    }

    /**
     * 安全地显示长时吐司
     *
     * @param text 文本
     */
    fun showLong(@NonNull text: CharSequence) {
        show(text, Toast.LENGTH_LONG)
    }

    /**
     * 安全地显示长时吐司
     *
     * @param resId 资源Id
     */
    fun showLong(@StringRes resId: Int) {
        show(resId, Toast.LENGTH_LONG)
    }

    /**
     * 安全地显示长时吐司
     *
     * @param resId 资源Id
     * @param args  参数
     */
    fun showLong(@StringRes resId: Int, vararg args: Any) {
        show(resId, Toast.LENGTH_LONG, *args)
    }

    /**
     * 安全地显示长时吐司
     *
     * @param format 格式
     * @param args   参数
     */
    fun showLong(format: String, vararg args: Any) {
        show(format, Toast.LENGTH_LONG, *args)
    }

    /**
     * 安全地显示短时自定义吐司
     */
    fun showCustomShort(@LayoutRes layoutId: Int): View {
        val view: View = getView(layoutId)
        show(view, Toast.LENGTH_SHORT)
        return view
    }

    /**
     * 安全地显示长时自定义吐司
     */
    fun showCustomLong(@LayoutRes layoutId: Int): View {
        val view: View = getView(layoutId)
        show(view, Toast.LENGTH_LONG)
        return view
    }

    /**
     * 取消吐司显示
     */
    fun cancel() {
        if (sToast != null) {
            sToast!!.cancel()
            sToast = null
        }
    }

    private fun show(@StringRes resId: Int, duration: Int) {
        show(Utils.getApp().getResources().getText(resId).toString(), duration)
    }

    private fun show(@StringRes resId: Int, duration: Int, vararg args: Any) {
        show(
            java.lang.String.format(Utils.getApp().getResources().getString(resId), args),
            duration
        )
    }

    private fun show(format: String, duration: Int, vararg args: Any) {
        show(String.format(format, *args), duration)
    }

    private fun show(text: CharSequence, duration: Int) {
        HANDLER.post(Runnable {
            cancel()
            sToast = Toast.makeText(Utils.getApp(), text, duration)
            // solve the font of toast
            val tvMessage = sToast!!.view!!.findViewById<View>(R.id.message) as TextView
            TextViewCompat.setTextAppearance(tvMessage, R.style.TextAppearance)
            tvMessage.setTextColor(msgColor)
            setBgAndGravity()
            sToast!!.show()
        })
    }

    private fun show(view: View, duration: Int) {
        HANDLER.post(Runnable {
            cancel()
            sToast = Toast(Utils.getApp())
            sToast!!.view = view
            sToast!!.duration = duration
            setBgAndGravity()
            sToast!!.show()
        })
    }

    private fun setBgAndGravity() {
        val toastView: View? = sToast!!.view
        if (bgResource != -1) {
            toastView!!.setBackgroundResource(bgResource)
        } else if (bgColor != COLOR_DEFAULT) {
            val background: Drawable = toastView!!.getBackground()
            background.colorFilter =
                PorterDuffColorFilter(bgColor, PorterDuff.Mode.SRC_IN)
        }
        sToast!!.setGravity(gravity, xOffset, yOffset)
    }

    private fun getView(@LayoutRes layoutId: Int): View {
        if (sLayoutId == layoutId) {
            if (sViewWeakReference != null) {
                val toastView: View? = sViewWeakReference!!.get()
                if (toastView != null) {
                    return toastView
                }
            }
        }
        val inflate = Utils.getApp()
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val toastView: View = inflate.inflate(layoutId, null)
        sViewWeakReference = WeakReference(toastView)
        sLayoutId = layoutId
        return toastView
    }
}