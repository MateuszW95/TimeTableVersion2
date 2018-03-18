package com.mwdevelop.android.timetable

import android.content.Context
import android.content.res.Resources
import org.jsoup.nodes.Document
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Created by mateusz on 25.02.18.
 */
class DataLab(var context:Context) {
    var mDays:ArrayList<Day>?=null
    var mDaysHM:HashMap<UUID,Day>?=null
    var mDaysName:ArrayList<String>?=null
    var mGroupName:String?=null
    var mTimes:ArrayList<TimeClass>?=null;
    companion object {
        private var sDatalab:DataLab?=null

        fun get(context: Context):DataLab{
            if(sDatalab==null)
            {
                sDatalab= DataLab(context)
            }
            return sDatalab as DataLab
        }

        fun getDataFromURL(URL:String){

            var document:Document
        }
    }
    init {
         mDays = ArrayList<Day>()
         mDaysHM = HashMap<UUID,Day>()
         mDaysName = ArrayList<String>()
        mTimes= ArrayList<TimeClass>()


        mDaysName!!.add(context.resources.getString(R.string.Monday))
        mDaysName!!.add(context.resources.getString(R.string.Tuesday))
        mDaysName!!.add(context.resources.getString(R.string.Wednesday))
        mDaysName!!.add(context.resources.getString(R.string.Thursday))
        mDaysName!!.add(context.resources.getString(R.string.Friday))

        for(i in 8..19)
        {
            var b=i.toString()+":00"
            var e=(i+1).toString()+":00"
            var tmp=TimeClass(b,e)
            mTimes!!.add(tmp)

        }

        for( i in 0..4){
            var tmp=Day()
            tmp.name= mDaysName!![i]
            mDays!!.add(tmp)
            mDaysHM!!.put(tmp.id,tmp)
        }

    }

    fun getDay(dayId:UUID):Day?{
        return mDaysHM!!.get(dayId)
    }
    fun setDayArray(p:ArrayList<Day>){
        mDays=p
        mDaysHM!!.clear()
        for( i in 0..4){

            mDaysHM!!.put(mDays!![i].id, mDays!![i])
        }
    }







}