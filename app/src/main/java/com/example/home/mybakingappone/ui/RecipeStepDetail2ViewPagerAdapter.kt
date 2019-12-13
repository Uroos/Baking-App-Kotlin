package com.example.home.mybakingappone.ui

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class RecipeStepDetail2ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
    private var mFragmentList: ArrayList<RecipeStepDetail2Fragment> = ArrayList()
    //private val mFragmentTitleList: ArrayList<String> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return mFragmentList.get(position)
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragmentList(fragmentList: ArrayList<RecipeStepDetail2Fragment>) {
        mFragmentList = fragmentList
        //mFragmentTitleList.add(title)
    }
}