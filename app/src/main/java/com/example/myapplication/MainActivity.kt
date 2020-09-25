package com.example.myapplication

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    private lateinit var inputDataArray: Array<EditText>
    private lateinit var textInputLayoutArray: Array<TextInputLayout>
    private var isCorrectArray: Array<Boolean> = arrayOf(false,false,false,false,false,false,false)
    private var isDone = false
    internal val viewDisposables = CompositeDisposable() // 메모리 누수를 막기 위한 클래스

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        inputData()
        setJoin()
    }
    private fun init() {
        inputDataArray = arrayOf(editEmail, editPwd, editCheckPwd, editName)
        textInputLayoutArray = arrayOf(editEmailLayout, editPwdLayout, editCheckPwdLayout, editNameLayout)

    }

    private fun setJoin(){
        btnJoin.isEnabled = isDone
    }

    private fun inputData(){
        val inputEmail = RxTextView.textChanges(inputDataArray[0])
            .map { Pattern.matches(Constant.regexEmail, inputDataArray[0].text.toString())}
            .subscribe({
                isCorrectArray[0] = it
                displayMsg(0,it)
            }){
                //Error Block
            }
    }

    private fun displayMsg(index:Int, check:Boolean){
        textInputLayoutArray[index].isErrorEnabled = !check
        if(!check){
            textInputLayoutArray[index].error = Constant.emailErrorMessage
        }
    }

    override fun onStop() {
        super.onStop()
        viewDisposables.clear()
    }
}
