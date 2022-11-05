package cc.fastcv.jetpack.lifecycle

import android.util.Log
import androidx.annotation.NonNull
import cc.fastcv.jetpack.executor.CvArchTaskExecutor
import cc.fastcv.jetpack.internal.FastSafeIterableMap
import java.lang.ref.WeakReference
import kotlin.math.log

class CvLifecycleRegistry : CvLifecycle {
    companion object {
        private const val TAG = "xcl_debug"

        fun min(state1: State, state2: State?): State {
            return if ((state2 != null && state2 < state1)) {
                state2
            } else {
                state1
            }
        }
    }

    private val mObserverMap = FastSafeIterableMap<CvLifecycleObserver, ObserverWithState>()

    private var mState: State

    private var mLifecycleOwner: WeakReference<CvLifecycleOwner>

    private var mAddingObserverCounter = 0

    private var mHandlingEvent = false
    private var mNewEventOccurred = false

    private val mParentStates = ArrayList<State>()
    private var mEnforceMainThread: Boolean = false

    constructor(provider: CvLifecycleOwner) {
        mLifecycleOwner = WeakReference<CvLifecycleOwner>(provider)
        mState = State.INITIALIZED
        mEnforceMainThread = true
    }

    constructor(provider: CvLifecycleOwner, enforceMainThread: Boolean) {
        mLifecycleOwner = WeakReference<CvLifecycleOwner>(provider)
        mState = State.INITIALIZED
        mEnforceMainThread = enforceMainThread
    }

    fun handleLifecycleEvent(@NonNull event: Event) {
        enforceMainThreadIfNeeded("handleLifecycleEvent")
        moveToState(event.targetState)
    }

    private fun moveToState(next: State) {
        if (mState == next) {
            Log.d(TAG, "moveToState: mState == next")
            return
        }
        mState = next
        Log.d(TAG, "moveToState: $mHandlingEvent    $mAddingObserverCounter    ${Thread.currentThread().name}")
        if (mHandlingEvent || mAddingObserverCounter != 0) {
            mNewEventOccurred = true
            return
        }
        mHandlingEvent = true
        sync()
        mHandlingEvent = false
    }

    //是否已经是同步状态了
    private fun isSynced(): Boolean {
        Log.d(TAG, "isSynced: ${mObserverMap.size()}")
        if (mObserverMap.size() == 0) {
            return true
        }
        Log.d(TAG, "isSynced: mObserverMap.eldest():${mObserverMap.eldest()}")
        val eldestObserverState = mObserverMap.eldest()?.value?.mState
        Log.d(TAG, "isSynced: mObserverMap.newest():${mObserverMap.newest()}")
        val newestObserverState = mObserverMap.newest()?.value?.mState
        Log.d(
            TAG,
            "isSynced: eldestObserverState:$eldestObserverState   newestObserverState:$newestObserverState    mState:$mState"
        )
        return eldestObserverState == newestObserverState && mState == newestObserverState
    }

    private fun calculateTargetState(observer: CvLifecycleObserver): State {
        //获取上一个观察者
        val previous = mObserverMap.ceil(observer)

        Log.d(TAG, "calculateTargetState: previous = $previous")
        val siblingState = previous?.value?.mState
        val parentState = if (mParentStates.isNotEmpty()) {
            mParentStates[mParentStates.size - 1]
        } else {
            null
        }
        Log.d(
            TAG,
            "calculateTargetState: siblingState = $siblingState   parentState = $parentState"
        )
        return min(min(mState, siblingState), parentState)

    }

    override fun addObserver(observer: CvLifecycleObserver) {
        enforceMainThreadIfNeeded("addObserver")

        val initialState = if (mState == State.DESTROYED) {
            State.DESTROYED
        } else {
            State.INITIALIZED
        }
        val statefulObserver = ObserverWithState(observer, initialState)
        val previous = mObserverMap.putIfAbsent(observer, statefulObserver)
        Log.d(TAG, "addObserver: $statefulObserver")
        Log.d(TAG, "addObserver: previous = $previous")
        if (previous != null) {
            return
        }
        val mLifecycleOwner = mLifecycleOwner.get() ?: return

        Log.d(
            TAG,
            "addObserver: mAddingObserverCounter = $mAddingObserverCounter    mHandlingEvent = $mHandlingEvent"
        )
        val isReentrance = (mAddingObserverCounter != 0 || mHandlingEvent)
        var targetState = calculateTargetState(observer)
        Log.d(TAG, "addObserver: targetState = $targetState")
        mAddingObserverCounter++
        while ((statefulObserver.mState < targetState
                    && mObserverMap.contains(observer))
        ) {
            pushParentState(statefulObserver.mState)
            val event = Event.upFrom(statefulObserver.mState) ?: return
            statefulObserver.dispatchEvent(mLifecycleOwner, event)
            popParentState()
            targetState = calculateTargetState(observer)
            Log.d(TAG, "addObserver: targetState2 = $targetState")
        }

        if (!isReentrance) {
            sync()
        }
        mAddingObserverCounter--
    }

    private fun popParentState() {
        mParentStates.removeAt(mParentStates.size - 1)
    }

    private fun pushParentState(state: State) {
        mParentStates.add(state)
    }

    override fun removeObserver(observer: CvLifecycleObserver) {
        enforceMainThreadIfNeeded("removeObserver")
        mObserverMap.remove(observer)
    }

    fun getObserverCount(): Int {
        enforceMainThreadIfNeeded("getObserverCount")
        return mObserverMap.size()
    }

    override fun getCurrentState(): State {
        return mState
    }

    private fun forwardPass(lifecycleOwner: CvLifecycleOwner) {
        val ascendingIterator = mObserverMap.iteratorWithAdditions()
        while (ascendingIterator.hasNext() && !mNewEventOccurred) {
            val entry = ascendingIterator.next()
            val observer = entry!!.value
            while ((observer.mState < mState && !mNewEventOccurred
                        && mObserverMap.contains(entry.key))
            ) {
                pushParentState(observer.mState)
                val event = Event.upFrom(observer.mState)
                    ?: throw IllegalStateException("no event up from " + observer.mState)

                observer.dispatchEvent(lifecycleOwner, event)
                popParentState()
            }
        }
    }

    private fun backwardPass(lifecycleOwner: CvLifecycleOwner) {
        val descendingIterator = mObserverMap.descendingIterator()

        while (descendingIterator.hasNext() && !mNewEventOccurred) {
            val entry = descendingIterator.next()
            Log.d(TAG, "backwardPass: entry = $entry   mNewEventOccurred = $mNewEventOccurred")
            val observer = entry?.value
            while ((observer!!.mState > mState && !mNewEventOccurred
                        && mObserverMap.contains(entry.key))
            ) {
                val event = Event.downFrom(observer.mState)
                    ?: throw IllegalStateException("no event down from " + observer.mState)
                pushParentState(event.targetState)
                observer.dispatchEvent(lifecycleOwner, event)
                popParentState()
            }
        }
    }

    private fun sync() {
        //获取生命周期载体
        val lifecycleOwner = mLifecycleOwner.get()
            ?: throw IllegalStateException(
                "LifecycleOwner of this LifecycleRegistry is already" +
                        "garbage collected. It is too late to change lifecycle state."
            )

        //判断是否同步
        while (!isSynced()) {
            mNewEventOccurred = false
            if (mState < mObserverMap.eldest()!!.value.mState) {
                backwardPass(lifecycleOwner)
            }
            val newest = mObserverMap.newest()
            if (!mNewEventOccurred && newest != null && mState > newest.value.mState) {
                forwardPass(lifecycleOwner)
            }
        }
        mNewEventOccurred = false
    }

    private fun enforceMainThreadIfNeeded(methodName: String) {
        if (mEnforceMainThread) {
            if (!CvArchTaskExecutor.getInstance().isMainThread()) {
                throw IllegalStateException("Method $methodName must be called on the main thread")
            }
        }
    }


    class ObserverWithState(
        observer: CvLifecycleObserver,
        var mState: State
    ) {

        private val mLifecycleObserver: CvLifecycleEventObserver =
            CvLifecycling.lifecycleEventObserver(observer)

        fun dispatchEvent(owner: CvLifecycleOwner, event: Event) {
            val newState = event.targetState
            mState = min(mState, newState)
            mLifecycleObserver.onStateChanged(owner, event)
            mState = newState

        }
    }
}