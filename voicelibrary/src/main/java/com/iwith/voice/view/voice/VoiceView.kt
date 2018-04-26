package com.iwith.voice.view.voice

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import cn.yc.vioce.R
import kotlinx.android.synthetic.main.view_voice.view.*
import android.animation.ObjectAnimator
import android.animation.AnimatorSet
import android.animation.TimeInterpolator
import android.os.*
import android.util.Log
import android.view.MotionEvent
import android.view.animation.*
import com.iwith.voice.api.VoiceSdk
import com.iwith.voice.listener.VoiceListener
import com.iwith.voice.service.VoiceService
import com.iwith.voice.service.impl.VoiceServiceImpl


class VoiceView : LinearLayout {

    private val TAG="VoiceView"

    internal inner class DefaultHandler:Handler(){
        override fun handleMessage(msg: Message?) {

        }
    }

    internal inner class DefaultTask:Runnable{
        override fun run() {
            Log.e("Activity","run")
            if(mCurrentTime<0){
                return
            }
            mCurrentTime++
            setTime()
            mHanlder.postDelayed(mTask,1000)
        }
    }

    private var mTask=DefaultTask()
    private val mHanlder=DefaultHandler()

    private var mContentView:View?=null
    private var mStart=false
    private var mCurrentTime=0

    private var mServiceListener:VoiceListener=object : VoiceListener{
        override fun onError(code: Int, msg: String) {
        }

        override fun onStartVoice() {

        }

        override fun onSystemError(code: Int, msg: String) {

        }

        override fun onEndVoice() {

        }

        override fun onVolumeChanged(current: Int) {

        }

        override fun onNext(value: String, machineValue: String) {
        }
    }
    private val mService:VoiceService=VoiceServiceImpl(context,mServiceListener)

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init(){
        VoiceSdk.init(context)
        mContentView=mContentView?:LayoutInflater.from(context).inflate(R.layout.view_voice,null,false).apply {
            mCurrentTime=-1
            val set=AnimatorSet()
            val animY = ObjectAnimator.ofFloat(voice_img_parent, "scaleY", 1f,0.85f,1f)
            val animX = ObjectAnimator.ofFloat(voice_img_parent, "scaleX", 1f,0.85f,1f)
            animX.addUpdateListener {
                //Log.e("Activity","update ${it.animatedValue}")
            }
            set.duration=500
            set.interpolator = AccelerateDecelerateInterpolator() as TimeInterpolator?

            set.play(animX).with(animY)
            voice_img_parent?.setOnTouchListener { v, event ->
                var touch=false
                when(event.action){
                    MotionEvent.ACTION_DOWN->{
                        mService.cancelSynthesisVoice()
                        touch=true
                        //点击语音识别
                        voice_title?.visibility=View.INVISIBLE
                        voice_time_parent?.visibility=View.VISIBLE
                        set.start()
                        mCurrentTime=0
                        mCurrentTime++
                        setTime()
                        mService.startVoice()
                        mHanlder.postDelayed(mTask,1000)
                    }
                    MotionEvent.ACTION_UP->{
                        voice_title?.visibility=View.VISIBLE
                        voice_time_parent?.visibility=View.INVISIBLE
                        mStart=false
                        touch=false
                        mCurrentTime=-1
                        //mService.cancelVoice()
                        //mService.cancelVoice()
                    }
                }
                touch
            }
            voice_img_parent?.setOnClickListener {

            }
        }
        mContentView?.apply {
            if(parent==null){
                addView(this)
            }
        }
    }

    private fun setTime(){
        mContentView?.voice_tv_time?.text=if(mCurrentTime<10){
            "00:0$mCurrentTime"
        }else{
            "00:$mCurrentTime"
        }
    }

    /**
     * 设置回调接口
     */
    fun setVoiceListener(voiceListener: VoiceListener){
        this.mServiceListener=voiceListener
        mService.setListener(mServiceListener)
    }

    /**
     * 设置语音是否播放
     */
    fun setPlay(play:Boolean){
        mService.mPlay=play
    }

    /**
     * 清除数据，初始化状态
     */
    fun onStop(){
        VoiceSdk.buildConfig(VoiceSdk.VoiceBuildConfig())
        mService.stopVoice()
        mService.cancelVoice()
    }

}






















