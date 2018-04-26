package com.iwith.voice.data.impl

import com.iwith.voice.bean.MachineRequestBean
import com.iwith.voice.bean.MachineResultBean
import com.iwith.voice.data.RobotData
import com.iwith.voice.data.api.HttpApi
import com.iwith.voice.listener.HttpCallListener
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


internal class RobotDataImpl(override var mCallListener: HttpCallListener):RobotData{

    private val TAG="RobotDataImpl"

    private var mCall:Call<JsonObject>?=null

    override fun getMachineLanguage(text:String) {
        val client = OkHttpClient.Builder()
                .connectTimeout(10000L,TimeUnit.MILLISECONDS)       //设置连接超时
                .readTimeout(10000L,TimeUnit.MILLISECONDS)          //设置读取超时
                .writeTimeout(10000L,TimeUnit.MILLISECONDS)         //设置写入超时
                .addInterceptor {
                    val original = it.request()
                    val request = original.newBuilder()
                            .header("Content-Type", "application/json")
                            .header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOjI5MCwiaXNzIjoiaXdpdGgiLCJ1c2VyVHlwZSI6NiwiZXhwIjoxNTI0ODg2MTE3LCJpYXQiOjE1MjQ2MjY5MTcsImp0aSI6IjVjZDcxOTI2ZTgxMzQ1NmQ4OTU5OWY3OThkNzQ4ZmJiIn0.Pr9dfj1G7uoryGDaZdY-er8t12SOplkxJfduBNxSbao")
                            .method(original.method(), original.body())
                            .build()
                    it.proceed(request)
                }
                .build()

        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl("http://jp.52iwith.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val bean=MachineRequestBean()
        bean.text=text
        val json=Gson().toJson(bean)
        var body=RequestBody.create(okhttp3.MediaType.parse("application/json"),json)
        val service = retrofit.create<HttpApi>(HttpApi::class.java)
        mCall=service.getMachineLanguage(body)
        val error:()->Unit={
            mCallListener.onError("获取机器语言识别,请稍后再试")
        }
        mCall?.enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                error()
            }

            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                val string=response?.body()?.toString()?:""
                if(response?.code() == 200 && string.isNotBlank()){
                    val bean=Gson().fromJson<MachineResultBean>(string,MachineResultBean::class.java)
                    val  value=bean.entry?.get(0)?.parameters?.text?:""
                    if(bean.entry?.isNotEmpty()==true && value.isNotEmpty()){
                        mCallListener.onNext(text,value)
                    }else{
                        error()
                    }
                }else{
                    error()
                }
            }
        })
    }
}