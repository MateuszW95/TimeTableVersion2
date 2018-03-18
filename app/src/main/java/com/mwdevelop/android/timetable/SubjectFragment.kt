package com.mwdevelop.android.timetable

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import java.util.*

/**
 * Created by mateusz on 13.03.18.
 */
class SubjectFragment:Fragment() {
    lateinit var subject:Subject
    companion object {
        private val ARG_DAYID:String="DAY_ID"
        private  val ARG_SUBID:String="SUBJECT_ID"
        fun newInstance(dayID:UUID,subjectID:UUID):Fragment{
            var fragment=SubjectFragment()
            var bundle=Bundle()
            bundle.putSerializable(ARG_DAYID,dayID)
            bundle.putSerializable(ARG_SUBID,subjectID)
            fragment.arguments=bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var dID:UUID=arguments.getSerializable(ARG_DAYID) as UUID
        var sID:UUID=arguments.getSerializable(ARG_SUBID) as UUID
        var a=DataLab.get(context).mDays

        subject=DataLab.get(context).getDay(dID)!!.getSubject(sID)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    var view:View=inflater!!.inflate(R.layout.fragment_subject,container,false)
        var s_name:EditText=view.findViewById(R.id.ET_sname)
        var s_lec:EditText=view.findViewById(R.id.ET_slecturer)
        var s_room:EditText=view.findViewById(R.id.ET_room)

        s_name.text=SpannableStringBuilder(subject.name)
        s_lec.text=SpannableStringBuilder(subject.lecturer)
        s_room.text=SpannableStringBuilder(subject.room)

        s_name.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                subject.name=s.toString()
            }

        })

        s_lec.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                subject.lecturer=s.toString()
            }

        })

        s_room.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                subject.room=s.toString()
            }


        })

        return view
    }


}