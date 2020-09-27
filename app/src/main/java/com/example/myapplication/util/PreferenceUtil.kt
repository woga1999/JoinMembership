package com.example.myapplication.util

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context : Context) {
    //어디서든 전역적으로 사용하기 위해 sharedPreference 싱글톤 패턴 이용
    private  val prefs:SharedPreferences = context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String): String{
        return prefs.getString(key, defValue).toString()
    }
    fun setString(key:String, str:String){
        prefs.edit().putString(key,str).apply()
    }
}