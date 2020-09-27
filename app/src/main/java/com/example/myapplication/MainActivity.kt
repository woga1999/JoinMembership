package com.example.myapplication

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import com.jakewharton.rxbinding2.widget.RxCompoundButton
import com.jakewharton.rxbinding2.widget.RxRadioGroup
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    protected lateinit var inputDataArray: Array<EditText>
    private lateinit var textInputLayoutArray: Array<TextInputLayout>
    private lateinit var isCorrectArray: Array<Boolean>
    private var userSex:String = ""
    internal val viewDisposables = CompositeDisposable() // 메모리 누수를 막기 위한 클래스
    private lateinit var errorMsgArray:Array<String>
    private lateinit var sexArray:Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        inputData()
        setJoin()
    }
    private fun init() {
        inputDataArray = arrayOf(editEmail, editPwd, editCheckPwd, editName, editBirth)
        textInputLayoutArray = arrayOf(editEmailLayout, editPwdLayout, editCheckPwdLayout, editNameLayout, editBirthLayout)
        isCorrectArray  = arrayOf(false,false,false,false,false,false,false,false)
        errorMsgArray  = arrayOf(Constant.emailErrorMessage,  Constant.pwdErrorMessage, Constant.checkPwdErrorMessage, Constant.nickNameErrorMessage, Constant.birthErrorMessage)
        sexArray = arrayOf("Male","Female","Nothing")
    }

    private fun setJoin(){
//        for(i in 0 until isCorrectArray.size-1){
//            if(!isCorrectArray[i]) {
//                btnJoin.isEnabled = false
//                return
//            }
//        }
        btnJoin.isEnabled = true
        loading(btnJoin)
    }

    private fun loading(btn:Button){
        btn.setOnClickListener { v->
            DialogTask(this).execute()
        }
    }

    public fun checkExecption(): Int{
        if( calculateAge() != -1 && calculateAge() <= 14) return 400
        else if(UserStorage.prefs.getString(inputDataArray[0].text.toString(), "email") == "email") return 401
        else{
            UserStorage.prefs.setString(inputDataArray[0].text.toString(), "email")
            return 200
        }
    }

    private fun calculateAge(): Int{
        var nowTime : LocalDate=LocalDate.now()
        var userBirth = LocalDate.parse(inputDataArray[4].text.toString(), DateTimeFormatter.ISO_DATE);
        var age = nowTime.year - userBirth.year
        var month = nowTime.monthValue - userBirth.monthValue
        if(month <0 || (month == 0 && nowTime.dayOfMonth < userBirth.dayOfMonth)){
            age--
        }
        return age
    }

    private fun birthTouchListener():Boolean{
        //생년월일 선택
        editBirth.setOnTouchListener { v, event ->
            if(event.action == MotionEvent.ACTION_DOWN){
                displayDatePickerDialog(editBirth)
            }
            true
        }
        editBirth.setOnKeyListener { v, keyCode, event ->
            if(event.action == KeyEvent.ACTION_DOWN && keyCode == KEYCODE_ENTER){
                displayDatePickerDialog(editBirth)
            }
            true
        }
        return Pattern.matches(Constant.regexBirth, editBirth.text.toString())
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

    private fun inputData() {
        val inputEmail = RxTextView.textChanges(inputDataArray[0])
            .map { it.isEmpty() || Pattern.matches(Constant.regexEmail, inputDataArray[0].text.toString()) }
            .subscribe {
                if(!inputDataArray[0].text.isEmpty()) isCorrectArray[0] = it
                else isCorrectArray[0] = false
                displayMsg(0, it)
                setJoin()
            }
        val inputPwd = RxTextView.textChanges(inputDataArray[1])
            .map { it.isEmpty() || Pattern.matches(Constant.regexPwd, inputDataArray[1].text.toString()) }
            .subscribe {
                if(!inputDataArray[1].text.isEmpty()) isCorrectArray[0] = it
                else isCorrectArray[1] = false
                displayMsg(1, it)
                setJoin()
            }
        val checkInputPwd = RxTextView.textChanges(inputDataArray[2])
            .map { it.isEmpty() || inputDataArray[1].text.toString() == inputDataArray[2].text.toString() }
            .subscribe {
                if(!inputDataArray[2].text.isEmpty()) isCorrectArray[0] = it
                else isCorrectArray[2] = false
                displayMsg(2, it)
                setJoin()
            }
        val inputNickName = RxTextView.textChanges(inputDataArray[3])
            .map { it.isEmpty() || it.length in 8..30 }
            .subscribe {
                if(!inputDataArray[3].text.isEmpty()) isCorrectArray[0] = it
                else isCorrectArray[3] = false
                displayMsg(3, it)
                setJoin()
            }
        val setBirthDate = RxTextView.textChanges(inputDataArray[4])
            .map { birthTouchListener() || Pattern.matches(Constant.regexBirth, inputDataArray[4].text.toString())}
            .subscribe {
                isCorrectArray[4] = it
                if(!inputDataArray[4].text.isEmpty()) displayMsg(4, it)
                setJoin()
            }

        val choiceSexRadioGroup = RxRadioGroup.checkedChanges(sexRadioGroup)
            .subscribe{
                isCorrectArray[5] = it != -1
                if(it != -1) userSex = sexArray[it-1]
                setJoin()
            }
        val agreeCheckBox = RxCompoundButton.checkedChanges(needAgree)
            .subscribe {
                isCorrectArray[6] = it
                setJoin()
            }
        val marketingAgreeCheckBox = RxCompoundButton.checkedChanges(marketingAgree)
            .subscribe {
                isCorrectArray[7] = it
            }

        viewDisposables.addAll(inputEmail, inputPwd, checkInputPwd, inputNickName, setBirthDate, choiceSexRadioGroup, agreeCheckBox, marketingAgreeCheckBox)
    }

    private fun displayMsg(index:Int, check:Boolean){
        textInputLayoutArray[index].isErrorEnabled = !check
        if(!check){
            textInputLayoutArray[index].error = errorMsgArray[index]
        }
    }

    private fun displayToast(code:Int){
        var str = ""
        if(code == 400){
            str = "만 14세 미만은 회원가입 할 수 없습니다."
        }
        else if(code == 401){
            str = "이미 가입된 사용자입니다."
        }
        toast( "($code) $str")
    }

    protected fun startActivity(){
        var email = inputDataArray[0].text.toString()
        var password = inputDataArray[1].text.toString()
        var nickName = inputDataArray[3].text.toString()
        var sex = userSex
        var checkBox1 = "약관에 동의했습니다."
        var checkBox2="마케팅 약관에 동의하지 않았습니다."
        if(isCorrectArray[7]) checkBox2 = "마케팅 약관에 동의했습니다."
        startActivity(intentFor<ViewInformation>("email" to email,"pwd" to password,"nickname" to nickName, "sex" to sex, "checkBox1" to checkBox1, "checkBox2" to checkBox2))
    }

    class DialogTask(var activity: MainActivity) : AsyncTask<Void, Void, Void>(){

        var dialog = Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar)
        override fun onPreExecute() {
            val view = activity.layoutInflater.inflate(R.layout.progress_bar,null)
            dialog.setContentView(view)
            dialog.setCancelable(false)
            dialog.show()
            super.onPreExecute()
        }
        override fun doInBackground(vararg params: Void?): Void? {
            Thread.sleep(2000)
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            dialog.dismiss()
            var statusCode = activity.checkExecption()
            if(statusCode != 200){
                activity.displayToast(statusCode)
            }
            else{
                activity.startActivity()
            }
        }

    }

    override fun onStop() {
        super.onStop()
        viewDisposables.clear()
    }
}

