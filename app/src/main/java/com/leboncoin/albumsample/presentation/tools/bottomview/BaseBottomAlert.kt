package com.leboncoin.albumsample.presentation.tools.bottomview

import android.view.View
import kotlin.properties.Delegates

abstract class BaseBottomAlert {

    protected open val LENGTH_SHORT by Delegates.notNull<Int>()
    protected open val LENGTH_LONG  by Delegates.notNull<Int>()

    protected var duration: Int = 0

    private fun setDuration(
        duration: DurationBottomView
    ) {
        val mDuration = when(duration){
            DurationBottomView.SHORT -> LENGTH_SHORT
            DurationBottomView.LONG -> LENGTH_LONG
        }
        this.duration = mDuration
    }

    open fun makeBottomView(
        view: View,
        msg: String,
        durationBottomView: DurationBottomView
    ): BaseBottomAlert {
        setDuration(durationBottomView)
        return this
    }

    abstract fun show()

    abstract fun getView() : Any?


}

enum class DurationBottomView {
    SHORT, LONG
}
