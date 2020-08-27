package com.georgcantor.kotlincorutines.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.georgcantor.kotlincorutines.R
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

var animationPlaybackSpeed: Double = 0.8

class MainActivity : AppCompatActivity() {

    private lateinit var mainListAdapter: MainListAdapter

    private val loadingDuration: Long
        get() = (resources.getInteger(R.integer.loadingAnimDuration) / animationPlaybackSpeed).toLong()

    private val viewModel by inject<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        with(viewModel) {
            getNews("USA", 1)

            news.observe(this@MainActivity, Observer {
                mainListAdapter = MainListAdapter(this@MainActivity, it)
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
