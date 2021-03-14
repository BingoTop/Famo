package com.softsquared.template.kotlin.src.main.category

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.databinding.ActivityCategoryEditBinding
import com.softsquared.template.kotlin.src.main.category.adapter.ScheduleCategoryEditAdapter
import com.softsquared.template.kotlin.src.main.category.models.CategoryInsertRequest
import com.softsquared.template.kotlin.src.main.category.models.CategoryInsertResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleCategoryData

class CategoryEditActivity() : BaseActivity<ActivityCategoryEditBinding>
    (ActivityCategoryEditBinding::inflate), ICategoryRecyclerView, CategoryEditView {

    //    private var categoryEditList = ArrayList<ScheduleCategoryData>()
    val categoryEditList: ArrayList<ScheduleCategoryData> = arrayListOf()
    private lateinit var categoryEditAdapter: ScheduleCategoryEditAdapter

    var wholeName: String? = null
    var wholeColor: String? = null
    var name: List<String>? = null
    var color: List<String>? = null
    var size: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        wholeName = intent.getStringExtra("name")
        wholeColor = intent.getStringExtra("color")
        size = intent.getIntExtra("size", 0)
        Log.d("TAG", "CategoryEditActivity : name : $wholeName")
        Log.d("TAG", "CategoryEditActivity : color : $wholeColor")
        Log.d("TAG", "CategoryEditActivity : color : $size")

        name = wholeName!!.split(":")
        color = wholeColor!!.split(":")

        //카테고리 리사이클러뷰
        createCategoryRecyclerview()

        binding.categoryEditBtnPlus.setOnClickListener {

//            val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySUQiOjUsIm1ldGhvZCI6IkYiLCJpYXQiOjE2MTUzODYyMTEsImV4cCI6MTY0NjkyMjIxMSwic3ViIjoidXNlckluZm8ifQ.laZCThzA823-i5-ZTVyfvqq8PMIgHUdAnP97woIHufQ"

//            val categoryInsertRequest = CategoryInsertRequest(
//                categoryName = "",
//                categoryColor = 1
//            )
//            CategoryEditService(this).tryPostCategoryEditInsert(token,categoryInsertRequest)
//            val scheduleCategoryData =  ScheduleCategoryData("")
//            categoryEditAdapter.addItem(scheduleCategoryData)
//            categoryEditAdapter.notifyDataSetChanged()
        }

        //X 버튼
        binding.categoryEditXBtn.setOnClickListener {
//            (activity as MainActivity).replaceFragment(ScheduleFindFragment.newInstance());
//            (activity as MainActivity).onBackPressed()
            val token = ApplicationClass.sSharedPreferences.getString(
                ApplicationClass.X_ACCESS_TOKEN,
                null
            ).toString()
//            val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySUQiOjEzLCJtZXRob2QiOiJGIiwiaWF0IjoxNjE1NTI3OTA2LCJleHAiOjE2NDcwNjM5MDYsInN1YiI6InVzZXJJbmZvIn0.qd1_zNvzZWWfDp54uzEyIml5X6mfmNhFfQcyQN23_6U"

            Log.d(
                "TAG", "CategoryEditFragment: ${
                    ApplicationClass.sSharedPreferences.getString(
                        ApplicationClass.X_ACCESS_TOKEN,
                        null
                    )
                }"
            )
            val categoryInsertRequest = CategoryInsertRequest(
                categoryName = "aaaaaㅠ",
                categoryColor = 3
            )

            CategoryEditService(this).tryPostCategoryEditInsert(categoryInsertRequest)
//            val intent = Intent(activity,MainActivity::class.java)
//            startActivity(intent)
        }

    }

    fun createCategoryRecyclerview() {
        //테스트 데이터

        for (i in 0 until size!!) {

            categoryEditList.add(
                ScheduleCategoryData(name!![i], color!![i])
            )
        }
//        categoryEditList = arrayListOf(
//            ScheduleCategoryData("학교"),
//            ScheduleCategoryData("알바"),
//            ScheduleCategoryData("친구")
//        )

        categoryEditAdapter = ScheduleCategoryEditAdapter(categoryEditList)

//        this.photoGridRecyeclerViewAdapter = PhotoGridRecyclerViewAdapter()
//        this.photoGridRecyeclerViewAdapter.submitList(photoList)
//        //                                                                                                              데이터값을 처음부터/끝부분부터 방향
//        my_photo_recycler_view.layoutManager = GridLayoutManager(this,2,
//            GridLayoutManager.VERTICAL,false)
//        my_photo_recycler_view.adapter = this.photoGridRecyeclerViewAdapter

        binding.recyclerviewEditCategory.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerviewEditCategory.setHasFixedSize(true)
        binding.recyclerviewEditCategory.adapter = categoryEditAdapter
    }

    //삭제버튼 클릭
    override fun onItemDeleteBtnClicked(position: Int) {
        Log.d("aa", "onSearchItemDeleteBtnClicked: ")
        //해당 번쨰를 삭제 및 저장
        categoryEditList.removeAt(position)
        //데이터 덮어쓰기
//        SharedPrefManager.storeSearchHisotryList(this.searchHistoryList)
        //데이터 변경알림
        this.categoryEditAdapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(): CategoryEditActivity {    // shs: 함수의 반환 형이 Fragment 형이라...
            return CategoryEditActivity()
        }
    }

    override fun onPostCategoryInsertSuccess(response: CategoryInsertResponse) {

        if (response.isSuccess) {
            when (response.code) {
                100 -> {
                    showCustomToast("카테고리 생성 테스트")
                    Log.d("TAG", "onPostCategoryInsertSuccess: 카테고리생성 테스트")
                }
                else -> {
                    showCustomToast(response.message.toString())
                }
            }
        } else {
            showCustomToast(response.message.toString())
        }

    }

    override fun onPostCategoryInsertFail(message: String) {
    }

    override fun onDeleteCategoryDeleteSuccess(response: BaseResponse) {
    }

    override fun onDeleteCategoryDeleteFail(message: String) {
    }

    override fun onPatchCategoryUpdateSuccess(response: BaseResponse) {
    }

    override fun onPatchCategoryUpdateFail(message: String) {
    }

}