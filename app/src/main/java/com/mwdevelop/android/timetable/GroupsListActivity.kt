package com.mwdevelop.android.timetable

import android.support.v4.app.Fragment

/**
 * Created by mateusz on 18.03.18.
 */
class GroupsListActivity:SingleFragmentActivity(){
    override fun createFragment(): Fragment {
        return GroupsListFragment.newInstance()
    }
}