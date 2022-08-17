package cc.fastcv.koin

import android.app.Application
import cc.fastcv.koin.model.AppData
import cc.fastcv.koin.model.Person
import cc.fastcv.koin.model.UserData
import cc.fastcv.koin.viewmodel.MyViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
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
    }

}