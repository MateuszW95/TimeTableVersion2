package com.mwdevelop.android.timetable

import android.app.Activity
import android.content.Context
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
    private lateinit var mDays: ArrayList<Day>
    private val fileNameGroup="name"
    private val DataFileName="DataFile"
    private val EditDialog="Edit_Dialog"
    private var REQUEST_CODE=12

    private lateinit var mDayNameTextView:TextView
    companion object {
        var context:Context?=null
    }

    lateinit var mDate:Date
    private var mCurrnetDay:Int?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_pager)
        context=this


        var mCalendar:Calendar= Calendar.getInstance()
        mCurrnetDay=mCalendar.get(Calendar.DAY_OF_WEEK)
        mViewPager=findViewById(R.id.day_view_pager)


        //Informatyka I-go st sem.4 gr. dziekańska 2 lab.4
        if(readNameGroup(fileNameGroup) && readDataFile(DataFileName))
        {
            try {
                if(!object : DataDownloadTask() {}.execute(DataLab.get(applicationContext).mGroupName).get()) throw Exception("CONNECTION ERROR")
                LL_data.visibility = View.GONE
                LL_list.visibility = View.VISIBLE
                mDays = DataLab.get(this).mDays!!
            }
            catch (e:Exception)
            {
                Toast.makeText(applicationContext,"Błąd pobierania danych", Toast.LENGTH_LONG).show()
                LL_data.visibility=View.VISIBLE
                LL_list.visibility=View.GONE
            }
        }
        else
        {
            LL_data.visibility=View.VISIBLE
            LL_list.visibility=View.GONE
        }
        bt_enter.setOnClickListener(View.OnClickListener {
            if(!ET_data.text.isEmpty())
            {
                DataLab.get(this).mGroupName=ET_data.text.toString()
                try{
                    if(!object : DataDownloadTask() {}.execute(DataLab.get(applicationContext).mGroupName).get()) throw Exception("CONNECTION ERROR")
                    writeNameGroup("name",this)
                    writeDataToFile(DataFileName)
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



        refresh_item.setOnClickListener(View.OnClickListener {
            try{
                if(!object : DataDownloadTask() {}.execute(DataLab.get(applicationContext).mGroupName).get()) throw Exception("CONNECTION ERROR")
                writeDataToFile(DataFileName)
                mDays= DataLab.get(this).mDays!!
                mViewPager.adapter.notifyDataSetChanged()
            }
            catch(e:Exception)
            {
                Toast.makeText(this,"Błąd pobierania danych",Toast.LENGTH_LONG).show()
            }
        })




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
    fun writeDataToFile(fileName:String){
       var gson=Gson()
        var data_str=gson.toJson(DataLab.get(applicationContext).mDays)
        try {
            var fileOutputStream: FileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
            fileOutputStream.write(data_str.toByteArray())
            fileOutputStream.close()
            Toast.makeText(applicationContext, "File saved", Toast.LENGTH_LONG).show()
        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun readDataFile(fileName:String):Boolean{
        var message:String?
        try {
            var fileInputStream: FileInputStream = openFileInput(fileName)
            var inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            var stringBuffer = StringBuffer()
            message = bufferedReader.readLine()
            while (message != null) {
                stringBuffer.append(message)
                message = bufferedReader.readLine()
            }
            var gson=Gson()
            var type=object: TypeToken<ArrayList<Day>>(){}.type
            var array:ArrayList<Day> =gson.fromJson(stringBuffer.toString()!!,object: TypeToken<ArrayList<Day>>(){}.type)
            DataLab.get(applicationContext).mDays=array
            return true
        }
        catch(e:Exception){
            e.printStackTrace()
            return false
        }
    }

    fun writeNameGroup(fileName: String,context: Context){
        try {
            var fileOutputStream: FileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
            fileOutputStream.write(DataLab.get(context!!).mGroupName!!.toByteArray())
            fileOutputStream.close()
            Toast.makeText(applicationContext, "File saved", Toast.LENGTH_LONG).show()
        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun readNameGroup(fileName: String):Boolean{
        var message:String?
        try {
            var fileInputStream: FileInputStream = openFileInput(fileName)
            var inputStreamReader = InputStreamReader(fileInputStream)
            var bufferedReader = BufferedReader(inputStreamReader)
            var stringBuffer = StringBuffer()
            message = bufferedReader.readLine()
            while (message != null) {
                stringBuffer.append(message)
                message = bufferedReader.readLine()
            }
            DataLab.get(applicationContext).mGroupName=stringBuffer.toString()
            return true
        }
        catch(e:Exception){
            return false
        }

    }
}
