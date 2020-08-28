package com.georgcantor.kotlincorutines.ui.fragment.view_pager

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.georgcantor.kotlincorutines.R
import com.georgcantor.kotlincorutines.util.Constants.BUSINESS_PAGE
import com.georgcantor.kotlincorutines.util.Constants.SCIENCE_PAGE
import com.georgcantor.kotlincorutines.util.Constants.SPORT_PAGE
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_view_pager.*

class ViewPagerFragment : Fragment(R.layout.fragment_view_pager) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when (view_pager.currentItem) {
                    0 -> activity?.finish()
                    1 -> view_pager.currentItem = 0
                    2 -> view_pager.currentItem = 1
                }
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)

        view_pager.adapter = PagerAdapter(this)

        TabLayoutMediator(tabs, view_pager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }

    private fun getTabTitle(position: Int) = when (position) {
        BUSINESS_PAGE -> getString(R.string.business)
        SCIENCE_PAGE -> getString(R.string.science)
        SPORT_PAGE -> getString(R.string.sport)
        else -> null
    }
}