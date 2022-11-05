package cc.fastcv.jetpack.lifecycle

import androidx.annotation.MainThread

abstract class CvLifecycle {

    @MainThread
    abstract fun addObserver(observer: CvLifecycleObserver)

    @MainThread
    abstract fun removeObserver(observer: CvLifecycleObserver)

    @MainThread
    abstract fun getCurrentState(): State

    enum class Event {
        ON_CREATE,

        ON_START,

        ON_RESUME,

        ON_PAUSE,

        ON_STOP,

        ON_DESTROY,

        ON_ANY;

        val targetState: State
            get() {
                when (this) {
                    ON_CREATE, ON_STOP -> return State.CREATED
                    ON_START, ON_PAUSE -> return State.STARTED
                    ON_RESUME -> return State.RESUMED
                    ON_DESTROY -> return State.DESTROYED
                    ON_ANY -> {}
                }
                throw IllegalArgumentException("$this has no target state")
            }

        companion object {
            fun downFrom(state: State): Event? {
                return when (state) {
                    State.CREATED -> ON_DESTROY
                    State.STARTED -> ON_STOP
                    State.RESUMED -> ON_PAUSE
                    else -> null
                }
            }

            fun downTo(state: State): Event? {
                return when (state) {
                    State.DESTROYED -> ON_DESTROY
                    State.CREATED -> ON_STOP
                    State.STARTED -> ON_PAUSE
                    else -> null
                }
            }

            fun upFrom(state: State): Event? {
                return when (state) {
                    State.INITIALIZED -> ON_CREATE
                    State.CREATED -> ON_START
                    State.STARTED -> ON_RESUME
                    else -> null
                }
            }

            fun upTo(state: State): Event? {
                return when (state) {
                    State.CREATED -> ON_CREATE
                    State.STARTED -> ON_START
                    State.RESUMED -> ON_RESUME
                    else -> null
                }
            }
        }
    }

    enum class State {
        DESTROYED,

        INITIALIZED,

        CREATED,

        STARTED,

        RESUMED;

        fun isAtLeast(state: State): Boolean {
            return compareTo(state) >= 0
        }
    }

}