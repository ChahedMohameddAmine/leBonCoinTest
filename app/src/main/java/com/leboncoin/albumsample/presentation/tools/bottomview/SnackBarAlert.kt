package com.leboncoin.albumsample.presentation.tools.bottomview

import android.view.View
import com.google.android.material.snackbar.Snackbar

class SnackBarAlert : BaseBottomAlert() {

    private var snackBar: Snackbar? = null

    override val LENGTH_SHORT: Int = Snackbar.LENGTH_SHORT
    override val LENGTH_LONG: Int = Snackbar.LENGTH_LONG

    override fun makeBottomView(view: View, msg: String, durationBottomView: DurationBottomView): SnackBarAlert {
        super.makeBottomView(view, msg, durationBottomView)
        snackBar?.dismiss()
        snackBar =
            Snackbar.make(view, msg ,duration)
                .setDuration(duration)

        return this
    }

    override fun getView() = snackBar

    fun setAction(titleAction: String, onAction: () -> Unit): SnackBarAlert {
        snackBar?.setAction(titleAction) {
            onAction.invoke()
        }
        return this
    }

    override fun show() {
        snackBar?.show()
    }
}

