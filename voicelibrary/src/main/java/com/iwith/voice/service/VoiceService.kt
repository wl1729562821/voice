package com.iwith.voice.service

import android.content.Context
import com.iwith.voice.listener.VoiceListener
import com.iflytek.cloud.SpeechRecognizer
import com.iflytek.cloud.SpeechSynthesizer

internal interface VoiceService{

    val mSr: SpeechRecognizer
    val mTts: SpeechSynthesizer
    val mContext:Context
    var mListener:VoiceListener
    var mPlay:Boolean

    fun setListener(voiceListener: VoiceListener){
        mListener=voiceListener
    }

    fun startVoice()
    fun stopVoice()
    fun cancelVoice()

    /**
     * 开始语音合成
     */
    fun startSynthesisVoice(text:String)

    /**
     * 停止语音合成
     */
    fun stopSynthesisVoice()

    /**
     * 取消语音合成
     */
    fun cancelSynthesisVoice()
}