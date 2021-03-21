package com.softsquared.template.kotlin.src.main.schedulefind.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.src.main.schedulefind.CategoryInquiryView
import com.softsquared.template.kotlin.src.main.schedulefind.models.CategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.UserCategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleCategoryData

class ScheduleCategoryAdapter(
    var categoryList: ArrayList<ScheduleCategoryData>,
    myScheduleCategoryRecyclerView: IScheduleCategoryRecyclerView
) :
    RecyclerView.Adapter<ScheduleCategoryAdapter.ScheduleCategoryHolder>(), CategoryInquiryView {

    private var iScheduleCategoryRecyclerView: IScheduleCategoryRecyclerView? = null

    var mPreviousIndex = -1

    init {
        Log.d("TAG", "ScheduleCategoryAdapter: init() called ")
        this.iScheduleCategoryRecyclerView = myScheduleCategoryRecyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleCategoryHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_schedule_find_category_item, parent, false)
        view.setOnClickListener {

        }
        return ScheduleCategoryHolder(view, iScheduleCategoryRecyclerView!!).apply {
            itemView.setOnClickListener {
            }
        }
    }

    override fun onBindViewHolder(holder: ScheduleCategoryHolder, position: Int) {
//        holder.text.text = categoryList[position].text
        holder.text.text = categoryList[position].text
        holder.color.setColorFilter(Color.parseColor(categoryList[position].color))
//        holder.color.setImageResource(categoryList[position])
//        holder.color.setImageResource(categoryList[position].color)
//        holder.color.setColorFilter(Color.parseColor(response.data[0].colorInfo))
//        img.setColorFilter(Color.parseColor("#FF0000"))
    }

    override fun getItemCount(): Int = categoryList.size

    fun moveFragment() {

    }

    fun addItem(scheduleCategoryData: ScheduleCategoryData) {
        categoryList.add(scheduleCategoryData)
    }

    inner class ScheduleCategoryHolder(
        itemView: View,
        myScheduleCategoryRecyclerView: IScheduleCategoryRecyclerView
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var iSearchRecyclerViewInterface: IScheduleCategoryRecyclerView

        val text = itemView.findViewById<TextView>(R.id.recyclerview_category_text)
        val color = itemView.findViewById<ImageView>(R.id.recyclerview_category_color)
//        val button = itemView.findViewById<Button>(R.id.recyclerview_category_text)
//        val category = itemView.findViewById<RelativeLayout>(R.id.item_category_list)

        init {
            //리스너연결
//            category.setOnClickListener(this)
            text.setOnClickListener(this)
            color.setOnClickListener(this)
            this.iSearchRecyclerViewInterface = myScheduleCategoryRecyclerView
        }

        override fun onClick(view: View?) {
            when (view) {
                color, text -> {
                    Log.d("로그", "onClick: 카테고리 클릭: $adapterPosition")
                    val wholeColor = iSearchRecyclerViewInterface.onColor()
                    var size = 0
                    var colorList: List<String>? = null
                    val colorStrList = ArrayList<String>()
                    val colorID = ArrayList<Int>()
                    Log.d("로그", "칼라값 받아오기 테스트: $color")
                    colorList = wholeColor.split(":")

                    for (i in wholeColor.indices) {
                        if (wholeColor.substring(i, i + 1) == ":") {
                            size++
                        }
                    }
                    Log.d("로그", "사이즈 : $size")

                    for (i in 0 until size) {

                        if (colorList[i] == "#FF8484") {
                            colorStrList!!.add("#FF8484")
                            colorID.add(1)
                        }
                        if (colorList[i] == "#FCBC71") {
                            colorStrList!!.add("#FCBC71")
                            colorID.add(2)
                        }
                        if (colorList[i] == "#FCDC71") {
                            colorStrList!!.add("#FCDC71")
                            colorID.add(3)
                        }
                        if (colorList[i] == "#C6EF84") {
                            colorStrList!!.add("#C6EF84")
                            colorID.add(4)
                        }
                        if (colorList[i] == "#7ED391") {
                            colorStrList!!.add("#7ED391")
                            colorID.add(5)
                        }
                        if (colorList[i] == "#93EAD9") {
                            colorStrList!!.add("#93EAD9")
                            colorID.add(6)
                        }
                        if (colorList[i] == "#7CC3FF") {
                            colorStrList!!.add("#7CC3FF")
                            colorID.add(7)
                        }
                        if (colorList[i] == "#6D92F7") {
                            colorStrList!!.add("#6D92F7")
                            colorID.add(8)
                        }
                        if (colorList[i] == "#AB93FA") {
                            colorStrList!!.add("#AB93FA")
                            colorID.add(9)
                        }
                        if (colorList[i] == "#FFA2BE") {
                            colorStrList!!.add("#FFA2BE")
                            colorID.add(10)
                        }

                    }
                    Log.d("TAG", "categoryID: $colorStrList")

//                    for (i in 0 until size) {
//
//                        if (adapterPosition == 0 || adapterPosition == 1) {
//                            Log.d("TAG", "색 풀리는거")
//                            color.setColorFilter(Color.parseColor("#00000000"))
//                        }
//                    }

                    color.setColorFilter(Color.parseColor(colorStrList[adapterPosition]))
                    this.iSearchRecyclerViewInterface.onItemMoveBtnClicked(colorID[adapterPosition])

                    Log.d("TAG", "onClick: 다시돌아옴?")
                }
            }
        }

    }

    override fun onGetUserCategoryInquirySuccess(responseUser: UserCategoryInquiryResponse) {
    }

    override fun onGetUserCategoryInquiryFail(message: String) {
    }

    override fun onGetCategoryInquirySuccess(categoryInquiryResponse: CategoryInquiryResponse) {
    }

    override fun onGetCategoryInquiryFail(message: String) {
    }

}