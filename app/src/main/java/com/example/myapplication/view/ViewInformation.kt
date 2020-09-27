package com.example.myapplication.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_information.*

class ViewInformation : AppCompatActivity() {
    private lateinit var userInfoArray:Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)
        getInfo()
        setInfo()
    }

    private fun getInfo(){
        var email=intent.getStringExtra("email")
        var pwd=intent.getStringExtra("pwd")
        var nickname = intent.getStringExtra("nickname")
        var userBirth = intent.getStringExtra("birth")
        var userSex = intent.getStringExtra("sex")
        var agree = intent.getStringExtra("checkBox1")
        var marketingAgree = intent.getStringExtra("checkBox2")
        userInfoArray = arrayOf(email,pwd,nickname, userBirth, userSex,agree,marketingAgree)
    }

    @SuppressLint("SetTextI18n")
    private fun setInfo(){
        emailTextView.text = " 이메일 : " + userInfoArray[0]
        pwdTextView.text = "비밀번호 : " + userInfoArray[1]
        nickNameTextView.text =  "닉네임 : " + userInfoArray[2]
        birthTextView.text = "생년월일 : " + userInfoArray[3]
        userSexTextView.text =  "성별 : " + userInfoArray[4]
        agreeTextView.text =  "약관 동의사항 : " + userInfoArray[5]
        marketingAgreeTextView.text =  "마케팅 약관 동의사항 : " + userInfoArray[6]
    }
}