package com.georgcantor.kotlincorutines.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.georgcantor.kotlincorutines.R
import kotlinx.android.synthetic.main.activity_main.*

var animationPlaybackSpeed: Double = 0.8

class MainActivity : AppCompatActivity() {

    private lateinit var mainListAdapter: MainListAdapter
    private val loadingDuration: Long
        get() = (resources.getInteger(R.integer.loadingAnimDuration) / animationPlaybackSpeed).toLong()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainListAdapter = MainListAdapter(this)
        recycler_view.adapter = mainListAdapter
        recycler_view.setHasFixedSize(true)
        updateRecyclerViewAnimDuration()
    }

    private fun updateRecyclerViewAnimDuration() = recycler_view.itemAnimator?.run {
        removeDuration = loadingDuration * 60 / 100
        addDuration = loadingDuration
    }
}
