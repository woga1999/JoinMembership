package com.example.myapplication.app

import android.app.Application
import com.example.myapplication.util.PreferenceUtil

class UserStorage: Application() {
    //다른 액티비티보다 먼저 생성되어야 다른 곳에 데이터를 넘겨줄 수 있기 때문에 application 클래스 생성
    companion object{
        lateinit var prefs : PreferenceUtil
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate()
    }
}