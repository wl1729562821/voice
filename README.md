**使用步骤**
#### 1、初始化  VoiceSdk.init(this)
2、<com.iwith.voice.view.voice.VoiceView
		android:layout_alignParentBottom="true"
		android:layout_width="match_parent"
		android:layout_height="wrap_content"
		android:id="@+id/voice">

	</com.iwith.voice.view.voice.VoiceView>
3、初始化数据  VoiceSdk.buildConfig(VoiceSdk.VoiceBuildConfig("TR012112C700007","11508","157","cn","2018-04-25 12:29:59+0900","v1"))

4、设置语音合成播放开关 voiceView.setPlay(false)

5、设置回调接口 setVoiceListener(voiceListener: VoiceListener)