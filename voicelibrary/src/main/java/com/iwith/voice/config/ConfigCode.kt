package com.iwith.voice.config

object ConfigCode{
    /**
     * 初始化科大讯飞引擎失败
     */
    const val ERROR_INIT_CODE=0

    /**
     * 设置科大讯飞事件返回值错误
     */
    const val ERROR_VOICE_RETURN_CODE=1

    /**
     * 语音合成返回失败
     */
    const val ERROR_VOICE_SYNTHESISVOICE_CODE=1

    /**
     * 语音识别出来的是空字符串
     */
    const val ERROR_VOICE_EMPTY_CODE=3

    /**
     * 请求机器语言识别
     */
    const val ERROR_VOICE_HTTP_ERROR_CODE=4
}