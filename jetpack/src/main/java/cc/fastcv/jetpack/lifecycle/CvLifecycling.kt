package cc.fastcv.jetpack.lifecycle

import android.util.Log
import androidx.lifecycle.GeneratedAdapter
import java.lang.RuntimeException
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CvLifecycling {

    companion object {
        private const val REFLECTIVE_CALLBACK = 1
        private const val GENERATED_CALLBACK = 2


        private val sCallbackCache = HashMap<Class<*>, Int>()
        private val sClassToAdapters =
            HashMap<Class<*>, ArrayList<Constructor<in CvGeneratedAdapter>>>()

        fun getCallback(any: Any): CvGenericLifecycleObserver {
            val observer = lifecycleEventObserver(any)
            return object : CvGenericLifecycleObserver {
                override fun onStateChanged(source: CvLifecycleOwner, event: CvLifecycle.Event) {
                    observer.onStateChanged(source, event)
                }
            }
        }

        fun lifecycleEventObserver(any: Any): CvLifecycleEventObserver {
            val isLifecycleEventObserver = any is CvLifecycleEventObserver
            val isFullLifecycleObserver = any is CvFullLifecycleObserver

            Log.d("xcl_debug1", "lifecycleEventObserver: isLifecycleEventObserver = $isLifecycleEventObserver  isFullLifecycleObserver = $isFullLifecycleObserver ")
            if (isLifecycleEventObserver && isFullLifecycleObserver) {
                return CvFullLifecycleObserverAdapter(
                    any as CvFullLifecycleObserver,
                    any as CvLifecycleEventObserver
                )
            }

            if (isFullLifecycleObserver) {
                return CvFullLifecycleObserverAdapter(any as CvFullLifecycleObserver, null)
            }

            if (isLifecycleEventObserver) {
                return any as CvLifecycleEventObserver
            }

            val klass = any.javaClass
            val type = getObserverConstructorType(klass)
            Log.d("xcl_debug1","lifecycleEventObserver  type = $type")
            if (type == GENERATED_CALLBACK) {
                val constructors = sClassToAdapters[klass]
                if (constructors?.size == 1) {
                    val generatedAdapter = createGeneratedAdapter(constructors[0], any)
                    return CvSingleGeneratedAdapterObserver(generatedAdapter)
                }
                val adapters = arrayOf<CvGeneratedAdapter>()
                adapters.mapIndexed { index, _ ->
                    adapters[index] = createGeneratedAdapter(constructors?.get(index)!! ,any)
                }
                return CvCompositeGeneratedAdaptersObserver(adapters)
            }
            return CvReflectiveGenericLifecycleObserver(any)
        }

        private fun createGeneratedAdapter(
            constructor: Constructor<in CvGeneratedAdapter>,
            any: Any
        ): CvGeneratedAdapter {
            try {
                return constructor.newInstance(any) as CvGeneratedAdapter
            } catch (e: IllegalAccessException) {
                throw RuntimeException(e)
            } catch (e: InstantiationException) {
                throw RuntimeException(e)
            } catch (e: InvocationTargetException) {
                throw RuntimeException(e)
            }
        }

        private fun generatedConstructor(klass: Class<*>): Constructor<in CvGeneratedAdapter>? {
            try {
                val aPackage = klass.`package`
                Log.d("xcl_debug1", "generatedConstructor: aPackage = $aPackage")
                val name = klass.canonicalName
                Log.d("xcl_debug1", "generatedConstructor: name = $name")
                val fullPackage = aPackage?.name ?: ""
                Log.d("xcl_debug1", "generatedConstructor: fullPackage = $fullPackage")
                val adapterName = getAdapterName(
                    if (fullPackage.isEmpty()) {
                        name
                    } else {
                        name.substring(fullPackage.length + 1)
                    }
                )
                Log.d("xcl_debug1", "generatedConstructor: adapterName = $adapterName")

                val aClass = Class.forName(
                    if (fullPackage.isEmpty()) {
                        adapterName
                    } else {
                        "$fullPackage.$adapterName"
                    }
                )

                val constructor = aClass.getDeclaredConstructor(klass)
                if (!constructor.isAccessible) {
                    constructor.isAccessible = true
                }
                return constructor as Constructor<in CvGeneratedAdapter>?
            } catch (e: ClassNotFoundException) {
                return null
            } catch (e: NoSuchMethodException) {
                throw RuntimeException(e)
            }
        }

        private fun getObserverConstructorType(klass: Class<*>): Int {
            val callbackCache = sCallbackCache[klass]
            if (callbackCache != null) {
                return callbackCache
            }
            val type = resolveObserverCallbackType(klass)
            sCallbackCache[klass] = type
            return type
        }

        private fun resolveObserverCallbackType(klass: Class<*>): Int {
            Log.d("xcl_debug1", "resolveObserverCallbackType: canonicalName  = ${klass.canonicalName}")
            //获取全路径
            if (klass.canonicalName == null) {
                return REFLECTIVE_CALLBACK
            }

            val constructor = generatedConstructor(klass)
            if (constructor != null) {
                sClassToAdapters[klass] = Collections.singletonList(constructor) as ArrayList<Constructor<in CvGeneratedAdapter>>
                return GENERATED_CALLBACK
            }

            val hasLifecycleMethods = CvClassesInfoCache.sInstance.hasLifecycleMethods(klass)
            if (hasLifecycleMethods) {
                return REFLECTIVE_CALLBACK
            }

            val superclass = klass.superclass
            var adapterConstructors: ArrayList<Constructor<in CvGeneratedAdapter>>? = null
            if (isLifecycleParent(superclass)) {
                if (getObserverConstructorType(superclass) == REFLECTIVE_CALLBACK) {
                    return REFLECTIVE_CALLBACK
                }
                adapterConstructors = ArrayList(sClassToAdapters[superclass])
            }

            for (intrface in klass.interfaces) {
                if (!isLifecycleParent(intrface)) {
                    continue
                }
                if (getObserverConstructorType(intrface) == REFLECTIVE_CALLBACK) {
                    return REFLECTIVE_CALLBACK
                }
                if (adapterConstructors == null) {
                    adapterConstructors = ArrayList()
                }
                sClassToAdapters[intrface]?.let { adapterConstructors.addAll(it) }
            }

            if (adapterConstructors != null) {
                sClassToAdapters.put(klass, adapterConstructors)
                return GENERATED_CALLBACK
            }

            return REFLECTIVE_CALLBACK
        }

        private fun isLifecycleParent(klass: Class<*>?): Boolean {
            return klass != null && CvLifecycleObserver::class.java.isAssignableFrom(klass)
        }

        fun getAdapterName(className: String): String {
            return className.replace(".", "_") + "_LifecycleAdapter";
        }

    }


}