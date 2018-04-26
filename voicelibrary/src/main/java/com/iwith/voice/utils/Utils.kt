package com.iwith.voice.utils

import android.content.Context
import android.util.Log
import com.iwith.voice.api.VoiceSdk
import com.google.gson.Gson
import com.iflytek.cloud.util.ResourceUtil
import org.json.JSONObject
import org.json.JSONTokener

private var mContext:Context?=null

internal fun setContext(context:Context){
    mContext=context
    saveVoiceBuildConfig(VoiceSdk.VoiceBuildConfig())
}

internal fun parseIatResult(json: String): String {
    val ret = StringBuffer()
    try {
        val tokener = JSONTokener(json)
        val joResult = JSONObject(tokener)

        val words = joResult.getJSONArray("ws")
        (0 until words.length())
                .map { // 转写结果词，默认使用第一个结果
                    words.getJSONObject(it).getJSONArray("cw")
                }
                .map { it.getJSONObject(0) }
                .forEach {
                    ret.append(it.getString("w"))
                    //				如果需要多候选结果，解析数组其他字段
                    //				for(int j = 0; j < items.length(); j++)
                    //				{
                    //					JSONObject obj = items.getJSONObject(j);
                    //					ret.append(obj.getString("w"));
                    //				}
                }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return ret.toString()
}

internal fun saveVoiceBuildConfig(config:VoiceSdk.VoiceBuildConfig){
    mContext?.getSharedPreferences("voice_data",Context.MODE_PRIVATE)?.apply {
        val json=Gson().toJson(config)
        edit().putString("voice_json",json).commit()
    }
}

 //获取发音人资源路径
 internal fun getResourcePath(context: Context): String {
    val tempBuffer = StringBuffer()
    //合成通用资源
    tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "tts/common.jet"))
    tempBuffer.append(";")
    //发音人资源
    tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "tts/" + "xiaoyan" + ".jet"))
    return tempBuffer.toString()
}

internal fun getVoiceBuildConfig():VoiceSdk.VoiceBuildConfig{
    var config=VoiceSdk.VoiceBuildConfig()
    mContext?.getSharedPreferences("voice_data",Context.MODE_PRIVATE)?.getString("voice_json","")?.apply {
        Log.e("Utils","getVoiceBuildConfig $this")
        config=Gson().fromJson<VoiceSdk.VoiceBuildConfig>(this,VoiceSdk.VoiceBuildConfig::class.java)
    }
    return config
}