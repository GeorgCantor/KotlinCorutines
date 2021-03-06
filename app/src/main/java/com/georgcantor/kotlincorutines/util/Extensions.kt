package com.georgcantor.kotlincorutines.util

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.net.ConnectivityManager
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.georgcantor.kotlincorutines.R
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform

inline fun getValueAnimator(
    forward: Boolean = true,
    duration: Long,
    interpolator: TimeInterpolator,
    crossinline updateListener: (progress: Float) -> Unit
): ValueAnimator {
    val a = if (forward) ValueAnimator.ofFloat(0f, 1f) else ValueAnimator.ofFloat(1f, 0f)

    return a.apply {
        addUpdateListener { updateListener(it.animatedValue as Float) }
        this.duration = duration
        this.interpolator = interpolator
    }
}

fun blendColors(color1: Int, color2: Int, ratio: Float): Int {
    val inverseRatio = 1f - ratio
    val a = (Color.alpha(color1) * inverseRatio) + (Color.alpha(color2) * ratio)
    val r = (Color.red(color1) * inverseRatio) + (Color.red(color2) * ratio)
    val g = (Color.green(color1) * inverseRatio) + (Color.green(color2) * ratio)
    val b = (Color.blue(color1) * inverseRatio) + (Color.blue(color2) * ratio)

    return Color.argb(a.toInt(), r.toInt(), g.toInt(), b.toInt())
}

inline val Int.dp: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics
    ).toInt()
inline val Float.dp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics
    )

inline val Context.screenWidth: Int
    get() = Point().also {
        (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(
            it
        )
    }.x

inline val View.screenWidth: Int
    get() = context!!.screenWidth

fun View.getTransform(mEndView: View) = MaterialContainerTransform().apply {
    startView = this@getTransform
    endView = mEndView
    addTarget(mEndView)
    pathMotion = MaterialArcMotion()
    duration = 550
    scrimColor = Color.TRANSPARENT
}

fun View.setScale(scale: Float) {
    this.scaleX = scale
    this.scaleY = scale
}

fun Context.isNetworkAvailable(): Boolean {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

    return manager?.activeNetworkInfo?.isConnectedOrConnecting ?: false
}

fun Context.loadImage(urlToImage: String?, imageView: ImageView) {
    Glide.with(this)
        .load(urlToImage)
        .placeholder(R.drawable.ic_logo)
        .thumbnail(0.1F)
        .into(imageView)
}

fun AppCompatActivity.openFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction().apply {
        add(R.id.frame_container, fragment)
        addToBackStack(null)
        commit()
    }
}