package com.example.emarketapp.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.emarketapp.R


object Spinner {

    private var lottieAnimationView: ProductLottieAnimationView? = null

    fun show(context: Context, isCancelable: Boolean = false, onTouchOutside: Boolean = true) {
        try {
            hide().also {
                context.let { context ->
                    lottieAnimationView = ProductLottieAnimationView(context)
                    lottieAnimationView?.let {
                        it.apply {
                            setCanceledOnTouchOutside(onTouchOutside)
                            setCancelable(isCancelable)
                            show()
                            lottie?.setFailureListener { _ -> }
                            lottie?.setAnimation(R.raw.spinner_animation)
                            lottie?.resumeAnimation()
                            lottie?.playAnimation()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun hide() {
        if (isVisibleLoading) {
            lottieAnimationView = try {
                lottieAnimationView?.dismiss()
                lottieAnimationView?.lottie?.pauseAnimation()
                null
            } catch (e: Exception) {
                null
            }
        }
    }

    @JvmStatic
    val isVisibleLoading get(): Boolean = lottieAnimationView?.isShowing ?: false
}

class ProductLottieAnimationView(context: Context) : Dialog(context) {

    var lottie: LottieAnimationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.spinner)

        lottie = findViewById(R.id.progressBar)
        window?.setLayout(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        window?.setDimAmount(0f)
        window?.setBackgroundDrawableResource(R.color.loading_background)
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = ContextCompat.getColor(context, R.color.transparent)
    }
}
