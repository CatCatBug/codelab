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
import cc.fastcv.koin.model.*
import cc.fastcv.koin.viewmodel.MyViewModel
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

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


    //方式一
    private val normalData:NormalData by inject(named("nameAnum"))
    //方式二
    private val normalData1 by inject<NormalData>(named("app"))
    //方式三
    private val normalData2 = get<NormalData>(named("appData"))

    //方式一
    private val weatherData:WeatherData by inject(named("wea_name"))
    //方式二
    private val weatherData1 by inject<WeatherData>(named("wea_app"))
    //方式三
    private val weatherData2 = get<WeatherData>(named("wea_appData"))

    val appData by inject<AppData>()

    val viewData by inject<ViewData> { parametersOf(binding.navView) }

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
//        showInfo1()
        showInfo2()
    }

    private fun showInfo2() {
        weatherData.printData("weather1")
        weatherData1.printData("weather2")
        weatherData2.printData("weather3")

        viewData.printId()

        CompontData().printInfo()//这边直接new对象,看里面注入的对象信息
    }

    private fun showInfo1() {
        Log.d("xcl_debug", viewmodel.hashCode().toString())
        Log.d("xcl_debug", viewmodel1.hashCode().toString())
        Log.d("xcl_debug", viewmodel2.hashCode().toString())

        Log.d("xcl_debug", "application是否为空:" + (appData.mApp == null))

        normalData.printInfo("norData1")
        normalData1.printInfo("norData2")
        normalData2.printInfo("norData3")
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