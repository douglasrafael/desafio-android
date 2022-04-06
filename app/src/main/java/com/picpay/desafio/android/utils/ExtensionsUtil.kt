package com.picpay.desafio.android.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

fun <T> Fragment.collectLifecycleFlow(flow: Flow<T>, collector: FlowCollector<T>) {
    viewLifecycleOwner.lifecycleScope.launch {
        // Safely collect from locationFlow when the lifecycle is STARTED
        // and stops collection when the lifecycle is STOPPED
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(collector)
        }
    }
}