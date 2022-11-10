package cc.fastcv.jetpack.viewmodel

class CvViewModelStore {

    private val mMap = HashMap<String,CvViewModel>()

    fun put(key: String, viewModel: CvViewModel) {
        val oldViewModel = mMap.put(key,viewModel)
        oldViewModel?.onCleared()
    }

    fun get(key: String) : CvViewModel? {
        return mMap[key]
    }

    fun keys() : Set<String> {
        return mMap.keys
    }

    fun clear() {
        for (vm in mMap.values) {
            vm.clear()
        }
        mMap.clear()
    }
}