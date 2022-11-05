package cc.fastcv.jetpack.lifecycle

class CvMethodCallsLogger {
    private val mCallMethods = HashMap<String, Int>()

    fun approveCall(name: String, type: Int): Boolean {
        val mask = mCallMethods[name] ?: 0
        val wasCalled = ((mask and type) != 0)
        mCallMethods[name] = mask or type
        return !wasCalled
    }
}