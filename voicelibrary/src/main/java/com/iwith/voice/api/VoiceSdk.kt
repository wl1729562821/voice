package com.iwith.voice.api

import android.content.Context
import com.iwith.voice.utils.saveVoiceBuildConfig
import com.iwith.voice.utils.setContext
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechUtility

object VoiceSdk{
    /**
     * 初始化科大讯飞模块
     */
    fun init(context: Context,appId:String):VoiceSdk{
        setContext(context)
        val param = StringBuffer()
        param.append("appid=" +appId)
        param.append(",")
        // 设置使用v5+
        param.append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC)
        SpeechUtility.createUtility(context, param.toString())
        return this
    }

    /**
     * 初始化科大讯飞模块
     */
    fun init(context: Context):VoiceSdk{
        return init(context,"59c2053c")
    }

    fun buildConfig(config:VoiceBuildConfig){
        saveVoiceBuildConfig(config)
    }

    class VoiceBuildConfig(var robotSn: String,
                           var robotId: String,
                           var shopId: String,
                           var language: String,
                           var datetime: String,
                           var nluVersion: String){

        constructor():this("","","","","","")
    }
}