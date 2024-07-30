package com.example.emarketapp.utils

import android.app.Activity
import android.content.Context
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

fun Activity.updateStatusBarColor(color: Int) {
    val window: Window = window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = ContextCompat.getColor(this, color)
}

fun <T> StateFlow<T>.pksCollectResult(
    lifecycleScope: LifecycleOwner,
    contextForShowSpinner: Context? = null,
    resultError: ((message: String) -> Any)? = null,
    resultSuccess: (T) -> Unit,
) {
    lifecycleScope.lifecycleScope.launch {
        // repeatOnLifecycle launches the block in a new coroutine every time the
        // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
        lifecycleScope.repeatOnLifecycle(Lifecycle.State.STARTED) {
            // Trigger the flow and start listening for values.
            // Note that this happens when lifecycle is STARTED and stops
            // collecting when the lifecycle is STOPPED

            collect {
                when (it) {
                    is Resource.Loading -> contextForShowSpinner?.let { ctx ->
                        Spinner.show(context = ctx)
                    }

                    is Resource.Failure -> resultError?.let { _ ->
                        Spinner.hide()
                        resultError(it.exceptionMessage)
                    }

                    is Resource.Success<*> -> {
                        Spinner.hide()
                        resultSuccess(it)
                    }
                }
            }
        }
    }
}