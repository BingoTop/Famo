package com.softsquared.template.kotlin.src.wholeschedule

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityWholeScheduleBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.adapter.MainPagerAdapter
import com.softsquared.template.kotlin.src.main.monthly.MonthlyFragment
import com.softsquared.template.kotlin.src.main.schedulefind.ScheduleFindBookmarkFragment
import com.softsquared.template.kotlin.src.main.schedulefind.ScheduleFindFragment
import com.softsquared.template.kotlin.src.main.schedulefind.ScheduleFindLatelyFragment
import com.softsquared.template.kotlin.src.main.today.TodayFragment
import com.softsquared.template.kotlin.src.wholeschedule.adapter.WholeScheduleAdapter

class WholeScheduleActivity : BaseActivity<ActivityWholeScheduleBinding>
    (ActivityWholeScheduleBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val check = intent.getBooleanExtra("boolean",true)
        Log.d("TAG", "WholeScheduleActivity : $check")

        val scheduleFindBookmarkFragment = ScheduleFindBookmarkFragment()
        val scheduleFindLatelyFragment = ScheduleFindLatelyFragment()
        val bundle = Bundle()
        bundle.getBoolean("boolean",check)
        scheduleFindBookmarkFragment.arguments = bundle
        scheduleFindLatelyFragment.arguments = bundle

        // viewPager
        val adapter = WholeScheduleAdapter(supportFragmentManager)
        adapter.addFragment(scheduleFindBookmarkFragment,"즐겨찾기")
        adapter.addFragment(scheduleFindLatelyFragment,"최근")
        binding.wholeScheduleViewPager.adapter = adapter
        binding.wholeScheduleTabLayout.setupWithViewPager(binding.wholeScheduleViewPager)

        binding.wholeScheduleXBtn.setOnClickListener {
//            val intent = Intent(this,MainActivity::class.java)
//            startActivity(intent)
            finish()
        }
    }

}