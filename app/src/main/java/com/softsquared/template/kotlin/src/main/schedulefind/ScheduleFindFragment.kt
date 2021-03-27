package com.softsquared.template.kotlin.src.main.schedulefind

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.lakue.pagingbutton.OnPageSelectListener
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.databinding.FragmentScheduleFindBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.category.CategoryEditActivity
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.*
import com.softsquared.template.kotlin.src.main.schedulefind.models.*
import com.softsquared.template.kotlin.src.searchhistories.ScheduleSearchActivity
import com.softsquared.template.kotlin.src.wholeschedule.models.LatelyScheduleInquiryResponse
import com.softsquared.template.kotlin.util.Constants
import java.util.*

class ScheduleFindFragment() : BaseFragment<FragmentScheduleFindBinding>
    (FragmentScheduleFindBinding::bind, R.layout.fragment_schedule_find),
    IScheduleCategoryRecyclerView, CategoryInquiryView, ScheduleFindView,
    ScheduleBookmarkView {

    //카테고리 편집으로 보내줄 변수
    var name = ""
    var color = ""
    var size = 0
    var categoryID = ""

    //전체페이징수
    var wholePagingCnt = 0

    var pagingCnt = 1

    private lateinit var scheduleCategoryAdapter: ScheduleCategoryAdapter

    // 전체 일정 어댑터
    private lateinit var scheduleWholeAdapter: ScheduleWholeAdapter

    private val partList: ArrayList<ScheduleWholeData> = arrayListOf()

    @SuppressLint("ResourceAsColor", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        ScheduleFindService(this).tryGetWholeScheduleCount()



//         검색
//        if (word != null){
//            word = ApplicationClass.sSharedPreferences.getString(Constants.SEARCHWROD,null)!!
////            ScheduleFindService(this).tryGetScheduleSearch(searchWord)
//        }


        val searchWord = ""

        //앞으로 내보내기
        binding.scheduleFindTvTotaySchedule.bringToFront()

        val token =
            ApplicationClass.sSharedPreferences.getString(ApplicationClass.X_ACCESS_TOKEN, null)
                .toString()
        Log.d("TAG", "일정찾기 홈 $token")


        //프래그먼트 이동간 gone/visibility설정
        (activity as MainActivity).fragmentSetting()

//        binding.scheduleFindMainLinear.visibility = View.VISIBLE
//        binding.scheduleFindMainFragment.visibility = View.GONE

//        레이아웃 마진 설정
//        val layout1 = binding.scheduleFindLinear
//        val plControl = layout1.layoutParams as RelativeLayout.LayoutParams
//        plControl.topMargin = 30
//        layout1.layoutParams = plControl
//        binding.scheduleFindLinear.marginTop.plus(30)

        // 즐겨찾기탭의 height 초기 값을 4로 지정
//        binding.scheduleFindBookmarkView.layoutParams.height = 4

        // 일정찾기 탭 default설정 - 처음에는 즐겨찾기탭이 보이게, 카테고리 설정
//        childFragmentManager.beginTransaction()
//            .replace(R.id.schedule_find_fragment, ScheduleFindBookmarkFragment())
//            .commit()

        // 처음 시작은 즐겨찾기/최근 중 즐겨찾기로 선택되게끔
//        binding.scheduleFindTvBookmark.setTextColor(Color.BLACK)
//        binding.scheduleFindLatelyView.visibility = View.GONE


//        binding.scheduleFindNoCategory.setOnClickListener {
//
//            onItemMoveBtnClicked(0)
//        }

        // +버튼 클릭 시 카테고리 편집으로 이동
        binding.scheduleFindBtnCategory.setOnClickListener {
            val intent = Intent(activity, CategoryEditActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("color", color)
            intent.putExtra("size", size)
            intent.putExtra("categoryID", categoryID)
            startActivity(intent)
//            (activity as MainActivity).replaceFragment(CategoryEditFragment.newInstance());
//            binding.scheduleFindLinear.visibility = View.GONE
        }


        // 즐겨찾기 리사이클러뷰
//        createBookmarkRecyclerview()
//        val mFragmentTransaction : FragmentTransaction = childFragmentManager.beginTransaction();

//        (activity as MainActivity).replaceFragment(ScheduleFindBookmarkFragment.newInstance());


        binding.scheduleFindBtn.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->

            val searchWord = binding.scheduleFindBtn.text.toString()

            val edit = ApplicationClass.sSharedPreferences.edit()
            edit.putString(Constants.SEARCHWROD,searchWord)
            edit.apply()

            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    showCustomToast("aaaaaa")
                    Log.d("TAG", "ScheduleFindFragment:검색조회 ")

                    binding.scheduleFindMainLinear.visibility = View.GONE
                    binding.scheduleFindMainFragment.visibility = View.VISIBLE
                    val scheduleFindCategoryFragment = ScheduleFindCategoryFragment()
                    val bundle = Bundle()
                    bundle.putString("searchWord", searchWord)
                    scheduleFindCategoryFragment.arguments = bundle
                    childFragmentManager.beginTransaction()
                        .replace(R.id.schedule_find_main_fragment, scheduleFindCategoryFragment)
                        .commit()

//                    ScheduleFindService(this).tryGetScheduleSearch(scheduleSearchRequest)
                }
            }
//
            false
        })
        //검색창 클릭 시
        binding.scheduleFindIvSearch.setOnClickListener {
//            val searchWord = binding.scheduleFindBtn.text.toString()
//
//            val scheduleFindCategoryFragment = ScheduleFindCategoryFragment()
//            val bundle = Bundle()
//            bundle.putString("searchWord", searchWord)
//            scheduleFindCategoryFragment.arguments = bundle
//            childFragmentManager.beginTransaction()
//                .replace(R.id.schedule_find_main_fragment, scheduleFindCategoryFragment)
//                .commit()
//            val word = 1
//
//            val scheduleSearchRequest = ScheduleSearchRequest(
//                searchWord = word.toString()
//            )
            //검색
//            ScheduleFindService(this).tryGetScheduleSearch(scheduleSearchRequest)
            val intent = Intent(activity, ScheduleSearchActivity::class.java)
            startActivity(intent)
        }


        //즐겨찾기 탭
//        binding.scheduleFindTvBookmark.setOnClickListener {
//            binding.scheduleFindTvBookmark.setTextColor(Color.BLACK)
//            binding.scheduleFindTvBookmark.setTypeface(null, Typeface.BOLD)
//            binding.scheduleFindBookmarkView.setBackgroundColor(Color.BLACK)
//            binding.scheduleFindBookmarkView.layoutParams.height = 4
//
//            binding.scheduleFindTvLately.setTextColor(Color.GRAY)
//            binding.scheduleFindTvLately.setTypeface(null, Typeface.NORMAL)
//            binding.scheduleFindLatelyView.setBackgroundColor(Color.parseColor("#E1DDDD"))
//            binding.scheduleFindLatelyView.layoutParams.height = 2
//
//            childFragmentManager.beginTransaction().replace(
//                R.id.schedule_find_fragment, ScheduleFindBookmarkFragment()
//            ).commit()
//////            (activity as MainActivity).replaceFragment(ScheduleFindBookmarkFragment.newInstance());
//        }

        //최근 탭
//        binding.scheduleFindTvLately.setOnClickListener {
//            binding.scheduleFindTvLately.setTextColor(Color.BLACK)
//            binding.scheduleFindTvLately.setTypeface(null, Typeface.BOLD)
//            binding.scheduleFindLatelyView.setBackgroundColor(Color.BLACK)
//            binding.scheduleFindLatelyView.layoutParams.height = 4
//
//            binding.scheduleFindTvBookmark.setTextColor(Color.GRAY)
//            binding.scheduleFindTvBookmark.setTypeface(null, Typeface.NORMAL)
//            binding.scheduleFindBookmarkView.setBackgroundColor(Color.parseColor("#E1DDDD"))
//            binding.scheduleFindBookmarkView.layoutParams.height = 2
//
////            ScheduleFindService(this).tryGetLatelyScheduleFindInquiry(0,2)
//            childFragmentManager.beginTransaction().replace(
//                R.id.schedule_find_fragment, ScheduleFindLatelyFragment()).commit()
////            (activity as MainActivity).replaceFragment(ScheduleFindLatelyFragment.newInstance());
//        }

        //자세히 보기 클릭 시
//        binding.scheduleFindBtnDetail.setOnClickListener {
            val bundle = Bundle()
            val scheduleFindDetailFragment = ScheduleFindDetailFragment()

            // 즐겨찾기가 선택되어 있는 경우
//            if (binding.scheduleFindBookmarkView.layoutParams.height == 4) {
//
//                binding.scheduleFindLinear.visibility = View.GONE
////                ApplicationClass.sSharedPreferences.edit().putBoolean("boolean", true).apply()
////                (activity as MainActivity).replaceFragment(ScheduleFindDetailFragment.newInstance());
//                val intent = Intent(activity, WholeScheduleActivity::class.java)
//                Log.d("TAG", "즐겨찾기 선택 유무")
//                intent.putExtra("boolean", true)
//                startActivity(intent)
//            }

            //최근이 선택되어 있는 경우
//            if (binding.scheduleFindLatelyView.layoutParams.height == 4) {
//                binding.scheduleFindLinear.visibility = View.GONE
//                val intent = Intent(activity, WholeScheduleActivity::class.java)
//                Log.d("TAG", "최근 선택 유무")
//                intent.putExtra("boolean", false)
//                startActivity(intent)
//            }

//            binding.scheduleFindIvSearch.setOnTouchListener { _, event ->
//                when (event.action) {
//                    MotionEvent.ACTION_DOWN -> {
//                        Log.d("TAG", "일정찾기 이미지클릭 확인 ")
//                        binding.scheduleFindMainFragment.visibility = View.GONE
//                        binding.scheduleFindMainLinear.visibility = View.VISIBLE
//                    }
//                }
//                false
//            }

//        }

        //한 번에 표시되는 버튼 수 (기본값 : 5)
        binding.scheduleFindPaging.setPageItemCount(4);

        //총 페이지 버튼 수와 현재 페이지 설정
//        Log.d("TAG", "wholePagingCnt : ${wholePagingCnt} ")
        binding.scheduleFindPaging.addBottomPageButton(10, 1);

        //페이지 리스너를 클릭했을 때의 이벤트
        binding.scheduleFindPaging.setOnPageSelectListener(object : OnPageSelectListener {
            //PrevButton Click
            override fun onPageBefore(now_page: Int) {
                //prev 버튼을 클릭하면 버튼이 재설정되고 버튼이 그려집니다.
                binding.scheduleFindPaging.addBottomPageButton(10, now_page)
                ScheduleFindService(this@ScheduleFindFragment).tryGetWholeScheduleInquiry(
                    ((now_page - 1)), 1
                )
            }

            override fun onPageCenter(now_page: Int) {
//                binding.recyclerviewWhole.visibility = View.GONE
//                setFrag(Integer.parseInt(now_page.toString())-1)

//                for (i in 1 until 4) {
//                    if (now_page == i) {
//                        ScheduleFindService(this@ScheduleFindFragment).tryGetWholeScheduleInquiry(
//                            ((i - 1) * 10), 10)
//                    }
//                }
                ScheduleFindService(this@ScheduleFindFragment).tryGetWholeScheduleInquiry(
                    ((now_page - 1) * 1), 1
                )

            }

            //NextButton Click
            override fun onPageNext(now_page: Int) {
                //next 버튼을 클릭하면 버튼이 재설정되고 버튼이 그려집니다.
                binding.scheduleFindPaging.addBottomPageButton(10, now_page)
                ScheduleFindService(this@ScheduleFindFragment).tryGetWholeScheduleInquiry(
                    ((now_page - 1) * 1), 1
                )

            }
        })

    }


    override fun viewPagerApiRequest() {
        super.viewPagerApiRequest()
        //카테고리
        CategoryInquiryService(this).tryGetUserCategoryInquiry()
        //전체일정
        ScheduleFindService(this).tryGetWholeScheduleInquiry(0, 1)
        //최근일정
        ScheduleFindService(this).tryGetLatelyScheduleFindInquiry(0, 2)
        //즐겨찾기
        ScheduleBookmarkService(this).tryGetScheduleBookmark(0, 2)
        //남은일정
        ScheduleFindService(this).tryGetRestScheduleCount("today")

    }

    //프래그먼트 간 이동하기 위한 선언
    companion object {
        fun newInstance(): ScheduleFindFragment {    // shs: 함수의 반환 형이 Fragment 형이라...
            return ScheduleFindFragment()
        }
    }

    //카테고리 클릭 시 카테고리별 일정으로 이동
    override fun onItemMoveBtnClicked(position: Int, scheduleCategoryID : Int) {
        binding.scheduleFindMainLinear.visibility = View.GONE
        binding.scheduleFindMainFragment.visibility = View.VISIBLE
        Log.d("TAG", "onItemMoveBtnClicked: $position")
//        CategoryInquiryService(this).tryGetCategoryInquiry()
        val scheduleFindCategoryFragment = ScheduleFindCategoryFragment()
        val bundle = Bundle()
        bundle.putInt("categoryID", position)
        bundle.putInt("scheduleCategoryID", scheduleCategoryID)
        scheduleFindCategoryFragment.arguments = bundle
        childFragmentManager.beginTransaction()
            .replace(R.id.schedule_find_main_fragment, scheduleFindCategoryFragment)
            .commit()
    }

    override fun onMoveFilterFragment(scheduleCategoryID : Int) {
        val scheduleFindFilterBottomDialogBinding = SchedulefindFilterBottomDialogFragment(scheduleCategoryID)
        scheduleFindFilterBottomDialogBinding.show(
            fragmentManager!!, scheduleFindFilterBottomDialogBinding.tag
        )
    }

    //어댑터에서 color값을 가져오기위한 함수
    override fun onColor(): String {

        return color
    }

    //유저별 카테고리조회 성공
    override fun onGetUserCategoryInquirySuccess(responseUser: UserCategoryInquiryResponse) {
        when (responseUser.code) {
            100 -> {
                Log.d("TAG", "onGetCategoryInquirySuccess: 카테고리조회성공")
                val categoryList: ArrayList<ScheduleCategoryData> = arrayListOf()

                for (i in 0 until responseUser.data.size) {
                    categoryList.add(
                        ScheduleCategoryData(
                            responseUser.data[i].categoryID,
                            responseUser.data[i].categoryName,
                            "#00000000",
                            false

                        )
                    )
//                    #00000000

                    name += responseUser.data[i].categoryName + ":"
                    color += responseUser.data[i].colorInfo + ":"
                    size = responseUser.data.size

                    categoryID += "${responseUser.data[i].categoryID}:"
                }

                Log.d("TAG", "name: $name")
                Log.d("TAG", "color: $color")
                binding.recyclerviewCategory.layoutManager = LinearLayoutManager(
                    context, LinearLayoutManager.HORIZONTAL, false
                )
                binding.recyclerviewCategory.setHasFixedSize(true)
                binding.recyclerviewCategory.adapter = ScheduleCategoryAdapter(categoryList, this)
//                scheduleCategoryAdapter.notifyDataSetChanged()

            }
            else -> {
                showCustomToast("실패 메시지 : ${responseUser.message}")
            }
        }
    }

    override fun onGetUserCategoryInquiryFail(message: String) {
    }

    override fun onGetCategoryInquirySuccess(categoryInquiryResponse: CategoryInquiryResponse) {

    }

    override fun onGetCategoryInquiryFail(message: String) {
    }

    //전체일정조회 성공
    override fun onGetWholeScheduleInquirySuccess(response: WholeScheduleInquiryResponse) {

        when (response.code) {
            100 -> {
                Log.d("TAG", "onGetWholeScheduleInquirySuccess 성공")
                binding.recyclerviewWhole.visibility = View.VISIBLE

//                val wholeScheduleJsonArray = response.data
//                Log.d("TAG", "wholeScheduleJsonArray: $wholeScheduleJsonArray")
//                wholeScheduleJsonArray.forEach {
//                    val wholeScheduleJsonObject = it.asJsonObject
//                    val scheduleDate = wholeScheduleJsonObject.get("scheduleDate").asString
//                    val scheduleName = wholeScheduleJsonObject.get("scheduleName").asString
//                    //  일정 내용을 그냥 작성 안 하고 작성할 경우도 인지해야합니다. null로 들어올 수 있어요.
//                    val scheduleContentJson:JsonElement? = wholeScheduleJsonObject.get("scheduleMemo")
//                    val schedulePick = wholeScheduleJsonObject.get("schedulePick").asInt
//                    // 컬러는 카테고리가 없이 작성이 가능해서 null로 들어올 수 있습니다.
//                    val scheduleColorInfoJson:JsonElement? = wholeScheduleJsonObject.get("colorInfo")
//                    val scheduleStatus = wholeScheduleJsonObject.get("scheduleStatus").asInt
//
//                    val scheduleID = wholeScheduleJsonObject.get("scheduleID").asInt
//                    var scheduleColorInfo = "#CED5D9"
//                    var scheduleContent = ""
//
//                    // json이기 때문에 그냥 null 체크하면 null인지 모르기 때문에 isJsonNull을 써주세요.
//                    if(scheduleColorInfoJson?.isJsonNull!!.equals(false)){
//                        // 널이 아닌경우 그대로 컬러 스트링을 넣어줍니다.
//                        scheduleColorInfo = scheduleColorInfoJson.asString
//                    }
//                    if(scheduleContentJson?.isJsonNull!!){
//                        // 마찬가지로 일정 내용이 있으면 수정하고 없으면 위에 빈 스트링으로 일정에 넣어줍니다.
//                        scheduleContent = scheduleContentJson.asString
//                    }
//                    partList.add(ScheduleWholeData(scheduleID,scheduleDate,scheduleName,scheduleContent,schedulePick,scheduleStatus,scheduleColorInfo))
//                }


//                Log.d("TAG", "길이 : ${response.data.size} ")
//                if (pagingCnt == 1) {
//                    wholePagingCnt = (response.data.size / 10) + 1
//                    //총 페이지 버튼 수와 현재 페이지 설정
//                    Log.d("TAG", "처음 한번만 wholePagingCnt : ${wholePagingCnt} ")
//                    binding.scheduleFindPaging.addBottomPageButton(wholePagingCnt, 1);
//                    pagingCnt++
//                }

                val wholeScheduleList: ArrayList<ScheduleWholeData> = arrayListOf()

                if (response.data.size > 0) {

                    for (i in 0 until response.data.size) {
                        //즐겨찾기 X and 카테고리 O인경우
                        if (response.data[i].schedulePick == -1 && response.data[i].colorInfo == null) {
                            wholeScheduleList.add(
                                ScheduleWholeData(
                                    response.data[i].scheduleID,
                                    response.data[i].scheduleDate,
                                    response.data[i].scheduleName,
                                    response.data[i].scheduleMemo,
                                    R.drawable.schedule_find_inbookmark,
                                    response.data[i].scheduleStatus,
                                    "#CED5D9"
                                )
                            )
//                            "#CED5D9"
                            //즐겨찾기X and 카테고리O
                        } else if (response.data[i].schedulePick == -1 && response.data[i].colorInfo != null) {
                            wholeScheduleList.add(
                                ScheduleWholeData(
                                    response.data[i].scheduleID,
                                    response.data[i].scheduleDate,
                                    response.data[i].scheduleName,
                                    response.data[i].scheduleMemo,
                                    R.drawable.schedule_find_inbookmark,
                                    response.data[i].scheduleStatus,
                                    response.data[i].colorInfo
                                )
                            )
                        }
                        //즐겨찾기O and 카테고리 X
                        else if (response.data[i].schedulePick == 1 && response.data[i].colorInfo == null) {
                            wholeScheduleList.add(
                                ScheduleWholeData(
                                    response.data[i].scheduleID,
                                    response.data[i].scheduleDate,
                                    response.data[i].scheduleName,
                                    response.data[i].scheduleMemo,
                                    R.drawable.schedule_find_bookmark,
                                    response.data[i].scheduleStatus,
                                    "#CED5D9"
                                )
                            )
                        }
                        //즐겨찾기 O and 카테고리 O
                        else if (response.data[i].schedulePick == 1 && response.data[i].colorInfo != null) {
                            wholeScheduleList.add(
                                ScheduleWholeData(
                                    response.data[i].scheduleID,
                                    response.data[i].scheduleDate,
                                    response.data[i].scheduleName,
                                    response.data[i].scheduleMemo,
                                    R.drawable.schedule_find_bookmark,
                                    response.data[i].scheduleStatus,
                                    response.data[i].colorInfo
                                )
                            )
                        }
                    }


                }


                scheduleWholeAdapter = ScheduleWholeAdapter(wholeScheduleList)

                //전체일정 리사이큘러뷰 연결
                binding.recyclerviewWhole.layoutManager =
                    GridLayoutManager(
                        context, 2, GridLayoutManager.VERTICAL,
                        false
                    )
                binding.recyclerviewWhole.setHasFixedSize(true)
                binding.recyclerviewWhole.adapter = scheduleWholeAdapter


            }
            else -> {
                Log.d(
                    "TAG",
                    "onGetWholeScheduleInquirySuccess 100이 아닌: ${response.message.toString()}"
                )
            }
        }

    }

    override fun onGetWholeScheduleInquiryFail(message: String) {
    }

    override fun onPostBookmarkSuccess(response: BaseResponse) {

        when (response.code) {
            100 -> {
                showCustomToast("즐겨찾기 성공")
                Log.d("TAG", "onPostBookmarkSuccess: 즐겨찾기 성공")
            }
            else -> {
                showCustomToast("즐겨찾기 실패")
                Log.d("TAG", "onPostBookmarkSuccess: 즐겨찾기 실패 ${response.message.toString()}")
            }
        }
    }

    override fun onPostBookmarkFail(message: String) {
    }

    override fun onGetWholeScheduleCountSuccess(response: WholeScheduleCountResponse) {

        when (response.code) {
            100 -> {
                showCustomToast("전체일정수 조회 성공")
                val cnt: Int = response.totaldata[0].totalScheduleCount
                val cnt2: Int = response.totaldonedata[0].totalScheduleCount
                Log.d("TAG", "onGetWholeScheduleCountSuccess - 전체일정수 - $cnt")
                Log.d("TAG", "onGetWholeScheduleCountSuccess - 전체 해낸일정수 - $cnt2")

                //페이징수 세팅
//                wholePagingCnt = (cnt / 10) + 1
//                binding.scheduleFindPaging.addBottomPageButton(wholePagingCnt, 1);
            }
            else -> {
                showCustomToast("즐겨찾기 실패")
                Log.d("TAG", "onPostBookmarkSuccess: 즐겨찾기 실패 ${response.message.toString()}")
            }
        }
    }

    override fun onGetWholeScheduleCountFailure(message: String) {
    }

    override fun onGetLatelyScheduleFindInquirySuccess(response: LatelyScheduleInquiryResponse) {

//        when(response.code){
//            100 -> {
//                showCustomToast("즐겨찾기 성공")
//                Log.d("TAG", "onGetLatelyScheduleInquirySuccess: 최근일정조회성공")
//
//                val latelyList = arrayListOf(
//                    ScheduleBookmarkData("최근제목", "최근시간"),
//                    ScheduleBookmarkData("최근제목", "최근시간")
//                )
//
//                // 즐겨찾기/최근 일정 리사이클러뷰 연결
//                binding.recyclerViewLately.layoutManager = LinearLayoutManager(
//                    context, LinearLayoutManager.VERTICAL, false
//                )
//                binding.recyclerViewLately.setHasFixedSize(true)
//                binding.recyclerViewLately.adapter = ScheduleBookmarkAdapter(latelyList)
//
//            }
//            else -> {
//                showCustomToast("즐겨찾기 실패")
//                Log.d("TAG", "onGetLatelyScheduleInquirySuccess: 최근일정조회실패 - ${response.message.toString()}")
//            }
//        }

    }

    override fun onGetLatelySchedulefindInquiryFail(message: String) {
    }

    @SuppressLint("SetTextI18n")
    override fun onGetTodayRestScheduleSuccess(response: TodayRestScheduleResponse) {

        when (response.code) {
            100 -> {
                showCustomToast("남은일정조회성공")
                Log.d("TAG", "onGetTodayRestScheduleSuccess: 남은일정조회성공")
                binding.scheduleFindTvTotaySchedule.text = " 일정이 " +
                        response.data[0].remainScheduleCount.toString() +
                        "개 "

            }
            else -> {
                showCustomToast("즐겨찾기 실패")
                Log.d(
                    "TAG",
                    "onGetTodayRestScheduleSuccess: 남은일정조회성공 ${response.message.toString()}"
                )
            }
        }

    }

    override fun onGetTodayRestScheduleFail(message: String) {
    }

    override fun onGetScheduleSearchSuccess(response: ScheduleSearchResponse) {

        when (response.code) {
            100 -> {
                showCustomToast("검색 성공")
                Log.d("TAG", "onGetScheduleSearchSuccess: 검색 성공")
            }
            else -> {
                showCustomToast("검색 성공")
                Log.d("TAG", "onGetScheduleSearchSuccess: 검색 실패 ${response.message.toString()}")
            }
        }
    }

    override fun onGetScheduleSearchFail(message: String) {
    }

    override fun onGetScheduleBookmarkSuccess(response: ScheduleBookmarkResponse) {

        when (response.code) {
            100 -> {
                showCustomToast("즐겨찾기 일정조회성공")
                Log.d("TAG", "onGetScheduleBookmarkSuccess: 즐겨찾기 일정조회성공")

                val boomarkList: ArrayList<WholeScheduleBookmarkData> = arrayListOf()

                for (i in 0 until response.data.size) {
                    boomarkList.add(
                        WholeScheduleBookmarkData(
                            response.data[i].scheduleID,
                            response.data[i].scheduleDate,
                            response.data[i].scheduleName,
                            response.data[i].scheduleMemo,
                            response.data[i].schedulePick,
                            10,
                            "#CED5D9"
                        )
                    )
                }

//                binding.recyclerViewBookmark.layoutManager = LinearLayoutManager(
//                    context, LinearLayoutManager.VERTICAL, false
//                )
//                binding.recyclerViewBookmark.setHasFixedSize(true)
//                binding.recyclerViewBookmark.adapter = ScheduleBookmarkAdapter(boomarkList)
//                scheduleCategoryAdapter.notifyDataSetChanged()


            }
            else -> {
                showCustomToast("즐겨찾기 실패")
                Log.d(
                    "TAG",
                    "onGetScheduleBookmarkSuccess: 즐겨찾기 일정조회성공 ${response.message.toString()}"
                )
            }
        }

    }

    override fun onGetScheduleBookmarkFail(message: String) {
    }

    override fun onPause() {
        super.onPause()
        Log.d("TAG", "onPause: 확인")
        binding.scheduleFindMainLinear.visibility = View.VISIBLE
        binding.scheduleFindMainFragment.visibility = View.GONE

        CategoryInquiryService(this).tryGetUserCategoryInquiry()
    }


    override fun onResume() {
        super.onResume()

        val word : String? = ApplicationClass.sSharedPreferences.getString(Constants.SEARCHWROD,null)
        Log.d("TAG", "onResume: ㅇㅇㅇㅇ $word")

        if (word != null){
            Log.d("TAG", "onResume: 안 $word")

            if (word!!.length > 0){
                binding.scheduleFindMainLinear.visibility = View.GONE
                binding.scheduleFindMainFragment.visibility = View.VISIBLE
                val scheduleFindCategoryFragment = ScheduleFindCategoryFragment()
                childFragmentManager.beginTransaction()
                    .replace(R.id.schedule_find_main_fragment, scheduleFindCategoryFragment)
                    .commit()
            }
        }

    }

//    override fun onGetLatelyScheduleInquirySuccess(response: LatelyScheduleInquiryResponse) {
//    }
//
//    override fun onGetLatelyScheduleInquiryFail(message: String) {
//    }
}