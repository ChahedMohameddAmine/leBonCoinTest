package com.leboncoin.albumsample.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.leboncoin.albumsample.presentation.tools.bottomview.BaseBottomAlert
import com.leboncoin.albumsample.presentation.tools.bottomview.SnackBarAlert
import kotlin.properties.Delegates

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!

    open val  bottomViewAlert by Delegates.notNull<BaseBottomAlert>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding()
        return binding.root
    }

    abstract fun getViewBinding(): VB

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}