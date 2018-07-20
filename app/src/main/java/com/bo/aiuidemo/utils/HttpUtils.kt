package com.bo.aiuidemo.utils

import android.util.Log
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author ex-yangjb001
 * @date 2018/7/18.
 */
object HttpUtils {
    private var retrofit: Retrofit? = null
    private fun createRetrofit(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder().client(
                    OkHttpClient.Builder()
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .cookieJar(object : CookieJar {
                                val cookieStore = HashMap<String, MutableList<Cookie>>()
                                override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
                                    cookieStore[url.host()] = cookies
                                }

                                override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
                                    return cookieStore[url.host()] ?: ArrayList<Cookie>()
                                }
                            })
                            .addInterceptor(HttpLoggingInterceptor { logger ->
                                Log.e("Https===>  ", logger)
                            }.setLevel(HttpLoggingInterceptor.Level.BODY)
                            )
                            .build()).baseUrl("https://gw.dev.cmrh.com:5001/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
        }
        return retrofit
    }

    fun createApiService(): ApiService? {
        val apiService = createRetrofit()?.create(ApiService::class.java)
        return apiService
    }

    fun getHeader(): Map<String, String> {
        val headers = HashMap<String, String>()
        headers["Content-Type"] = "application/json"
        return headers
    }

}