package com.iwith.voice.listener

internal interface HttpCallListener {
    fun onNext(value:String,machineValue:String)

    fun onError(error:String)
}