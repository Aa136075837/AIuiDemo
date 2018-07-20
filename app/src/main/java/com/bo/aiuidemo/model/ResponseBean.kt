package com.bo.aiuidemo.model

import com.google.gson.annotations.SerializedName

/**
 * @author ex-yangjb001
 * @date 2018/7/18.
 */
data class LoginBean(var code: String, var message: String, var userId: String, var cookies: LoginCookie)

data class LoginCookie(@SerializedName("rf-proxy-staff-prd-user-id") var prdUserId: String,

                       @SerializedName("PROXY-SESSION-staff-prd") var staffPrd: String)

data class BaseResponse<T>(
        @SerializedName("param") val param: T,
        @SerializedName("version") val version: String,
        @SerializedName("flag") val flag: String,
        @SerializedName("method") val method: String,
        @SerializedName("message") val message: String,
        @SerializedName("type") val type: String
)

data class Param (
        @SerializedName("applyForLeave") val applyForLeave: ApplyForLeave
)

data class ApplyForLeave(
        @SerializedName("msg") val msg: Any,
        @SerializedName("flag") val flag: String,
        @SerializedName("flowId") val flowId: String
)