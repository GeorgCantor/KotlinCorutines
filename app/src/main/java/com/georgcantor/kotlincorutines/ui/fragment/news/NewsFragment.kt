package com.georgcantor.kotlincorutines.ui.fragment.news

import android.os.Bundle
import android.transition.TransitionManager.beginDelayedTransition
import android.view.View
import androidx.fragment.app.Fragment
import com.georgcantor.kotlincorutines.R
import com.georgcantor.kotlincorutines.model.response.Article
import com.georgcantor.kotlincorutines.util.Constants.ANIM_PLAYBACK_SPEED
import com.georgcantor.kotlincorutines.util.Constants.ARG_QUERY
import com.georgcantor.kotlincorutines.util.EndlessScrollListener
import com.georgcantor.kotlincorutines.util.getTransform
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

    private lateinit var newsAdapter: NewsAdapter

    private val loadingDuration: Long
        get() = (resources.getInteger(R.integer.loadingAnimDuration) / ANIM_PLAYBACK_SPEED).toLong()

    private val viewModel by inject<NewsViewModel>()

    private var isFirstLoad = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val query = arguments?.getString(ARG_QUERY)

        with(viewModel) {
            getNews(query, 1)

            news.observe(viewLifecycleOwner, {
                when (isFirstLoad) {
                    true -> {
                        newsAdapter = NewsAdapter(
                            requireContext(),
                            it as MutableList<Article>
                        ) { startView, endView, rootLayout ->
                            beginDelayedTransition(rootLayout, startView.getTransform(endView))
                        }
                        recycler_view.adapter = newsAdapter
                        recycler_view.setHasFixedSize(true)
                        updateRecyclerViewAnimDuration()
                        isFirstLoad = false
                    }
                    false -> it?.let { newsAdapter.updateArticles(it) }
                }
            })

            val scrollListener = object : EndlessScrollListener() {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    getNews(query, page)
                }
            }

            recycler_view.addOnScrollListener(scrollListener)
        }
    }

    private fun updateRecyclerViewAnimDuration() = recycler_view.itemAnimator?.run {
        removeDuration = loadingDuration * 60 / 100
        addDuration = loadingDuration
    }
}