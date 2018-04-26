package com.iwith.voice.data

import com.iwith.voice.listener.HttpCallListener

internal interface RobotData {

    val mCallListener:HttpCallListener

    fun getMachineLanguage(text:String)
}