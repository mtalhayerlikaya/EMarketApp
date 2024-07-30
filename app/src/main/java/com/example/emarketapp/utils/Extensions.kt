package com.example.emarketapp.utils

import android.app.Activity
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat

fun Activity.updateStatusBarColor(color: Int) {
    val window: Window = window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = ContextCompat.getColor(this, color)
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}