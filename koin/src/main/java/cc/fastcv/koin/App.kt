package cc.fastcv.koin

import android.app.Application
import android.view.View
import cc.fastcv.koin.model.*
import cc.fastcv.koin.viewmodel.MyViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.qualifier.named
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@App)
            modules(listOf(appModule))
        }
    }

    private val appModule = module {//里面添加各种注入对象
        factory {//普通的注入方式
            Person()
        }

        factory {
            AppData(get())
        }

        single {
            UserData()
        }

        viewModel {
            MyViewModel()
        }

        factory(named("nameAnum")) {
            //该限定符的构造方法中包含字符串和数字
            NormalData("曹老板", 12)
        }
        factory(named("app")) {
            //该限定符定义构造方法中有appliaction的
            NormalData(get<Application>())
        }
        factory(named("appData")) {
            //该限定符定义构造方法中有AppData的
            NormalData(get<AppData>())
        }

        factory(named("wea_name")) {
            WeatherData(get(named("nameAnum")))
            //这边get方法中有一个泛型,可以指定传入的对象的类型,因为我构造函数只有一个,所以会智能输入,可以省略掉
        }
        factory(named("wea_app")) {
            WeatherData(get(named("app")))//这边就智能省略掉泛型了
        }
        factory(named("wea_appData")) {
            WeatherData(get(named("appData")))
        }

        factory {
            //外部调用的方式,如果是多参数也一样,聪明的同学么应该要学会举一反三了
                (view: View) ->
            ViewData(view)
        }
    }

}