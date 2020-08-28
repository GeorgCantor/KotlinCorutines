package com.georgcantor.kotlincorutines.ui.fragment.view_pager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.georgcantor.kotlincorutines.R
import com.georgcantor.kotlincorutines.ui.fragment.news.NewsFragment
import com.georgcantor.kotlincorutines.util.Constants.BUSINESS_PAGE
import com.georgcantor.kotlincorutines.util.Constants.SCIENCE_PAGE
import com.georgcantor.kotlincorutines.util.Constants.SPORT_PAGE

class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        BUSINESS_PAGE to { NewsFragment.create(fragment.context?.getString(R.string.business) ?: "") },
        SCIENCE_PAGE to { NewsFragment.create(fragment.context?.getString(R.string.science) ?: "") },
        SPORT_PAGE to { NewsFragment.create(fragment.context?.getString(R.string.sport) ?: "") },
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}