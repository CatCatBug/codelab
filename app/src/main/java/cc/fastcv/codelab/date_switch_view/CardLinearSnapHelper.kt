package cc.fastcv.codelab.date_switch_view

import android.view.View
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * @创建者 mingyan.su
 * @创建时间 2018/9/6 16:42
 * @类描述 防止卡片在第一页和最后一页因无法"居中"而一直循环调用onScrollStateChanged-->SnapHelper.snapToTargetExistingView-->onScrollStateChanged
 */
class CardLinearSnapHelper : LinearSnapHelper() {

    var mNoNeedToScroll : Boolean = false

    override fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager, targetView: View): IntArray? {
        return if (mNoNeedToScroll) {
            intArrayOf(0, 0)
        } else {
            super.calculateDistanceToFinalSnap(layoutManager, targetView)
        }
    }
}