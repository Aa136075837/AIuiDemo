package com.bo.aiuidemo

import android.app.Application
import com.iflytek.cloud.SpeechUtility

/**
 * @author ex-yangjb001
 * @date 2018/7/17.
 */
class AIUIApp : Application(){
    override fun onCreate() {
        super.onCreate()
        SpeechUtility.createUtility(this, String.format("engine_start=ivw,delay_init=0,appid=%s","5b4c0631"))
    }
}