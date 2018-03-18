package com.mwdevelop.android.timetable

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import java.util.*

/**
 * Created by mateusz on 13.03.18.
 */
class SubjectActivity:SingleFragmentActivity() {
    private lateinit var dID:UUID
    private lateinit var sID:UUID
    override fun onCreate(savedInstanceState: Bundle?) {

        dID=intent.extras.getSerializable(EXTRA_DID) as UUID
        sID=intent.extras.getSerializable(EXTRA_SID) as UUID
        super.onCreate(savedInstanceState)
    }
    override fun createFragment(): Fragment {
        return SubjectFragment.newInstance(dID,sID)
    }

    companion object {
        private val EXTRA_DID:String="com.mwdevelop.android.timetable.DAYID"
        private val EXTRA_SID:String="com.mwdevelop.android.timetable.SUBJECTID"
        fun getIntetnt(context:Context,dID:UUID,sID:UUID):Intent{
            var intent=Intent(context,SubjectActivity::class.java)
            intent.putExtra(EXTRA_DID,dID)
            intent.putExtra(EXTRA_SID,sID)
            return intent
        }
    }
}