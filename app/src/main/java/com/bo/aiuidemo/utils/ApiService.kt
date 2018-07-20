package com.bo.aiuidemo.utils

import com.bo.aiuidemo.model.*
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

/**
 * @author ex-yangjb001
 * @date 2018/7/18.
 */
interface ApiService {

    @POST("login")
    fun login(@Body bean: LoginReq): Observable<LoginBean>

    @POST("RH_MAS/bus/v1.0/ams/applyForLeave")
    fun applyForLeave(@HeaderMap headers: Map<String, String>, @Body bean: ApplyForLeaveReq): Observable<BaseResponse<Param>>
}