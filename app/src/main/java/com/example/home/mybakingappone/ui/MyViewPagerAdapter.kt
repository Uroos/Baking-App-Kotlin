package com.example.home.mybakingappone.ui

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class MyViewPagerAdapter (manager: FragmentManager) : FragmentPagerAdapter(manager) {
    private var mFragmentList: ArrayList<RecipeStepFragment2> = ArrayList()
    //private val mFragmentTitleList: ArrayList<String> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return mFragmentList.get(position)
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragmentList(fragmentList: ArrayList<RecipeStepFragment2>) {
        mFragmentList = fragmentList
        //mFragmentTitleList.add(title)
    }

//    override fun getPageTitle(position: Int): CharSequence? {
//        //return mFragmentTitleList.get(position)
//    }
}