package cc.fastcv.jetpack.lifecycle

import android.view.View
import androidx.lifecycle.runtime.R

class ViewTreeLifecycleOwner private constructor() {

    companion object {
        fun set(view: View, lifecycleOwner: CvLifecycleOwner) {
            view.setTag(R.id.view_tree_lifecycle_owner, lifecycleOwner)
        }

        fun get(view: View): CvLifecycleOwner? {
            var found = view.getTag(R.id.view_tree_lifecycle_owner)
            if (found != null) return found as CvLifecycleOwner


            var parent = view.parent
            while (found == null && parent is View) {
                val parentView = parent as View
                found = view.getTag(R.id.view_tree_lifecycle_owner)
                parent = view.parent
            }

            return if (found != null) {
                found as CvLifecycleOwner
            } else {
                found
            }
        }
    }
}