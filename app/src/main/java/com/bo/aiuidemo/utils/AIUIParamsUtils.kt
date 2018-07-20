package com.bo.aiuidemo.utils

import android.content.Context

/**
 * @author ex-yangjb001
 * @date 2018/7/17.
 */
class AIUIParamsUtils {
    companion object {

        fun getAIUIParams(context: Context): String {
            var params = ""
            val assetManager = context.resources.assets
            val ins = assetManager.open("cfg/aiui_phone.cfg")

            ins.buffered().reader().use {
                params = it.readText()
            }
            return params
        }
    }

}