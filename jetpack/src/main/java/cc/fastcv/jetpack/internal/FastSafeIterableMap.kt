package cc.fastcv.jetpack.internal


open class FastSafeIterableMap<K, V> : SafeIterableMap<K, V>() {

    private val mHashMap = HashMap<K, Entry<K, V>>()

    override fun get(k: K): Entry<K, V>? {
        return mHashMap[k]
    }

    override fun putIfAbsent(key: K, v: V): V? {
        val current = get(key)
        if (current != null) {
            return current.value
        }
        mHashMap[key] = put(key, v)
        return null
    }

    override fun remove(key: K): V? {
        val removed = super.remove(key)
        mHashMap.remove(key)
        return removed
    }

    fun contains(key: K): Boolean {
        return mHashMap.containsKey(key)
    }

    fun ceil(k: K): Map.Entry<K, V>? {
        if (contains(k)) {
            return mHashMap[k]?.mPrevious
        }
        return null
    }

}