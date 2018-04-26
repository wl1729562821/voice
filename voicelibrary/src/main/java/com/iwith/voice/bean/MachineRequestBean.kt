package com.iwith.voice.bean

import com.iwith.voice.utils.getVoiceBuildConfig
import com.google.gson.annotations.SerializedName

internal class MachineRequestBean {
    /**
     * robotSn : TR012112C700007
     * robotId : 11508
     * shopId: : 157
     * language : cn
     * datetime : 2018-04-25 12:29:59+0900
     * nluVersion : v1
     * text : 你好
     */

    var build= getVoiceBuildConfig()
    var robotSn: String = build.robotSn
    var robotId: String = build.robotId
    @SerializedName("shopId:")
    var `_$ShopId60`: String = build.shopId
    var language: String = build.language
    var datetime: String = build.datetime
    var nluVersion: String = build.nluVersion
    var text: String = ""
    override fun toString(): String {
        return "MachineRequestBean(robotSn='$robotSn', robotId='$robotId', `_ShopId60`='$`_$ShopId60`', language='$language', datetime='$datetime', nluVersion='$nluVersion', text='$text')"
    }


}