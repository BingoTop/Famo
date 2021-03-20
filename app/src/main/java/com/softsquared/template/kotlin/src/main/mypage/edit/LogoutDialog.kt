package com.softsquared.template.kotlin.src.main.mypage.edit

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.softsquared.template.kotlin.R


class LogoutDialog(context:Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logout_dialog);

        val logout : Button = findViewById(R.id.logout_check)
        val cancel : Button = findViewById(R.id.logout_cancel)

        cancel.setOnClickListener {
            Log.d("TAG", "onPositiveClicked: 로그아웃버튼 눌림")
        }

        logout.setOnClickListener {
            Log.d("TAG", "onPositiveClicked: 취소버튼 눌림")
        }

    }

}