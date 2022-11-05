package cc.fastcv.jetpack.internal

import java.util.*

open class SafeIterableMap<K, V> : Iterable<Map.Entry<K, V>?> {

    var mStart: Entry<K, V>? = null
    private var mEnd: Entry<K, V>? = null

    private val mIterators = WeakHashMap<SupportRemove<K, V>, Boolean>()

    private var mSize = 0

    protected open fun get(k: K): Entry<K, V>? {
        var currentNode = mStart
        while (currentNode != null) {
            if (currentNode.key!! == k) {
                break
            }
            currentNode = currentNode.mNext
        }
        return currentNode
    }

    open fun putIfAbsent(key: K, v: V): V? {
        val entry = get(key)
        if (entry != null) {
            return entry.value
        }
        put(key, v)
        return null
    }

    protected fun put(key: K, v: V): Entry<K, V> {
        val newEntry = Entry(key, v)
        mSize++
        if (mEnd == null) {
            mStart = newEntry
            mEnd = mStart
            return newEntry
        }

        mEnd!!.mNext = newEntry
        newEntry.mPrevious = mEnd
        mEnd = newEntry
        return newEntry
    }

    open fun remove(key: K): V? {
        val toRemove = get(key) ?: return null

        mSize--
        if (mIterators.isNotEmpty()) {
            for (key in mIterators.keys) {
                key.supportRemove(toRemove)
            }
        }

        if (toRemove.mPrevious != null) {
            toRemove.mPrevious!!.mNext = toRemove.mNext
        } else {
            mStart = toRemove.mNext
        }

        if (toRemove.mNext != null) {
            toRemove.mNext!!.mPrevious = toRemove.mPrevious
        } else {
            mEnd = toRemove.mPrevious
        }

        toRemove.mNext = null
        toRemove.mPrevious = null
        return toRemove.value
    }

    fun size(): Int {
        return mSize
    }

    override fun iterator(): Iterator<Map.Entry<K, V>?> {
        val iterator = AscendingIterator(mStart, mEnd)
        mIterators[iterator] = false
        return iterator
    }

    fun descendingIterator(): Iterator<Map.Entry<K, V>?> {
        val iterator = DescendingIterator(mEnd, mStart)
        mIterators[iterator] = false
        return iterator
    }

    fun iteratorWithAdditions(): IteratorWithAdditions {
        val iterator = IteratorWithAdditions()
        mIterators[iterator] = false
        return iterator
    }

    fun eldest(): Map.Entry<K, V>? {
        return mStart
    }

    fun newest(): Map.Entry<K, V>? {
        return mEnd
    }

    override fun equals(other: Any?): Boolean {
        if (other == this) {
            return true
        }
        if (other !is SafeIterableMap<*, *>) {
            return false
        }

        if (this.size() != other.size()) {
            return false
        }

        val iterator1 = iterator()
        val iterator2 = other.iterator()
        while (iterator1.hasNext() && iterator2.hasNext()) {
            val next1 = iterator1.next()
            val next2 = iterator2.next()
            if ((next1 == null && next2 != null) ||
                (next1 != null && next1 != next2)
            ) {
                return false
            }
        }

        return !iterator1.hasNext() && !iterator2.hasNext()
    }

    override fun hashCode(): Int {
        var h = 0
        val i = iterator()
        while (i.hasNext()) {
            h += i.next().hashCode()
        }
        return h
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append("[")
        val iterator = iterator()
        while (iterator.hasNext()) {
            builder.append(iterator.next().toString())
            if (iterator.hasNext()) {
                builder.append(", ")
            }
        }
        builder.append("]")
        return builder.toString()
    }

    abstract class ListIterator<K, V>(var mNext: Entry<K, V>?, var mExpectedEnd: Entry<K, V>?) :
        Iterator<Map.Entry<K, V>?>, SupportRemove<K, V> {

        override fun hasNext(): Boolean {
            return mNext != null
        }

        override fun supportRemove(entry: Entry<K, V>) {
            if (mExpectedEnd == entry && entry == mNext) {
                mNext = null
                mExpectedEnd = null
            }

            if (mExpectedEnd == entry) {
                mExpectedEnd = backward(mExpectedEnd)
            }

            if (mNext == entry) {
                mNext = nextNode()
            }
        }

        private fun nextNode(): Entry<K, V>? {
            if (mNext == mExpectedEnd || mExpectedEnd == null) {
                return null
            }
            return forward(mNext)
        }

        override fun next(): Map.Entry<K, V>? {
            val result = mNext
            mNext = nextNode()
            return result
        }

        abstract fun forward(entry: Entry<K, V>?): Entry<K, V>?

        abstract fun backward(entry: Entry<K, V>?): Entry<K, V>?
    }

    class AscendingIterator<K, V>(start: Entry<K, V>?, expectedEnd: Entry<K, V>?) :
        ListIterator<K, V>(start, expectedEnd) {
        override fun forward(entry: Entry<K, V>?): Entry<K, V>? {
            return entry?.mNext
        }

        override fun backward(entry: Entry<K, V>?): Entry<K, V>? {
            return entry?.mPrevious
        }
    }

    class DescendingIterator<K, V>(start: Entry<K, V>?, expectedEnd: Entry<K, V>?) :
        ListIterator<K, V>(start, expectedEnd) {
        override fun forward(entry: Entry<K, V>?): Entry<K, V>? {
            return entry?.mPrevious
        }

        override fun backward(entry: Entry<K, V>?): Entry<K, V>? {
            return entry?.mNext
        }
    }

    inner class IteratorWithAdditions : Iterator<Map.Entry<K, V>?>, SupportRemove<K, V> {

        private var mCurrent: Entry<K, V>? = null

        private var mBeforeStart = true


        override fun supportRemove(entry: Entry<K, V>) {
            if (entry == mCurrent) {
                mCurrent = mCurrent!!.mPrevious
                mBeforeStart = (mCurrent == null)
            }
        }

        override fun hasNext(): Boolean {
            if (mBeforeStart) {
                return mStart != null
            }
            return mCurrent != null && mCurrent!!.mNext != null
        }

        override fun next(): Map.Entry<K, V>? {
            if (mBeforeStart) {
                mBeforeStart = false
                mCurrent = mStart
            } else {
                mCurrent = mCurrent?.mNext
            }
            return mCurrent
        }
    }

    interface SupportRemove<K, V> {
        fun supportRemove(entry: Entry<K, V>)
    }

    class Entry<K, V>(private val mKey: K, private val mValue: V) : Map.Entry<K, V> {

        var mNext: Entry<K, V>? = null
        var mPrevious: Entry<K, V>? = null

        override val key: K
            get() = mKey

        override val value: V
            get() = mValue

        fun setValue(value: V) {
            throw UnsupportedOperationException("An entry modification is not supported")
        }

        override fun toString(): String {
            return "$mKey = $mValue"
        }

        override fun equals(other: Any?): Boolean {
            if (other === this) {
                return true
            }
            if (other !is Entry<*, *>) {
                return false
            }
            return mKey!! == other.mKey && mValue!! == other.mValue
        }

        override fun hashCode(): Int {
            return mKey.hashCode() xor mValue.hashCode()
        }
    }
}