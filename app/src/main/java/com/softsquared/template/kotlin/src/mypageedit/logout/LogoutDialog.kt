package com.softsquared.template.kotlin.src.mypageedit.logout

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseResponse
//import com.softsquared.template.kotlin.src.auth.login.LoginActivity
import com.softsquared.template.kotlin.src.auth.loginInformation.LoginInformation
import com.softsquared.template.kotlin.src.mypage.MyPageService
import com.softsquared.template.kotlin.src.mypage.MyPageView
import com.softsquared.template.kotlin.src.mypage.models.MonthsAchievementsResponse
import com.softsquared.template.kotlin.src.mypageedit.models.MyPageCommentsResponse
import com.softsquared.template.kotlin.src.mypage.models.MyPageResponse
import com.softsquared.template.kotlin.src.mypage.models.RestScheduleCountResponse
import com.softsquared.template.kotlin.src.mypage.models.TotalScheduleCountResponse
import com.softsquared.template.kotlin.src.mypageedit.MyPageEditView
import com.softsquared.template.kotlin.src.mypageedit.models.SetProfileImageResponse
import com.softsquared.template.kotlin.util.Constants


class LogoutDialog(context:Context) : Dialog(context), MyPageEditView, MyPageView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logout_dialog);

        //카카오 로그인 초기화
        KakaoSdk.init(context, "850f56e3e5ba5613faf68a8aaa4b95bc")

        val logout : Button = findViewById(R.id.logout_check)
        val cancel : Button = findViewById(R.id.logout_cancel)

        logout.setOnClickListener {

            MyPageService(this).tryGetMyPage()

        }

        cancel.setOnClickListener {
            Log.d("TAG", "onPositiveClicked: 취소버튼 눌림")
            dismiss()
        }

    }

    override fun onGetMyPageCommentsSuccess(response: MyPageCommentsResponse) {
    }

    override fun onGetMyPageCommentsFail(message: String) {
    }

    override fun onGetMyPageSuccess(response: MyPageResponse) {

        when(response.code){
            100 -> {

                if (response.loginMethod == "F"){
                    Log.d("TAG", "onPositiveClicked: Famo 로그아웃성공")
                    val edit = ApplicationClass.sSharedPreferences.edit()
                    edit.remove(ApplicationClass.X_ACCESS_TOKEN)
                    edit.apply()
                }

                if (response.loginMethod == "K"){
                    Log.d("TAG", "onPositiveClicked: 카카오 로그아웃성공")
                    UserApiClient.instance.logout { error ->
                        if (error != null) {
                            Log.e("TAG", "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                        } else {
                            Log.i("TAG", "로그아웃 성공. SDK에서 토큰 삭제됨")
                        }
                    }
                }

                val intent = Intent(context,LoginInformation::class.java)
                context.startActivity(intent)
            }
        }

    }

    override fun onGetMyPageFail(message: String) {
    }

    override fun onGetRestScheduleCountSuccess(response: RestScheduleCountResponse) {
    }

    override fun onGetRestScheduleCountFailure(message: String) {
    }

    override fun onGetTotalScheduleCountSuccess(response: TotalScheduleCountResponse) {
    }

    override fun onGetTotalScheduleCountFailure(message: String) {
    }

    override fun onGetMonthsAchievmentsSuccess(response: MonthsAchievementsResponse) {
    }

    override fun onGetMonthsAchievmentsFailure(message: String) {
    }

    override fun onPutMyPageUpdateSuccess(response: BaseResponse) {
    }

    override fun onPutMyPageUpdateFail(message: String) {
    }

    override fun onPostProfileImageSuccess(response: SetProfileImageResponse) {
    }

    override fun onPostProfileImageFailure(message: String) {
    }

    override fun onPatchProfileSuccess(response: BaseResponse) {
    }

    override fun onPatchProfileFailure(message: String) {
    }

}