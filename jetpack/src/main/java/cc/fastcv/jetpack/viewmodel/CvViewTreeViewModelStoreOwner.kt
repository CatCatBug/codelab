package cc.fastcv.jetpack.viewmodel

import android.view.View

class CvViewTreeViewModelStoreOwner {

    companion object {
        fun set(view: View, viewModelStoreOwner: CvViewModelStoreOwner?) {
            view.setTag(androidx.lifecycle.viewmodel.R.id.view_tree_view_model_store_owner, viewModelStoreOwner)
        }

        fun get(view: View): CvViewModelStoreOwner? {
            var found = view.getTag(
                androidx.lifecycle.viewmodel.R.id.view_tree_view_model_store_owner
            ) as CvViewModelStoreOwner
            if (found != null) return found
            var parent = view.parent
            while (found == null && parent is View) {
                val parentView = parent as View
                found =
                    parentView.getTag(androidx.lifecycle.viewmodel.R.id.view_tree_view_model_store_owner) as CvViewModelStoreOwner
                parent = parentView.parent
            }
            return found
        }
    }
}