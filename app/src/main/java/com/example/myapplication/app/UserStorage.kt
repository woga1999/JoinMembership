package com.example.myapplication.app

import android.app.Application
import com.example.myapplication.util.PreferenceUtil

class UserStorage: Application() {
    companion object{
        lateinit var prefs : PreferenceUtil
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate()
    }
}