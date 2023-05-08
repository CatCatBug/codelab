package cc.fastcv.i18n

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.LocaleList
import android.text.TextUtils
import androidx.annotation.RequiresApi
import java.util.*

/**
 * /**
  *
  * @author xcl
  * create at 2022/8/10 16:07
  * 暂时发现 除了中文简体/繁体的操作复杂一些,其他较为简单
  */

 */
object I18nManager {

    private lateinit var application: Application

    fun initI18n(application: Application) {
        this.application = application
    }

    //APP支持的语言
    //默认 随系统
    private const val SP_KEY_APP_LANGUAGE = "app_language_key"

    private var currentLanguage = SP_KEY_APP_LANGUAGE.getSpStringValue("")

    private val appSupportLanguageArray = arrayOf("zh_HK", "zh_CN", "ar", "en")
    private val appSupportLanguageDescriptArray = arrayOf("中文繁體", "中文简体", "عربي", "English")


    fun applyAppLanguage(context: Context?): Context? {
        if (context == null) return context

        val language = if (TextUtils.isEmpty(currentLanguage)) {
            compatibleConversionSystemLanguage()
        } else {
            currentLanguage
        }

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //7.0及以上的方法
            createConfiguration(context, language)
        } else {
            updateConfiguration(context, language)
            context
        }
    }

    //将系统的语言字符识别为对应的语言缩写
    private fun compatibleConversionSystemLanguage(): String {
        return when (val currentSystemLanguage = getSystemLanguage()) {
            in appSupportLanguageArray -> currentSystemLanguage
            else -> "en"
        }
    }

    private fun getSystemLanguage(): String {
        val language: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            application.resources.configuration.locales[0].language
        } else {
            application.resources.configuration.locale.language
        }
        return language
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun createConfiguration(context: Context, language: String): Context {
        val resources = context.resources
        val locale = when (language) {
            "zh_CN" -> {
                Locale.SIMPLIFIED_CHINESE
            }
            "zh_HK" -> {
                Locale.TRADITIONAL_CHINESE
            }
            else -> {
                Locale(language)
            }
        }

        val configuration = resources.configuration
        configuration.setLocale(locale)
        val localeList = LocaleList(locale)
        LocaleList.setDefault(localeList)
        configuration.setLocales(localeList)
        return context.createConfigurationContext(configuration)
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun updateConfiguration(context: Context, language: String) {
        val resources = context.resources
        val locale = when (language) {
            "zh_CN" -> {
                Locale.SIMPLIFIED_CHINESE
            }
            "zh_HK" -> {
                Locale.TRADITIONAL_CHINESE
            }
            else -> {
                Locale(language)
            }
        }

        Locale.setDefault(locale)
        val configuration = resources.configuration
        configuration.setLocale(locale)
        val displayMetrics = resources.displayMetrics
        resources.updateConfiguration(configuration, displayMetrics)
    }

    /**
     * 获取国家码
     */
    fun getCountryZipCode(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            application.resources.configuration.locales[0].country
        } else {
            application.resources.configuration.locale.country
        }
    }

    fun updateLanguage(index: Int): Boolean {
        if (index == -1) {
            return false
        }

        val newLanguage = appSupportLanguageArray[index]
        if (newLanguage == currentLanguage) {
            return false
        }
        currentLanguage = newLanguage
        SP_KEY_APP_LANGUAGE.saveSpStringValue(currentLanguage)

        updateApplicationContext()
        return true
    }

    private fun updateApplicationContext() {
        //更新ApplicationContext的语言设置
        val resources = application.resources
        val configuration = resources.configuration
        val locale = when (currentLanguage) {
            "zh_CN" -> {
                Locale.SIMPLIFIED_CHINESE
            }
            "zh_HK" -> {
                Locale.TRADITIONAL_CHINESE
            }
            else -> {
                Locale(currentLanguage)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocales(LocaleList(locale))
        } else {
            configuration.setLocale(locale)
        }
        val dm = resources.displayMetrics
        resources.updateConfiguration(configuration, dm)
    }

    fun getSupportLanguageList() = appSupportLanguageArray.mapIndexed { index, language ->
        LanguageItem(language, appSupportLanguageDescriptArray[index], language == currentLanguage)
    }
}

data class LanguageItem(var language: String, var descript: String, var checked: Boolean)