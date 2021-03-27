package com.softsquared.template.kotlin.src.mypageedit

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.FileUtils
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.net.toFile
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.databinding.ActivityMyPageEditBinding
import com.softsquared.template.kotlin.src.main.mypage.edit.*
import com.softsquared.template.kotlin.src.mypage.MyPageActivity
import com.softsquared.template.kotlin.src.mypage.models.MyPageResponse
import com.softsquared.template.kotlin.src.mypageedit.account.AccountWithdrawalDialog
import com.softsquared.template.kotlin.src.mypageedit.logout.LogoutDialog
import com.softsquared.template.kotlin.src.mypageedit.models.SetProfileImageResponse
import com.softsquared.template.kotlin.util.Constants
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import com.softsquared.template.kotlin.src.mypageedit.models.MyPageCommentsResponse
import com.softsquared.template.kotlin.src.mypageedit.models.PutMyPageUpdateRequest
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class MyPageEditActivity : BaseActivity<ActivityMyPageEditBinding>
    (ActivityMyPageEditBinding::inflate), MyPageEditView {

    private var selectedImageUri:Uri ?= null
    //카메라 변수
    private val GET_GALLERY_IMAGE = 200
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var currentPhotoPath: String

    //클릭에 따른 visible/gone을 위한 변수
    var topMentCnt = 1
    var dDaySettingCnt = 1
    var accountSettingCnt = 1

    // 날짜
    var strDate = ""
    // 날짜를 선택유무에 대한 변수
    var dateCnt = 0

    //카메라/갤러리 확인변수
    var imgCnt : Int? = null
    var intentCnt : Int? = null

    var galleryUrl: Uri? = null
    var cameraImg: Bitmap? = null

    var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context = this

        intentCnt = intent.getIntExtra("check", 100)

        MyPageEditService(this).tryGetMyPage()

        //이미지 클릭 시
        binding.myPageEditImg.setOnClickListener {
            //카메라 권한 설정
            settingPermission()

            // 갤러리/카메라 알림창
            val builder = AlertDialog.Builder(this)
            builder.setTitle("사진 찍기")
                .setMessage("사진을 새로 찍으시거나 사진\n라이브러리에서 선택하세요.")
                .setPositiveButton("카메라",
                    DialogInterface.OnClickListener { dialog, id ->
                        //카메라 시작
                        startCapture()

                    })
                .setNegativeButton("갤러리",
                    DialogInterface.OnClickListener { dialog, id ->

                        val intent = Intent(Intent.ACTION_PICK)
                        intent.setDataAndType(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            "image/*"
                        )
                        startActivityForResult(intent, GET_GALLERY_IMAGE)
                    })

            val alertDialog = builder.create()

            //다이얼로그 색상
            alertDialog.setOnShowListener {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE)
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLUE)
            }
//            alertDialog.window!!.setBackgroundDrawable()

            alertDialog.show()
        }

        //상단 멘트설정 화살표 클릭 시 밑에 내용 나오게
        binding.myPageEditBtnTopMent.setOnClickListener {
            if (topMentCnt % 2 != 0){
                binding.myPageEditLinearTopMent.visibility = View.VISIBLE
                binding.myPageEditTopMentView.visibility = View.GONE
            }

            if (topMentCnt % 2 == 0){
                binding.myPageEditLinearTopMent.visibility = View.GONE
                binding.myPageEditTopMentView.visibility = View.VISIBLE
            }
            topMentCnt++
//            binding.myPageEditLinearTopMent.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }

        //디데이 설정 버튼 클릭 시
        //버튼 색상 및 화살표 하단내용 나오게
        binding.myPageEditBtnDday.setOnClickListener {
            if (dDaySettingCnt % 2 != 0){
//                binding.myPageEditBtnDday.setColorFilter(Color.parseColor("#FFAE2A"))
                binding.myPageEditBtnDdayCheck.visibility = View.VISIBLE
                binding.myPageEditBtnDdayCheck2.visibility = View.VISIBLE
                binding.myPageEditLinearDdaySetting.visibility = View.VISIBLE
            }

            if (dDaySettingCnt % 2 == 0){
//                binding.myPageEditBtnDday.setColorFilter(Color.parseColor("#FFFFFF"))
                binding.myPageEditBtnDdayCheck.visibility = View.GONE
                binding.myPageEditBtnDdayCheck2.visibility = View.GONE
                binding.myPageEditLinearDdaySetting.visibility = View.GONE
            }

            dDaySettingCnt++
        }

        //계정 설정 화살표 클릭 시
        //하단 내용 나오게
        binding.myPageEditBtnAccountSetting.setOnClickListener {
            if (accountSettingCnt % 2 != 0){
                binding.myPageEditBtnAccountSettingView.visibility = View.GONE
                binding.myPageEditLinearAccountSetting.visibility = View.VISIBLE
            }

            if (accountSettingCnt % 2 == 0){
                binding.myPageEditBtnAccountSettingView.visibility = View.VISIBLE
                binding.myPageEditLinearAccountSetting.visibility = View.GONE
            }

            accountSettingCnt++
        }

        //날짜 변화에 대한
        val listener = DatePicker.OnDateChangedListener { view, year, monthOfYear, dayOfMonth ->

            if (monthOfYear + 1 < 10 && dayOfMonth < 10) {
                strDate = year.toString() + "-" + "0" + (monthOfYear + 1) + "-" + "0" + dayOfMonth
            } else if (monthOfYear + 1 >= 10 && dayOfMonth < 10) {
                strDate = year.toString() + "-" + (monthOfYear + 1) + "-" + "0" + dayOfMonth
            } else if (monthOfYear + 1 < 10 && dayOfMonth >= 10) {
                strDate = year.toString() + "-" + "0" + (monthOfYear + 1) + "-" + dayOfMonth
            } else {
                strDate = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth
            }

            Toast.makeText(this, strDate, Toast.LENGTH_SHORT).show()
            dateCnt++
        }

        val month: Int = binding.dataPicker.month
        val year = binding.dataPicker.year
        val day = binding.dataPicker.dayOfMonth
        binding.dataPicker.init(year, month, day, listener)


        //체크표시
        binding.myPageEditBtnSave.setOnClickListener {
            val onlyDate: LocalDate = LocalDate.now()

            if (dateCnt == 0){
//                binding.myPageEditEtComments.setText(onlyDate.toString())
                val str = onlyDate.toString()
                val format = SimpleDateFormat("YYYY-MM-DD")
                val nowDate : Date? = format.parse(str)
                strDate = str
            }else{
//                binding.myPageEditEtComments.setText(strDate)

            }

            if (dDaySettingCnt % 2 != 0){
                dDaySettingCnt = 1
            }else{
                dDaySettingCnt = -1
            }

            Log.d("TAG", "상단멘트사전확인: ${binding.myPageEditEtComments.text}")
            val myPageUpdateRequest = PutMyPageUpdateRequest(
                nickname = binding.myPageEditEtName.text.toString(),
                titleComment = binding.myPageEditEtComments.text.toString(),
                goalStatus = dDaySettingCnt,
                goalTitle = binding.myPageEditEtGoaltitle.text.toString(),
                goalDate = strDate
            )

            MyPageEditService(this).tryPutMyPageUpdate(myPageUpdateRequest)
        }

        //로그아웃
        binding.mypageEditBtnLogout.setOnClickListener {
            logoutDialog()
        }

        binding.myPageEditAccount.setOnClickListener {
            accountWithdrawal()
        }

        //X버튼 클릭 시 내정보로 이동
        binding.myPageEditBack.setOnClickListener {
            finish()
        }

        // 프로필 설정 버튼
        binding.myPageEditBtnSave.setOnClickListener {

        }
    }

    //로그아웃 알림창
    fun logoutDialog() {
        val dialog = LogoutDialog(this)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    //회원탈퇴 알림창
    fun accountWithdrawal() {
        val dialog = AccountWithdrawalDialog(this)
        dialog.show()
    }

    //권한 설정
    fun settingPermission() {
        val permis = object : PermissionListener {
            //            어떠한 형식을 상속받는 익명 클래스의 객체를 생성하기 위해 다음과 같이 작성
            override fun onPermissionGranted() {
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                ActivityCompat.finishAffinity(this@MyPageEditActivity) // 권한 거부시 앱 종료
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permis)
            .setRationaleMessage("카메라 사진 권한 필요")
            .setDeniedMessage("카메라 권한 요청 거부")
            .setPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            )
            .check()
    }

    //카메라 시작
    @SuppressLint("QueryPermissionsNeeded")
    fun startCapture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(this!!.packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this!!,
                        "com.softsquared.template.kotlin.fileprovider",
                        it
                    )
                    //정보를 onActivityResult 함수로 전달
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    //이미지 생성
    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    fun createImageFile(): File {
        //사진이 생성되면 이름이 겹치지 않게 고유해야하므로 날짜로 만든다
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = this!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    //갤러리/카메라 실행
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //갤러리
        if (requestCode == GET_GALLERY_IMAGE && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri: Uri? = data.data
            Log.d("TAG", "onActivityResult:dd  ${data.data!!.path}")

//            currentPhotoPath = getExternalFilesDir(selectedImageUri.toString())
//
//            Log.d("TAG", "onActivityResult:dd  ${getExternalFilesDir(selectedImageUri.toString())}")
            val file = File(data.data!!.path!!.toString())
            val requestFile = file?.let { RequestBody.create("multipart/form-data".toMediaTypeOrNull(), it) }
            val requestImage = requestFile?.let { MultipartBody.Part.createFormData("profileImage",file.name, it) }
            if (requestImage != null) {
                showLoadingDialog(this)
                MyPageEditService(this).tryPostMyProfileImage(requestImage)
            }



            binding.myPageEditImg.setImageURI(selectedImageUri)

            val edit = ApplicationClass.sSharedPreferences.edit()
            edit.putString(Constants.PROFILE_GALLERY, selectedImageUri.toString())
            edit.apply()
            imgCnt = 1
        }

        //카메라
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val file = File(currentPhotoPath)
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),file)
            val requestImage = MultipartBody.Part.createFormData("profileImage",file.name,requestFile)
            showLoadingDialog(this)
            MyPageEditService(this).tryPostMyProfileImage(requestImage)

            if (Build.VERSION.SDK_INT < 28) {
                val bitmap = MediaStore.Images.Media
                    .getBitmap(this.contentResolver, Uri.fromFile(file))
                binding.myPageEditImg.setImageBitmap(bitmap)
            } else {

                val decode = ImageDecoder.createSource(
                    this.contentResolver,
                    Uri.fromFile(file)
                )
                val bitmap : Bitmap = ImageDecoder.decodeBitmap(decode)
                binding.myPageEditImg.setImageBitmap(bitmap)
                Log.d("TAG", "onActivityResult: 보내는bitmap $bitmap")

                val test = bitmapToString(bitmap)
                val edit = ApplicationClass.sSharedPreferences.edit()
                edit.putString(Constants.PROFILE_KAMERA, test)
                edit.apply()
            }
            imgCnt = 2
        }

    }

    fun bitmapToString(bitmap: Bitmap) : String{
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)

    }

    fun stringToBitmap(encodedString: String): Bitmap? {
        return try {
            val encodeByte: ByteArray = Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            e.message
            null
        }
    }



    override fun onGetMyPageCommentsSuccess(response: MyPageCommentsResponse) {
    }

    override fun onGetMyPageCommentsFail(message: String) {
    }

    override fun onGetMyPageSuccess(response: MyPageResponse) {

        when(response.code){
            100 -> {
                Log.d("TAG", "onGetMyPageSuccess: MyPage수정 조회성공")
                showCustomToast("MyPage수정 조회성공")

                val kakaoImg: String? = ApplicationClass.sSharedPreferences.getString(
                    Constants.KAKAO_THUMBNAILIMAGEURL,
                    null
                )
                val name =
                    ApplicationClass.sSharedPreferences.getString(Constants.USER_NICKNAME, null)


                Log.d("TAG", "onGetMyPageSuccess: kakaoImg :$kakaoImg")

                val gallery =
                    ApplicationClass.sSharedPreferences.getString(Constants.PROFILE_GALLERY, null)

                val camera =
                    ApplicationClass.sSharedPreferences.getString(Constants.PROFILE_KAMERA, null)

                if (gallery != null) {
                    galleryUrl = gallery.toUri()
                }

                if (camera != null) {
                    cameraImg = stringToBitmap(camera)
                }

                Log.d("TAG", "onGetMyPageSuccess: imgCnt :$imgCnt")
                if (response.loginMethod == "K") {
                    //카톡프사가 없을때 기본이미지 적용, 있으면 있는거 적용
                    if (kakaoImg!!.isEmpty()) {
                        Glide.with(this).load(R.drawable.my_page_img2)
                            .centerCrop().into(binding.myPageEditImg)
                    } else if (kakaoImg!!.isNotEmpty()) {
                        Glide.with(this).load(kakaoImg)
                            .centerCrop().into(binding.myPageEditImg)
                    }

                    if (intentCnt == 1) {
                        binding.myPageEditImg.setImageURI(galleryUrl)
                    } else if (intentCnt == 2) {
                        binding.myPageEditImg.setImageBitmap(cameraImg)
                    }
                }

//페모로그인일경우
                if (response.loginMethod == "F") {
                    //처음에는 기본 이미지
                    if (gallery == null && camera == null) {
                        Glide.with(this).load(R.drawable.my_page_img2)
                            .centerCrop().into(binding.myPageEditImg)
                    } else if (intentCnt == 1) {
                        binding.myPageEditImg.setImageURI(galleryUrl)
                    } else if (intentCnt == 2) {
                        binding.myPageEditImg.setImageBitmap(cameraImg)
                    }

                }


//                if (response.loginMethod == "K") {
//
//                    if (kakaoImg!!.isNotEmpty()) {
//                        Log.d("TAG", "onGetMyPageSuccess: 카로확인")
//                        Glide.with(this).load(kakaoImg)
//                            .centerCrop().into(binding.myPageEditImg)
//                    } else {
//                        Log.d("TAG", "onGetMyPageSuccess: 카로확인2")
//                        Glide.with(this).load(R.drawable.my_page_img2)
//                            .centerCrop().into(binding.myPageEditImg)
//                    }
//                } else {
//                    Glide.with(this).load(R.drawable.my_page_img2)
//                        .centerCrop().into(binding.myPageEditImg)
//                }

                //이름 적용
                binding.myPageEditEtName.setText(name)

            }
            else -> {
                Log.d("TAG", "onGetMyPageSuccess: ${response.message.toString()}")
                showCustomToast("${response.message.toString()}}")
            }
        }

    }

    override fun onGetMyPageFail(message: String) {
    }

    override fun onPutMyPageUpdateSuccess(response: BaseResponse) {

        when(response.code){
            100 -> {
                Log.d("TAG", "onPutMyPageUpdateSuccess: MyPage수정성공")
                showCustomToast("수정성공")

                val sampleDate = strDate
                val sf = SimpleDateFormat("yyyy-MM-dd")
                val date = sf.parse(sampleDate)
                val today = Calendar.getInstance()
                val day = ((date.time - today.time.time) / (60 * 60 * 24 * 1000)) + 1

                binding.myPageEditDay.setText(day.toString())
                val name = binding.myPageEditEtName.text.toString()
                val comments = binding.myPageEditEtComments.text.toString()
                val goalTitle = binding.myPageEditEtGoaltitle.text.toString()

                val edit = ApplicationClass.sSharedPreferences.edit()
                edit.putString(Constants.USER_NICKNAME, name)
                edit.putString(Constants.COMMENTS, comments)
                edit.putString(Constants.DAY, day.toString())
                edit.putString(Constants.GOALTITLE, goalTitle)
                edit.putString(Constants.DDAY_SETTING, dDaySettingCnt.toString())
                edit.apply()


                val intent = Intent(this, MyPageActivity::class.java)
//                intent.putExtra("day", day)
//                intent.putExtra("goalTitle", binding.myPageEditEtGoaltitle.toString())
                intent.putExtra("check", imgCnt)
                startActivity(intent)
//                myPageActivityView.moveMyPage()
//                nickname = binding.myPageEditEtName.text.toString(),
//                titleComment = binding.myPageEditEtComments.text.toString(),
//                goalStatus = dDaySettingCnt,
//                goalTitle = binding.myPageEditEtGoaltitle.text.toString(),
//                goalDate = goalDate!!


            }
            else -> {
                Log.d("TAG", "onPutMyPageUpdateSuccess: ${response.message.toString()}")
                showCustomToast("${response.message.toString()}}")
            }
        }

    }

    override fun onPutMyPageUpdateFail(message: String) {
    }

    override fun onPostProfileImageSuccess(response: SetProfileImageResponse) {
        if(response.isSuccess && response.code == 100){
            showCustomToast(response.message.toString())
        }else{
            showCustomToast(response.message.toString())
            Log.d("TAG", "onPostProfileImageSuccess: ${response.message}")

        }
        dismissLoadingDialog()
    }

    override fun onPostProfileImageFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
        Log.d("TAG", "onPostProfileImageFailure: $message")
    }

//    fun getRealPathFromURI(contentUri:Uri):String{
//        var cursor:Cursor? = null
//        try{
//            val proj = {MediaStore.Images.Media.DATA}
//            cursot = contentResolver.query(contentUri,proj,null,null,null)
//
//        }
//    }

}