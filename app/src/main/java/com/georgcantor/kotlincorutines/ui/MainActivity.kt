package com.georgcantor.kotlincorutines.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.georgcantor.kotlincorutines.R
import com.georgcantor.kotlincorutines.ui.fragment.home.HomeViewPagerFragment
import com.georgcantor.kotlincorutines.util.openFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openFragment(HomeViewPagerFragment())
    }
}
