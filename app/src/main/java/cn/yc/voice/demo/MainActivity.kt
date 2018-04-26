package cn.yc.voice.demo

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.Window
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.Toast
import com.iwith.voice.api.VoiceSdk
import com.iwith.voice.listener.VoiceListener
import cn.yc.voice.R
import cn.yc.voice.log.Log
import kotlinx.android.synthetic.main.main.*
import cn.yc.voice.log.LogFragment
import cn.yc.voice.log.MessageOnlyLogFilter
import cn.yc.voice.log.LogWrapper




class MainActivity : Activity(), OnClickListener {

    private val TAG="MainActivity"

    private var mToast: Toast? = null


    //Menu 列表
    internal var items = arrayOf("立刻体验语音听写", "立刻体验语法识别", "立刻体验语义理解", "立刻体验语音合成", "立刻体验语音唤醒", "立刻体验声纹密码")

    @SuppressLint("ShowToast")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置标题栏（无标题）
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.main)
        fragmentManager.beginTransaction()?.apply {
            add(R.id.fm,LogFragment())
            commit()
        }
        requestPermissions()
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT)
        val listitemAdapter = SimpleAdapter()
        //(findViewById<View>(R.id.listview_main) as ListView).adapter = listitemAdapter

        VoiceSdk.buildConfig(VoiceSdk.VoiceBuildConfig("TR012112C700007","11508","157",
        "cn","2018-04-25 12:29:59+0900","v1"))
        voice?.setVoiceListener(object : VoiceListener {
            override fun onError(code: Int, msg: String) {
                Log.e(TAG,"onError $code  $msg")
            }

            override fun onStartVoice() {
                Log.e(TAG,"开始播放")
            }

            override fun onSystemError(code: Int, msg: String) {

            }

            override fun onEndVoice() {
                Log.e(TAG,"结束播放")
            }

            override fun onVolumeChanged(current: Int) {
                //Log.e(TAG,"音量值 $current")
            }

            override fun onNext(value: String, machineValue: String) {
                Log.e(TAG,"onNext $value  $machineValue")
                tv2?.text=value
                tv4?.text=machineValue
            }
        })
    }

    override fun onStart() {
        super.onStart()
        val logWrapper = LogWrapper()
        Log.setLogNode(logWrapper)

        val msgFilter = MessageOnlyLogFilter()
        logWrapper.next = msgFilter

        val logFragment = fragmentManager
                .findFragmentById(R.id.fm) as LogFragment
        msgFilter.next = logFragment.logView
    }

    override fun onClick(view: View) {
        val tag = Integer.parseInt(view.tag.toString())
        var intent: Intent? = null
        when (tag) {
            0 ->
                // 语音转写
                intent = Intent(this, IatDemo::class.java)
            1 ->
                // 语法识别
                intent = Intent(this, AsrDemo::class.java)
            2 ->
                // 语义理解
                intent = Intent(this, UnderstanderDemo::class.java)
            3 ->
                // 合成
                intent = Intent(this, TtsDemo::class.java)
            4 ->
                // 唤醒
                intent = Intent(this, IvwActivity::class.java)
            5 ->
                // 声纹
                showTip("请登录：http://www.xfyun.cn/ 下载体验吧！")
            else -> showTip("此功能将于近期开放，敬请期待。")
        }
        if (intent != null) {
            startActivity(intent)
        }
    }

    private inner class SimpleAdapter : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            if (null == convertView) {
                val factory = LayoutInflater.from(this@MainActivity)
                val mView = factory.inflate(R.layout.list_items, null)
                convertView = mView
            }
            val btn = convertView!!.findViewById<View>(R.id.btn) as Button
            btn.setOnClickListener(this@MainActivity)
            btn.tag = position
            btn.text = items[position]
            return convertView
        }

        override fun getCount(): Int {
            return items.size
        }

        override fun getItem(position: Int): Any? {
            return null
        }

        override fun getItemId(position: Int): Long {
            return 0
        }
    }

    private fun showTip(str: String) {
        runOnUiThread {
            mToast!!.setText(str)
            mToast!!.show()
        }
    }


    private fun requestPermissions() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.LOCATION_HARDWARE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_SETTINGS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_CONTACTS), 0x0010)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        voice?.onStop()
        super.onDestroy()
    }
}
