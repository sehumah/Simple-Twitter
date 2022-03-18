package com.codepath.apps.restclienttemplate

import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat

class ScrollAwareFABBehavior : FloatingActionButton.Behavior() {
    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ): Boolean {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(
                    coordinatorLayout, child, directTargetChild, target,
                    nestedScrollAxes, type
                )
    }
}