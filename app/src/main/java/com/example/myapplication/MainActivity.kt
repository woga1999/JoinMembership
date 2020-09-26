package com.example.myapplication

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
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
        setData()
        setJoin()
    }
    private fun init() {
        inputDataArray = arrayOf(editEmail, editPwd, editCheckPwd, editName, editBirth)
        textInputLayoutArray = arrayOf(editEmailLayout, editPwdLayout, editCheckPwdLayout, editNameLayout, editBirthLayout)
        isCorrectArray  = arrayOf(false,false,false,false,false,false,false,false)
        errorMsgArray  = arrayOf(Constant.emailErrorMessage,  Constant.pwdErrorMessage, Constant.checkPwdErrorMessage, Constant.nickNameErrorMessage)

    }

    private fun setJoin(){
        btnJoin.isEnabled = isDone
    }

    private fun setData(){
        //생년월일 선택
        editBirth.setOnTouchListener { v, event ->
            if(event.action == MotionEvent.ACTION_DOWN){
                displayDatePickerDialog(editBirth)
                isCorrectArray[4] = true
            }
            true
        }
        //성별선택
        sexRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            Toast.makeText(applicationContext, "On checked Changed : ${radio.text}", Toast.LENGTH_SHORT).show()
            isCorrectArray[5] = checkedId != -1
        }

        //체크박스 선택
        needAgree.setOnCheckedChangeListener { buttonView, isChecked->
            Toast.makeText(applicationContext, "needAgree : $isChecked", Toast.LENGTH_SHORT).show()
            isCorrectArray[6] = isChecked
        }
        marketingAgree.setOnCheckedChangeListener { buttonView, isChecked ->
            Toast.makeText(applicationContext, "needlessAgree : $isChecked", Toast.LENGTH_SHORT).show()
            isCorrectArray[7] = isChecked
        }
    }

    private fun displayDatePickerDialog(eT : EditText){
        var cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.KOREA)
            eT.setText (sdf.format(cal.time))
        }

        DatePickerDialog(this@MainActivity, dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)).show()
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
        viewDisposables.addAll(inputEmail, inputPwd, checkInputPwd, inputNickName)
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
