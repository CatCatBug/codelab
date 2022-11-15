package cc.fastcv.jetpack.lifecycle

import android.util.Log
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

class CvClassesInfoCache {

    companion object {
        val sInstance = CvClassesInfoCache()

        private const val CALL_TYPE_NO_ARG = 0
        private const val CALL_TYPE_PROVIDER = 1
        private const val CALL_TYPE_PROVIDER_WITH_EVENT = 2
    }

    private val mCallbackMap = HashMap<Class<*>, CallbackInfo>()
    private val mHasLifecycleMethods = HashMap<Class<*>, Boolean>()

    fun hasLifecycleMethods(klass: Class<*>): Boolean {
        val hasLifecycleMethods = mHasLifecycleMethods[klass]
        if (hasLifecycleMethods != null) {
            return hasLifecycleMethods
        }

        val methods = getDeclareMethods(klass)
        for (method in methods) {

            val annotation = method.getAnnotation(CvOnLifecycleEvent::class.java)
            if (annotation != null) {
                createInfo(klass, methods)
                return true
            }
        }
        mHasLifecycleMethods[klass] = false
        return false

    }

    fun getInfo(klass: Class<*>): CallbackInfo {
        var existing = mCallbackMap[klass]
        if (existing != null) {
            return existing
        }

        existing = createInfo(klass, null)
        return existing
    }

    private fun getDeclareMethods(klass: Class<*>): Array<Method> {
        try {
            return klass.declaredMethods
        } catch (e: NoSuchMethodException) {
            throw IllegalArgumentException(
                "The observer class has some methods that use "
                        + "newer APIs which are not available in the current OS version. Lifecycles "
                        + "cannot access even other methods so you should make sure that your "
                        + "observer classes only access framework classes that are available "
                        + "in your min API level OR use lifecycle:compiler annotation processor.", e
            )
        }
    }

    private fun verifyAndPutHandler(
        handlers: HashMap<MethodReference, CvLifecycle.Event>,
        newHandler: MethodReference,
        newEvent: CvLifecycle.Event,
        klass: Class<*>
    ) {
        val event = handlers[newHandler]
        if (event != null && newEvent != event) {
            val method = newHandler.mMethod
            throw IllegalArgumentException(
                "Method " + method.name + " in " + klass.name
                        + " already declared with different @OnLifecycleEvent value: previous"
                        + " value " + event + ", new value " + newEvent
            )
        }
        if (event == null) {
            handlers[newHandler] = newEvent
        }
    }

    private fun createInfo(klass: Class<*>, declaredMethods: Array<Method>?): CallbackInfo {
        val superclass = klass.superclass
        Log.d("xcl_debug1", "createInfo: $superclass")
        val handlerToEvent = HashMap<MethodReference, CvLifecycle.Event>()
        if (superclass != null) {
            val superInfo = getInfo(superclass)
            handlerToEvent.putAll(superInfo.mHandlerToEvent)
        }

        //遍历实现的接口
        val interfaces = klass.interfaces
        for (intrfc in interfaces) {
            for (entry in getInfo(intrfc).mHandlerToEvent.entries) {
                verifyAndPutHandler(handlerToEvent, entry.key, entry.value, klass)
            }
        }

        val methods = declaredMethods ?: getDeclareMethods(klass)
        var hasLifecycleMethods = false
        for (method in methods) {
            val annotation = method.getAnnotation(CvOnLifecycleEvent::class.java) ?: continue
            hasLifecycleMethods = true
            val params = method.parameterTypes
            var callType = CALL_TYPE_NO_ARG
            if (params.isNotEmpty()) {
                callType = CALL_TYPE_PROVIDER
                if (!params[0].isAssignableFrom(CvLifecycleOwner::class.java)) {
                    throw IllegalArgumentException("invalid parameter type. Must be one and instanceof LifecycleOwner")
                }
            }
            val event = annotation.value

            if (params.size > 1) {
                callType = CALL_TYPE_PROVIDER_WITH_EVENT
                if (!params[1].isAssignableFrom(CvLifecycle.Event::class.java)) {
                    throw IllegalArgumentException("invalid parameter type. second arg must be an event")
                }

                if (event != CvLifecycle.Event.ON_ANY) {
                    throw IllegalArgumentException("Second arg is supported only for ON_ANY value")
                }
            }

            if (params.size > 2) {
                throw IllegalArgumentException("cannot have more than 2 params")
            }
            val methodReference = MethodReference(callType, method)
            verifyAndPutHandler(handlerToEvent, methodReference, event, klass)
        }
        val info = CallbackInfo(handlerToEvent)
        mCallbackMap[klass] = info
        mHasLifecycleMethods[klass] = hasLifecycleMethods
        return info
    }


    class CallbackInfo(val mHandlerToEvent: Map<MethodReference, CvLifecycle.Event>) {

        companion object {
            fun invokeMethodsForEvent(
                handlers: ArrayList<MethodReference>?,
                source: CvLifecycleOwner,
                event: CvLifecycle.Event,
                mWrapped: Any
            ) {
                if (handlers != null) {
                    for (i in handlers.size - 1..0) {
                        handlers[i].invokeCallback(source, event, mWrapped)
                    }
                }
            }
        }

        private val mEventToHandlers = HashMap<CvLifecycle.Event, ArrayList<MethodReference>>()

        init {
            for (entry in mHandlerToEvent.entries) {
                val event = entry.value
                var methodReferences = mEventToHandlers[event]
                if (methodReferences == null) {
                    methodReferences = ArrayList()
                    mEventToHandlers[event] = methodReferences
                }
                methodReferences.add(entry.key)
            }
        }

        fun invokeCallbacks(source: CvLifecycleOwner, event: CvLifecycle.Event, target: Any) {
            invokeMethodsForEvent(mEventToHandlers[event], source, event, target)
            invokeMethodsForEvent(
                mEventToHandlers[CvLifecycle.Event.ON_ANY],
                source,
                event,
                target
            )
        }
    }

    class MethodReference(val mCallType: Int, var mMethod: Method) {
        init {
            mMethod.isAccessible = true
        }

        fun invokeCallback(source: CvLifecycleOwner, event: CvLifecycle.Event, target: Any) {
            try {
                when (mCallType) {
                    CALL_TYPE_NO_ARG -> mMethod.invoke(target)
                    CALL_TYPE_PROVIDER -> mMethod.invoke(target, source)
                    CALL_TYPE_PROVIDER_WITH_EVENT -> mMethod.invoke(target, source, event)

                }
            } catch (e: InvocationTargetException) {
                throw RuntimeException("Failed to call observer method", e.cause)
            } catch (e: IllegalAccessError) {
                throw RuntimeException(e)
            }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other !is MethodReference) {
                return false
            }

            return mCallType == other.mCallType && mMethod.name == other.mMethod.name
        }

        override fun hashCode(): Int {
            return 31 * mCallType + mMethod.name.hashCode()
        }
    }

}