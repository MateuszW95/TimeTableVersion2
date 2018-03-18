package com.mwdevelop.android.timetable

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity

/**
 * Created by mateusz on 13.03.18.
 */
abstract class SingleFragmentActivity:AppCompatActivity() {

    abstract fun createFragment():Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)

        val fm:FragmentManager=supportFragmentManager
        var fragment:Fragment?=fm.findFragmentById(R.id.fragment_container)

        if(fragment==null)
        {
            fragment=createFragment()
            fm.beginTransaction()
                    .add(R.id.fragment_container,fragment)
                    .commit()
        }
    }
}