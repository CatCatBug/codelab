//package com.umeox.logger
//
//import android.content.Context
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.MotionEvent
//import android.view.View
//import android.widget.EditText
//import androidx.appcompat.app.AppCompatActivity
//import androidx.databinding.DataBindingUtil
//import androidx.databinding.ViewDataBinding
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.gyf.immersionbar.ImmersionBar
//import com.umeox.lib_base.ext.hideSoftKeyboard
//import com.umeox.lib_logger.UMLogger
//import java.lang.reflect.ParameterizedType
//
//abstract class BaseMvvmLibActivity<VM : ViewModel, DB : ViewDataBinding>
//    : AppCompatActivity() {
//
//    /** 当前界面 ViewModel 对象 */
//    protected open lateinit var viewModel: VM
//
//    /** 界面布局 id */
//    abstract val layoutResId: Int
//
//    /** 标记 - 触摸输入框以外范围是否隐藏软键盘*/
//    private var touchToHideInput = true
//
//    /** DataBinding 对象 */
//    lateinit var mBinding: DB
//
//    private var launcherStartTime = 0L
//
//    override fun attachBaseContext(newBase: Context?) {
//    launcherStartTime = System.currentTimeMillis()
//        super.attachBaseContext(newBase)
//
//    }
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        ImmersionBar.with(this).transparentNavigationBar().statusBarDarkFont(statusBarIconIsDark())
//            .navigationBarDarkIcon(navigationBarIconIsDark()).init()
//        // 初始化 DataBinding
////        mBinding = DataBindingUtil.inflate(
////            LayoutInflater.from(this),
////            layoutResId, null, false
////        )
//        initViewDataBinding()
//
//        // 绑定生命周期管理
//        mBinding.lifecycleOwner = this
//
//        // 设置布局
//        setContentView(mBinding.root)
//    }
//
//    override fun onWindowFocusChanged(hasFocus: Boolean) {
//        if (hasFocus && launcherStartTime != 0L) {
//            UMLogger.i(
//                this.javaClass.simpleName,
//                "启动总时长为：${System.currentTimeMillis() - launcherStartTime} ms"
//            )
//            launcherStartTime = 0L
//        }
//    }
//
//    private fun initViewDataBinding() {
//        try {
//            val pt =
//                this.javaClass.genericSuperclass as ParameterizedType?
//            val clazz =
//                pt!!.actualTypeArguments[1] as Class<DB>
//            val method = clazz.getDeclaredMethod("inflate", LayoutInflater::class.java)
//            method.isAccessible = true
//            mBinding = method.invoke(null, layoutInflater) as DB
//        } catch (e: Exception) {
//            UMLogger.e(this.javaClass.simpleName, e.message.toString())
//        }
//    }
//
//    /**
//     * 设置虚拟导航栏图标的颜色
//     * 默认是 暗色
//     * 返回  false则变为亮色
//     */
//    open fun navigationBarIconIsDark(): Boolean {
//        return true
//    }
//
//    /**
//     * 设置状态栏图标的颜色
//     * 默认是 暗色
//     * 返回  false则变为亮色
//     */
//    open fun statusBarIconIsDark(): Boolean {
//        return true
//    }
//
//    override fun onPause() {
//        super.onPause()
//        // 移除当前获取焦点控件的焦点，防止下个界面软键盘顶起布局
//        currentFocus?.clearFocus()
//    }
//
//    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        if (touchToHideInput) {
//            if (ev.action == MotionEvent.ACTION_DOWN) {
//                if (shouldHideInput(currentFocus, ev)) {
//                    // 需要隐藏软键盘
//                    currentFocus?.hideSoftKeyboard()
//                }
//                return super.dispatchTouchEvent(ev)
//            }
//            if (window.superDispatchTouchEvent(ev)) {
//                return true
//            }
//            return onTouchEvent(ev)
//        } else {
//            return super.dispatchTouchEvent(ev)
//        }
//    }
//
//    /** 根据当前焦点控件[v]、触摸事件[ev]判断是否需要隐藏软键盘 */
//    private fun shouldHideInput(v: View?, ev: MotionEvent): Boolean {
//        if (v is EditText) {
//            // 是输入框
//            val leftTop = intArrayOf(0, 0)
//            // 获取输入框当前的位置
//            v.getLocationInWindow(leftTop)
//            val top = leftTop[1]
//            val bottom = top + v.height
//            // 触摸位置不在输入框范围内，需要隐藏
//            return !(ev.y > top && ev.y < bottom)
//        }
//        return false
//    }
//}