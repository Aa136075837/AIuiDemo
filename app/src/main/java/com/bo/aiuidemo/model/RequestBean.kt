package com.bo.aiuidemo.model

import com.google.gson.annotations.SerializedName

/**
 * @author ex-yangjb001
 * @date 2018/7/18.
 */
data class LoginReq(var action: String, var params: LoginReqParams)

data class LoginReqParams(@SerializedName("RF—DEVICE-ID") var RFDEVICEID: String, var action: String, var password: String, var username: String)

data class ApplyForLeaveReq(
        var umAdid: String,
        var applyContent: String,
        var applyTitle: String,
        var date: String,
        var flowTemplType: Int,
        var leaveProcess: List<LeaveTime>,
        var time: String,
        var timeDesc: String,
        var vacationNo: Int,
        var contactTel: String,
        var contactAddress: String
)

data class LeaveTime(var startTime1: String, var endTime1: String)