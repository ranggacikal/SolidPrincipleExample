package com.ranggacikal.solidprincipleexample.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.ranggacikal.solidprincipleexample.core.data.CoroutineSafeJob
import javax.inject.Inject

abstract class BaseFragment<T: ViewBinding> : Fragment() {

    var binding: T by viewBinding()
    lateinit var safeCallJob: CoroutineSafeJob

    var initCustomOnLoadState: (() -> Unit)? = null
    var initCustomOnSuccessState: (() -> Unit)? = null
    var initCustomOnErrorState: (() -> Unit)? = null
    private var onReconnect: (() -> Unit)? = null

    protected abstract fun initView()
    var retryCount = 0
    protected abstract fun initBinding(inflater: LayoutInflater, container: ViewGroup?): T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = initBinding(inflater, container)
        return binding.root
    }

    private fun initCoroutineSafeJob(
        onErrorAction: (() -> Unit)? = null,
        onTimeoutAction: (() -> Unit)? = null,
        onTimeoutCompletion: (() -> Unit)? = null,
        delayAfterInMillis: Long? = null,
    ) {
        safeCallJob = CoroutineSafeJob(context,
            onErrorAction = { onErrorAction?.let { it() } },
            onTimeoutAction = { onTimeoutAction?.let { activity?.runOnUiThread { it() } } },
            onTimeoutCompletion = { onTimeoutCompletion?.let { activity?.runOnUiThread { it() } } }
        ).apply { continueProcess = { true } }

        delayAfterInMillis?.let { safeCallJob.addDelay(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCoroutineSafeJob()
        initView()
    }
}