package cc.fastcv.fragment

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val mHomeFragment = HomeFragment()
    private val mDiscoverFragment = DiscoverFragment()
    private val mFreshFragment = FreshFragment()
    private val mMsgFragment = MsgFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<RadioButton>(R.id.home_rb).setOnClickListener(this)
        findViewById<RadioButton>(R.id.find_rb).setOnClickListener(this)
        findViewById<RadioButton>(R.id.new_rb).setOnClickListener(this)
        findViewById<RadioButton>(R.id.message_rb).setOnClickListener(this)

        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.main_tab_fl, mHomeFragment)
        fragmentTransaction.commit()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.home_rb -> homeRbClick()
            R.id.find_rb -> findRbClick()
            R.id.new_rb -> newRbClick()
            R.id.message_rb -> messageRbClick()
        }
    }


    private fun homeRbClick() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        // 替换成当前页面
        fragmentTransaction.replace(R.id.main_tab_fl, mHomeFragment)
        fragmentTransaction.commit()
    }


    private fun findRbClick() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_tab_fl, mDiscoverFragment)
        fragmentTransaction.commit()
    }

    private fun newRbClick() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_tab_fl, mFreshFragment)
        fragmentTransaction.commit()
    }

    private fun messageRbClick() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_tab_fl, mMsgFragment)
        fragmentTransaction.commit()
    }
}