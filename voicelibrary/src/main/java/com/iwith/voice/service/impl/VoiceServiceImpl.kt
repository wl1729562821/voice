package com.iwith.voice.service.impl

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.iwith.voice.config.ConfigCode
import com.iwith.voice.data.impl.RobotDataImpl
import com.iwith.voice.listener.HttpCallListener
import com.iwith.voice.listener.VoiceListener
import com.iwith.voice.service.VoiceService
import com.iwith.voice.utils.getResourcePath
import com.iwith.voice.utils.parseIatResult
import com.iflytek.cloud.*
import com.iflytek.cloud.util.ResourceUtil

internal class VoiceServiceImpl(override val mContext: Context,
                       override var mListener: VoiceListener,
                       override var mSr: SpeechRecognizer,
                                override var mTts: SpeechSynthesizer,
                                override var mPlay: Boolean):VoiceService{

    private val TAG="VoiceServiceImpl"
    private val mRecognizerListener=DefaultRecognizerListener()
    private val mHttpCallListener=DefaultHttpCallListener()

    private val mData=RobotDataImpl(mHttpCallListener)


    internal inner class DefaultHttpCallListener:HttpCallListener{
        override fun onError(error: String) {
            mListener.onError(ConfigCode.ERROR_VOICE_HTTP_ERROR_CODE,error)
        }

        override fun onNext(value: String, machineValue: String) {
            mListener.onNext(value,machineValue)
            //语音播放开关
            if(mPlay){
                startSynthesisVoice(machineValue)
            }
        }
    }

    internal inner class DefaultRecognizerListener:RecognizerListener {

        private var mValue=""

        override fun onResult(p0: RecognizerResult?, isLast: Boolean) {
            Log.e(TAG, "result:" + p0?.resultString+";$isLast   $mValue")
            mValue+= parseIatResult(p0?.resultString?:"")
            if(isLast){
                //如果识别文字不为空那么翻译
                if(mValue.isNotBlank()){
                    mData.getMachineLanguage(mValue)
                }else{
                    mListener.onError(ConfigCode.ERROR_VOICE_EMPTY_CODE,"语音识别错误，空字符串")
                }
                mValue=""
            }
        }

        override fun onError(p0: SpeechError?) {
            Log.e(TAG, "error:" + p0?.getPlainDescription(true))
        }

        override fun onBeginOfSpeech() {
            mValue=""
            mListener.onStartVoice()
        }

        override fun onVolumeChanged(p0: Int, p1: ByteArray?) {
            mListener.onVolumeChanged(p0)
        }

        override fun onEndOfSpeech() {
            mListener.onEndVoice()
        }

        override fun onEvent(p0: Int, p1: Int, p2: Int, p3: Bundle?) {

        }
    }

    constructor(context: Context,listener: VoiceListener):this(context,listener,
            SpeechRecognizer.createRecognizer(context,null),
            SpeechSynthesizer.createSynthesizer(context,null),
            true)

    init {
        mSr=SpeechRecognizer.createRecognizer(mContext,InitListener {
            if (it != ErrorCode.SUCCESS) {
                mListener.onSystemError(ConfigCode.ERROR_INIT_CODE,"语音识别初始化失败，错误码" + it)
            }
        })
        mSr.apply {
            setParameter( SpeechConstant.CLOUD_GRAMMAR, null )
            setParameter( SpeechConstant.SUBJECT, null )
            setParameter( SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD )
        }

        mTts=SpeechSynthesizer.createSynthesizer(mContext,{
            if (it != ErrorCode.SUCCESS) {
                mListener.onSystemError(ConfigCode.ERROR_INIT_CODE,"语音合成初始化失败，错误码" + it)
            }
        })
        mTts.apply {
            setParameter(SpeechConstant.PARAMS, null)
            //设置使用本地引擎
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL)
            //设置发音人资源路径
            mTts.setParameter(ResourceUtil.TTS_RES_PATH, getResourcePath(mContext))
            //设置发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, "")
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED,"50")
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, "50")
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, "50")
            //设置播放器音频流类型
            mTts.setParameter(SpeechConstant.STREAM_TYPE, "3")

            // 设置播放合成音频打断音乐播放，默认为true
            mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true")

            // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
            // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
            mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav")
            mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory().toString() + "/msc/tts.wav")
        }
    }

    override fun startVoice() {
        //听写监听器
        val code=mSr.startListening(mRecognizerListener)
        if(ErrorCode.SUCCESS!=code){
            mListener.onError(ConfigCode.ERROR_VOICE_RETURN_CODE,"识别失败,错误码:"+code)
        }
    }

    override fun startSynthesisVoice(text: String) {
        val code = mTts.startSpeaking(text,object :SynthesizerListener{
            override fun onBufferProgress(p0: Int, p1: Int, p2: Int, p3: String?) {

            }

            override fun onCompleted(p0: SpeechError?) {

            }

            override fun onEvent(p0: Int, p1: Int, p2: Int, p3: Bundle?) {

            }

            override fun onSpeakBegin() {

            }

            override fun onSpeakPaused() {

            }

            override fun onSpeakProgress(p0: Int, p1: Int, p2: Int) {

            }

            override fun onSpeakResumed() {

            }
        })
        if (code != ErrorCode.SUCCESS) {
            mListener.onError(ConfigCode.ERROR_VOICE_SYNTHESISVOICE_CODE,"语音合成失败,错误码:"+code)
        }
    }

    override fun stopSynthesisVoice() {
        mTts.pauseSpeaking()
    }

    override fun cancelSynthesisVoice() {
        mTts.stopSpeaking()
    }

    override fun stopVoice() {
        mSr.stopListening()
    }

    override fun cancelVoice() {
        mSr.cancel()
    }
}