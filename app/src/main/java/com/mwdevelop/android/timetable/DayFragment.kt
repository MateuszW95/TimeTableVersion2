package com.mwdevelop.android.timetable

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.Adapter
import android.widget.TextView
import java.util.*
import java.util.zip.Inflater
import kotlin.collections.ArrayList

/**
 * Created by mateusz on 25.02.18.
 */
class DayFragment:Fragment(){
    lateinit var mDayNameTextView:TextView
    lateinit var mRecyclerView: RecyclerView
    private var mAdapter: SubjectsAdapter?=null
        companion object {
            private val ARG_DAYID:String="DAY_ID"

            fun newInstance(dayId:UUID):DayFragment{
                var args=Bundle()
                var fragment:DayFragment= DayFragment()
                args.putSerializable(ARG_DAYID,dayId)
                fragment.arguments=args
                return fragment
            }
        }
     protected lateinit var mDay:Day
    override fun onResume() {
        super.onResume()
        updateUI()
    }

    fun updateUI(){
        var subjects:ArrayList<Subject> = mDay.subjectArrayList
        if(mAdapter==null){
            mAdapter= SubjectsAdapter(subjects)
            mRecyclerView.adapter=mAdapter
        }
        else
            mAdapter!!.notifyDataSetChanged()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var dayId:UUID=arguments.getSerializable(ARG_DAYID) as UUID
        var tmp=DataLab.get(activity)
        mDay= DataLab.get(activity).getDay(dayId)!!


    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v:View=inflater!!.inflate(R.layout.day_layout,container,false)
        mDayNameTextView=v.findViewById(R.id.TV_dayName)
        mDayNameTextView.text=mDay.name
        mRecyclerView=v.findViewById(R.id.RV_subjects)
        mRecyclerView.layoutManager=LinearLayoutManager(this.context)

        updateUI()
        return v
    }

    private inner class SubjectHolder(inflater: LayoutInflater?, parent: ViewGroup):RecyclerView.ViewHolder(inflater!!.inflate(R.layout.list_item_subject,parent,false)),View.OnClickListener {
        private lateinit var mNameTextView: TextView
        private lateinit var mLecturerTextView: TextView
        private lateinit var mRoomTextView: TextView
        private lateinit var mBeginTextView: TextView
        private lateinit var mEndTextView: TextView
        private lateinit var mSubject: Subject

        init{
            mNameTextView=itemView.findViewById(R.id.TV_subjectName)
            mLecturerTextView=itemView.findViewById(R.id.TV_lecturerName)
            mRoomTextView=itemView.findViewById(R.id.TV_roomName)
            mBeginTextView=itemView.findViewById(R.id.TV_startTime)
            mEndTextView=itemView.findViewById(R.id.TV_endTime)
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            var intent=SubjectActivity.getIntetnt(v!!.context,mDay.id,mSubject.id)
            startActivity(intent)
        }


        fun Bind(subject:Subject){
            mSubject=subject
            mNameTextView.text=mSubject.name
            mLecturerTextView.text=mSubject.lecturer
            mRoomTextView.text=mSubject.room
            mBeginTextView.text=mSubject.beginTime
            mEndTextView.text=mSubject.endTime


        }
    }

    private inner class SubjectsAdapter(var mSubjects:ArrayList<Subject>): RecyclerView.Adapter<SubjectHolder>() {



        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SubjectHolder {
            var layoutInflater:LayoutInflater= LayoutInflater.from(parent!!.context)
            return SubjectHolder(layoutInflater,parent!!)
        }

        override fun getItemCount(): Int {
            return mSubjects.size
        }

        override fun onBindViewHolder(holder: SubjectHolder?, position: Int) {
            var subject:Subject=mSubjects.get(position)
            holder!!.Bind(subject)

        }

    }



}