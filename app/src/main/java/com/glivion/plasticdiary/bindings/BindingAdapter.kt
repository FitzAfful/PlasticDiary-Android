package com.glivion.plasticdiary.bindings

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.model.User
import com.glivion.plasticdiary.util.getYoutubeThumbnailUrlFromVideoUrl
import com.google.android.material.textview.MaterialTextView

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
    fun setCoverImage(imageView: ImageView, image: String?) {
        Glide.with(imageView.context)
            .load(image)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            //.placeholder(R.drawable.profile_picture)
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("set_youtube_image")
    fun setYoutubePlaceholder(imageView: ImageView, image: String?) {
        Glide.with(imageView.context)
            .load(getYoutubeThumbnailUrlFromVideoUrl(image))
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            //.placeholder(R.drawable.profile_picture)
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("highlight_answer")
    fun highlightAnswer(textView: MaterialTextView, answer: String) {
        if (textView.text.toString() == answer) {
            textView.setTextColor(
                ColorStateList.valueOf(
                ContextCompat.getColor(
                    textView.context,
                    R.color.heading_text_green
                )
            ))
        }
    }
}