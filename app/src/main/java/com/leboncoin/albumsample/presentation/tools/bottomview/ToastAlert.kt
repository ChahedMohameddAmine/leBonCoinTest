package com.leboncoin.albumsample.presentation.tools.bottomview

import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class ToastAlert : BaseBottomAlert() {

    private var toast: Toast? = null

    override val LENGTH_SHORT: Int = Snackbar.LENGTH_SHORT
    override val LENGTH_LONG: Int = Snackbar.LENGTH_SHORT


    override fun makeBottomView(view: View, msg: String, durationBottomView: DurationBottomView): ToastAlert {
        super.makeBottomView(view, msg, durationBottomView)
        toast?.cancel()
        toast = Toast.makeText(view.context, msg, super.duration)
        return this
    }

    override fun show(){
        toast?.show()
    }

    override fun getView() = toast
}

