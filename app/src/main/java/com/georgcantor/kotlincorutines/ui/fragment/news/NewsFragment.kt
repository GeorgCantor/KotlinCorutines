package com.georgcantor.kotlincorutines.ui.fragment.news

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.georgcantor.kotlincorutines.R
import com.georgcantor.kotlincorutines.util.Constants.ANIM_PLAYBACK_SPEED
import com.georgcantor.kotlincorutines.util.Constants.ARG_QUERY
import kotlinx.android.synthetic.main.fragment_news.*
import org.koin.android.ext.android.inject

class NewsFragment : Fragment(R.layout.fragment_news) {

    companion object {
        fun create(query: String): NewsFragment {
            return NewsFragment().apply {
                arguments = Bundle().apply { putString(ARG_QUERY, query) }
            }
        }
    }

    private lateinit var mainListAdapter: MainListAdapter

    private val loadingDuration: Long
        get() = (resources.getInteger(R.integer.loadingAnimDuration) / ANIM_PLAYBACK_SPEED).toLong()

    private val viewModel by inject<NewsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val query = arguments?.getString(ARG_QUERY)

        with(viewModel) {
            getNews(query, 1)

            news.observe(viewLifecycleOwner, {
                mainListAdapter = MainListAdapter(requireContext(), it)
                recycler_view.adapter = mainListAdapter
                recycler_view.setHasFixedSize(true)
                updateRecyclerViewAnimDuration()
            })
        }
    }

    private fun updateRecyclerViewAnimDuration() = recycler_view.itemAnimator?.run {
        removeDuration = loadingDuration * 60 / 100
        addDuration = loadingDuration
    }
}