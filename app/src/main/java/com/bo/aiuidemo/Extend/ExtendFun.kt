package com.bo.aiuidemo.Extend

import android.content.Context
import android.support.annotation.IdRes
import android.util.SparseArray
import android.view.View
import android.widget.Toast

/**
 * @author ex-yangjb001
 * @date 2018/7/19.
 */
fun Context.toast(msg: CharSequence, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, msg, duration).show()

fun <T : View> View.findViewOften(@IdRes viewId: Int): T {
    var viewHolder: SparseArray<View> = tag as? SparseArray<View> ?: SparseArray()
    tag = viewHolder
    var childView: View? = viewHolder.get(viewId)
    if (null == childView) {
        childView = findViewById(viewId)
        viewHolder.put(viewId, childView)
    }
    return childView as T
}