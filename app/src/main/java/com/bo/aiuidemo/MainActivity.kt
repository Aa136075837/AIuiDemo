package com.bo.aiuidemo

import android.Manifest
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.bo.aiuidemo.Extend.toast
import com.bo.aiuidemo.model.*
import com.bo.aiuidemo.utils.AIUIParamsUtils
import com.bo.aiuidemo.utils.HttpUtils
import com.iflytek.aiui.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.nio.charset.Charset

/**
 * @author ex-yangjb001
 * @date 2018/7/16.
 */
class MainActivity : AppCompatActivity() {
    var TAG = "MainActivity"
    var mAgent: AIUIAgent? = null
    var mCuttentState = AIUIConstant.STATE_IDLE
    val mAgentListener = AIUIListener {

        when (it.eventType) {
            AIUIConstant.EVENT_WAKEUP -> {
                Log.e(TAG, "EVENT_WAKEUP    info = " + it.info + "   " + it.arg1)
            }
            AIUIConstant.EVENT_SLEEP -> {
                Log.e(TAG, "EVENT_SLEEP   " + it.info + "   " + it.arg1)
            }
            AIUIConstant.EVENT_STATE -> {
                Log.e(TAG, "EVENT_STATE   " + it.info + "   " + it.arg1)
            }
            AIUIConstant.EVENT_RESULT -> {
                Log.e(TAG, "EVENT_RESULT   " + it.info + "   " + it.arg1)
                handResult(it)
            }
            AIUIConstant.EVENT_TTS -> {
                Log.e(TAG, "EVENT_TTS   " + it.info + "   " + it.arg1)
            }
            AIUIConstant.EVENT_ERROR -> {
                Log.e(TAG, "EVENT_ERROR   " + it.info + "   " + it.arg1)
            }
            AIUIConstant.EVENT_CMD_RETURN -> {
                Log.e(TAG, "EVENT_CMD_RETURN   " + it.info + "   " + it.arg1)
            }
            AIUIConstant.EVENT_CONNECTED_TO_SERVER -> {
                Log.e(TAG, "EVENT_CONNECTED_TO_SERVER   " + it.info + "   " + it.arg1)
            }
            AIUIConstant.EVENT_VAD -> {
                Log.e(TAG, "EVENT_VAD   " + it.info + "   " + it.arg1)
            }
        }

    }

    private fun handResult(it: AIUIEvent?) {
        val baseContent = JSONObject(it?.info)
        val data = baseContent.getJSONArray("data").getJSONObject(0)
        val params = data.getJSONObject("params")
        val content = data.getJSONArray("content").getJSONObject(0)
        if (content.has("cnt_id")) {
            val cnt_id = content.getString("cnt_id")
            val cntJson = JSONObject(String(it?.data!!.getByteArray(cnt_id), Charset.forName("utf-8")))
            val sub = params.optString("sub")
            val result = cntJson.optJSONObject("intent")
            if ("nlp" == sub && result.length() > 2) {
                val str: String?
                if (result.optInt("rc") == 0) {
                    val answer = result.optJSONObject("answer")
                    if (answer == null) {
                        val semantic = result.getJSONArray("semantic")
                        val qContent = semantic.getJSONObject(0)
                        val slotsArray = qContent.getJSONArray("slots")
                        var date: String = ""
                        var count: String = ""
                        for (i in 0..(slotsArray.length() - 1)) {
                            val value = slotsArray.getJSONObject(i).getString("value")
                            val normValue = slotsArray.getJSONObject(i).getString("normValue")
                            content_tv.append("\n")
                            content_tv.append("参数${i}  : " + value)
                            when (i) {
                                0 -> {
                                    date = JSONObject(normValue).getString("suggestDatetime")
                                }
                                1 -> {
                                    count = normValue
                                }
                            }
                        }
                        applyLeave(date, count)
                        str = ""
                    } else {
                        str = answer.optString("text")
                    }
                } else {
                    str = "无法识别"
                }
                content_tv.append("\n")
                content_tv.append(str)
            }
        }

    }

    private fun applyLeave(date: String, count: String) {
        stopTTs()
        stopRecord()
        HttpUtils.createApiService()!!.applyForLeave(HttpUtils.getHeader(), ApplyForLeaveReq("465798",
                "AI 语音请假测试", "休假申请", date, 90,
                mutableListOf(LeaveTime("\"2018-07-29 09:00:00\"", "\"2018-07-29 18:00:00\"")), count
                , "上午", 10, "17688458611", "ader"))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : Observer<BaseResponse<Param>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(t: BaseResponse<Param>) {
                        toast(t.param.applyForLeave.flag)
                        Log.e(TAG + "onNext", t.flag)
                    }

                    override fun onError(e: Throwable) {
                        toast(e.message!!)
                        Log.e(TAG + "onError", e.message)
                    }

                })
    }

    private fun login() {
        HttpUtils.createApiService()!!
                .login(LoginReq("login", LoginReqParams("9F97E500-DDEE-4018-AD91-96617C6E1653", "login",
                        "123456", "465798")))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : Observer<LoginBean> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(t: LoginBean) {
                        toast(t.message)
                    }

                    override fun onError(e: Throwable) {
                        Log.e(TAG + "login onError", e.message)
                    }
                })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        applyermission()

    }

    override fun onResume() {
        super.onResume()
        login()
    }

    private fun applyermission() {
        val listener = object : MultiplePermissionsListener {
            override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {

            }

            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report?.areAllPermissionsGranted()!!) {

                }
                //所有权限通过，初始化界面
                onPermissionChecked()
            }
        }
        Dexter.withActivity(this).withPermissions(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.ACCESS_NETWORK_STATE
        ).withListener(listener).check()
    }

    @CallSuper
    private fun onPermissionChecked() {
        initEvents()
        val params = AIUIParamsUtils.getAIUIParams(applicationContext)
        mAgent = AIUIAgent.createAgent(this, params, mAgentListener)
    }

    private fun initEvents() {
        record.setOnTouchListener(View.OnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    toast("down")
                    weakUp()
                    stopTTs()
                    startRecord()
                }
                MotionEvent.ACTION_UP -> {
                    toast("up")
                }
            }
            return@OnTouchListener true
        })
        record_cancel.setOnClickListener { v -> stopRecord() }
    }

    fun stopTTs() {
        sendMessage(AIUIMessage(AIUIConstant.CMD_TTS, 0, 0, "", null))
    }

    fun weakUp() {
        sendMessage(AIUIMessage(AIUIConstant.CMD_WAKEUP, 0, 0, "", null))
    }

    fun stopRecord() {
        sendMessage(AIUIMessage(AIUIConstant.CMD_STOP_RECORD, 0, 0, "data_type=audio,sample_rate=16000", null))
        content_tv.append("\n")
        content_tv.append("stop")
    }

    fun startRecord() {
        sendMessage(AIUIMessage(AIUIConstant.CMD_START_RECORD, 0, 0, "data_type=audio,sample_rate=16000", null))
        content_tv.append("\n")
        content_tv.append("start")
    }

    fun sendMessage(msg: AIUIMessage) {
        mAgent?.sendMessage(msg)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRecord()
    }

}
