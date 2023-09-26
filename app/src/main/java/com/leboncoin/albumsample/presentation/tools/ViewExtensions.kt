package com.leboncoin.albumsample.presentation.tools

import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.core.view.isVisible
import kotlinx.coroutines.delay

suspend fun View.showBriefly(delay: Long = 1500) {
    this.isVisible = true
    delay(delay)
    this.isVisible = false
}

fun ImageView.animateIconAddToFavorite(
    resource: Int,
    animDuration: Long = 500,
    scaleValue: Float = 1.5f,
    withoutAnimation: Boolean = false
) {
    if (withoutAnimation) {
        setImageResource(resource)
        return
    }

    animate().scaleX(1f).scaleY(1f).rotationY(0f).setDuration(0L).start()
    animate().apply {
        duration = animDuration
        rotationY(360f)
        interpolator = DecelerateInterpolator(1.5f)
    }.withStartAction {
        animate().apply {
            duration = animDuration / 2
            scaleX(scaleValue)
            scaleY(scaleValue)
            interpolator = DecelerateInterpolator(1.5f)
        }.withEndAction {
            animate().apply {
                duration = animDuration / 2
                scaleX(1f)
                scaleY(1f)
                interpolator = DecelerateInterpolator(1.5f)
            }.start()
        }.start()
    }.start()
    setImageResource(resource)
}


fun ImageView.animateIconDeleteFavorite(
    resource: Int, withoutAnimation: Boolean = false
) {
    if (withoutAnimation) {
        setImageResource(resource)
        return
    }
    animate().scaleX(1f).scaleY(1f).rotationY(0f).setDuration(0L).start()
    animate().apply {
        duration = 500
        rotationY(-180f)
    }.start()
    setImageResource(resource)
}