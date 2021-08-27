package com.engineerskasa.plasticdiary.bindings

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.engineerskasa.plasticdiary.R

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("set_icon")
    fun setColor(imageView: ImageView, type: String) {
        if (type == "quiz")
            imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context, R.drawable.ic_quiz_bolt))
        else
            imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context, R.drawable.ic_crown))
    }
}