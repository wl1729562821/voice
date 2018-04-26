package com.iwith.voice.listener

/**
 * @Description:语音识别事件接口
 * @Author: 钟宇涛
 * @Time: 2018/4/26 10:59
 **/
interface VoiceListener {

    /**
     *  语音开始识别监听事件
     */
    fun onStartVoice()

    /**
     *  语音识别结束
     */
    fun onEndVoice()

    /**
     * 系统级别错误
     * @param code  [cn.yc.vioce.config.ConfigCode]
     * @param msg  消息
     */

    fun onSystemError(code:Int,msg:String)

    /**
     * 常规错误
     * @param code  [cn.yc.vioce.config.ConfigCode]
     * @param msg  消息
     */
    fun onError(code: Int,msg: String)

    /**
     * 语言识别成功返回机器语言
     */
    fun onNext(value:String,machineValue:String)

    /**
     * 当前音量值
     */
    fun onVolumeChanged(current:Int)
}