package com.example.myapplication

class Constant {
    companion object {
        //에러메세지
        val emailErrorMessage = "이메일 형식으로 입력해주세요."
        val pwdErrorMessage = "대문자 1개 이상, 소문자 1개 이상, 숫자 1개 이상 포함해서 8자 이상 입력해주세요."
        val checkPwdErrorMessage = "입력하신 비밀번호와 맞지않습니다."
        val nickNameErrorMessage = "닉네임은 8자 이상 30자 이하로 입력해주세요."
        val birthErrorMessage = "생년월일을 yyyy-mm-dd 형식으로 넣어주세요"

        //정규식
        val regexEmail = "[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}"
        val regexPwd = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$"
        val regexBirth = "[0-9]{4}-[0-9]{2}-[0-9]{2}"
    }
}