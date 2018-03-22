package com.mwdevelop.android.timetable

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_day_pager.*
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader

import java.util.*
import kotlin.collections.ArrayList

@Suppress("UNREACHABLE_CODE")
class DayPagerActivity : AppCompatActivity() {

    private lateinit var mViewPager: ViewPager
    private lateinit var mGroupNameTextView: TextView
    private lateinit var mDays: ArrayList<Day>
    private var NameGroup:String="name"
    private lateinit var URL:String


    private lateinit var mDayNameTextView:TextView
    companion object {
        fun getActivity(URL: String,Name:String,context: Context):Intent{
            var intent=Intent(context,DayPagerActivity::class.java)
            intent.putExtra(EXTRA_URL,URL)
            intent.putExtra(EXTRA_NAME,Name)
            return intent
        }
        private val EXTRA_URL:String="com.mwdevelop.android.timetable.EXTRA_URL"
        private val EXTRA_NAME:String="com.mwdevelop.android.timetable.EXTRA_NAME"
        var context:Context?=null
    }

    lateinit var mDate:Date
    private var mCurrnetDay:Int?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        URL=intent.getStringExtra(EXTRA_URL)
        NameGroup=intent.getStringExtra(EXTRA_NAME)
        setContentView(R.layout.activity_day_pager)
        context=this


        var mCalendar:Calendar= Calendar.getInstance()
        mCurrnetDay=mCalendar.get(Calendar.DAY_OF_WEEK)
        mViewPager=findViewById(R.id.day_view_pager)
        mGroupNameTextView=findViewById(R.id.TV_groupName)
        mGroupNameTextView.text=NameGroup



            try {
                if(!object : DataDownloadTask() {}.execute(URL).get())
                mDays = DataLab.get(this).mDays!!
               // LL_wait.visibility = View.GONE
               // day_view_pager.visibility=View.VISIBLE
            }
            catch (e:Exception)
            {
                Toast.makeText(applicationContext,"Błąd pobierania danych", Toast.LENGTH_LONG).show()

            }

        bt_enter.setOnClickListener(View.OnClickListener {
            if(!ET_data.text.isEmpty())
            {
                DataLab.get(this).mGroupName=ET_data.text.toString()
                try{
                    if(!object : DataDownloadTask() {}.execute(DataLab.get(applicationContext).mGroupName).get()) throw Exception("CONNECTION ERROR")

                    LL_data.visibility=View.GONE
                    LL_list.visibility=View.VISIBLE
                    mDays= DataLab.get(this).mDays!!
                    mViewPager.adapter.notifyDataSetChanged()

                }
                catch(e:Exception){
                    Toast.makeText(this,"Błąd pobierania danych",Toast.LENGTH_LONG).show()
                }
            }
        })

        mDays= DataLab.get(this).mDays!!




        var fragmentManager:FragmentManager=supportFragmentManager

        mViewPager.adapter=object: FragmentStatePagerAdapter(fragmentManager){
            override fun getItem(position: Int): Fragment {
              var day:Day=mDays.get(position)
                return DayFragment.newInstance(day.id)
            }


            override fun getCount(): Int {
                return mDays.size
            }

            override fun getItemPosition(`object`: Any?): Int {
            return PagerAdapter.POSITION_NONE
            }

        }
        //mViewPager.adapter.notifyDataSetChanged()
        if(mCurrnetDay!! ==0 || mCurrnetDay!! >6){
            mViewPager.currentItem = 0
        }
        else
        {
            mViewPager.currentItem = mCurrnetDay!!-1
        }

    }



}
