package com.bo.aiuidemo.utils

import android.content.Context
import android.net.ConnectivityManager

/**
 * @author ex-yangjb001
 * @date 2018/7/18.
 */
object NetUtils {
    /**
     * 没有网络连接
     */
    val NETSTATE_NONE = 0
    /**
     * 连接wifi
     */
    val NETSTATE_WIFI = 1
    /**
     * 移动网络连接
     */
    val NETSTATE_MOBILE = 2

    fun getNetStatus(context: Context): Int {
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivity.activeNetworkInfo
        if (info.isConnected) {
            when (info.type) {
                ConnectivityManager.TYPE_MOBILE -> {
                    return NETSTATE_MOBILE
                }
                ConnectivityManager.TYPE_WIFI -> {
                    return NETSTATE_WIFI
                }
            }
        }
        return NETSTATE_NONE
    }

    fun isNetConnected(context: Context): Boolean {
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivity.activeNetworkInfo
        return info.isConnected
    }
}