package com.glivion.plasticdiary.bindings

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.model.User

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("set_icon")
    fun setColor(imageView: ImageView, type: String) {
        if (type == "quiz")
            imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context, R.drawable.ic_quiz_bolt))
        else
            imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context, R.drawable.ic_crown))
    }

    @JvmStatic
    @BindingAdapter("set_profile")
    fun setProfileImage(imageView: ImageView, user: User) {
       Glide.with(imageView.context)
           .load(user.imgUrl)
           .centerCrop()
           .placeholder(R.drawable.profile_picture)
           .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("set_cover_image")
    fun setCoverImage(imageView: ImageView, image: String) {
        Glide.with(imageView.context)
            .load(image)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            //.placeholder(R.drawable.profile_picture)
            .into(imageView)
    }
}