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
    private lateinit var isCorrectArray: Array<Boolean>
    private var isDone = false
    internal val viewDisposables = CompositeDisposable() // 메모리 누수를 막기 위한 클래스
    private lateinit var errorMsgArray:Array<String>

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
        isCorrectArray  = arrayOf(false,false,false,false,false,false,false)
        errorMsgArray  = arrayOf(Constant.emailErrorMessage,  Constant.pwdErrorMessage, Constant.checkPwdErrorMessage, Constant.nickNameErrorMessage)

    }

    private fun setJoin(){
        btnJoin.isEnabled = isDone
    }

    private fun inputData(){
        val inputEmail = RxTextView.textChanges(inputDataArray[0])
            .map { it.isEmpty() ||Pattern.matches(Constant.regexEmail, inputDataArray[0].text.toString())}
            .subscribe {
                isCorrectArray[0] = it
                displayMsg(0,it)
            }
        val inputPwd = RxTextView.textChanges(inputDataArray[1])
            .map { it.isEmpty() ||Pattern.matches(Constant.regexPwd, inputDataArray[1].text.toString()) }
            .subscribe{
                isCorrectArray[1] = it
                displayMsg(1,it)
            }
        val checkInputPwd = RxTextView.textChanges(inputDataArray[2])
            .map { it.isEmpty() || inputDataArray[1].text.toString() == inputDataArray[2].text.toString() }
            .subscribe{
                isCorrectArray[2] = it
                displayMsg(2,it)
            }
        val inputNickName = RxTextView.textChanges(inputDataArray[3])
            .map { it.isEmpty() || it.length in 8..30  }
            .subscribe{
                isCorrectArray[3] = it
                displayMsg(3,it)
            }
        
    }

    private fun displayMsg(index:Int, check:Boolean){
        textInputLayoutArray[index].isErrorEnabled = !check
        if(!check){
            textInputLayoutArray[index].error = errorMsgArray[index]
        }
    }

    override fun onStop() {
        super.onStop()
        viewDisposables.clear()
    }
}
