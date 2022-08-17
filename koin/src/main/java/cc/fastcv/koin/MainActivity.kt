package cc.fastcv.koin

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cc.fastcv.koin.databinding.ActivityMainBinding
import cc.fastcv.koin.model.AppData
import cc.fastcv.koin.model.Person
import cc.fastcv.koin.model.UserData
import cc.fastcv.koin.viewmodel.MyViewModel
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    //方式一
    private val person : Person by inject()
    //方式二
    private val person1 by inject<Person>()
    //方式三
    private val person2 = get<Person>()

    //方式一
    private val userData : UserData by inject()
    //方式二
    private val userData1 by inject<UserData>()
    //方式三
    private val userData2 = get<UserData>()


    //方式一
    private val viewmodel : MyViewModel by viewModels()
    //方式二
    private val viewmodel1 by viewModels<MyViewModel>()
    //方式三  在ViewModel中不适用
    private val viewmodel2 = get<MyViewModel>()

    val appData by inject<AppData>()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

//        showInfo()
        showInfo1()
    }

    private fun showInfo1() {
        Log.d("xcl_debug", viewmodel.hashCode().toString())
        Log.d("xcl_debug", viewmodel1.hashCode().toString())
        Log.d("xcl_debug", viewmodel2.hashCode().toString())

        Log.d("xcl_debug", "application是否为空:" + (appData.mApp == null))
    }

    private fun showInfo() {
        person.speak()
        person1.speak()
        person2.speak()

        Log.d("xcl_debug", person.hashCode().toString())
        Log.d("xcl_debug", person1.hashCode().toString())
        Log.d("xcl_debug", person2.hashCode().toString())

        userData.apply {
            age = 12
            userName = "张三"
        }
        userData.info()
        userData1.apply {
            age = 14
            userName = "李四"
        }
        userData1.info()
        userData2.apply {
            age = 18
            userName = "王五"
        }
        userData2.info()
        userData.info()
        Log.d("xcl_debug", userData.hashCode().toString())
        Log.d("xcl_debug", userData2.hashCode().toString())
        Log.d("xcl_debug", userData2.hashCode().toString())

        (1..100).forEach {
            Thread() {
                kotlin.run {
                    val userData3 = get<UserData>()
                    Log.d("xcl_debug", "${userData3.hashCode().toString()}   threadName:${Thread.currentThread().name}")
                }
            }.apply {
                name = "Thread$it"
                start()
            }
        }
    }
}