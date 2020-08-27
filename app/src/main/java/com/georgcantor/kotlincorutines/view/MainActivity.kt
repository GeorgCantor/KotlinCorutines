package com.georgcantor.kotlincorutines.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.georgcantor.kotlincorutines.R

var animationPlaybackSpeed: Double = 0.8

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
