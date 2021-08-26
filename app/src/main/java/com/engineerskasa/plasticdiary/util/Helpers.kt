package com.engineerskasa.plasticdiary.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.engineerskasa.plasticdiary.R
import com.google.android.material.snackbar.Snackbar

fun hideKeyboard(context: Activity) {
    val imm = context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    var currentFocus = context.currentFocus
    if (currentFocus == null) {
        currentFocus = View(context)
    }
    imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
}

fun setSystemBarColor(act: Activity, @ColorRes color: Int) {
    val window = act.window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.statusBarColor = ContextCompat.getColor(act, color)
}

fun getStringFromRes(context: Context?, label: Int): String? {
    return if (context == null || label == 0) null else context.resources.getString(label)
}

fun showSnackBarMessage(view: View?, message: String?) {
    val snackbar = Snackbar.make(view!!, message!!, Snackbar.LENGTH_LONG)
    val sbView = snackbar.view
    sbView.setBackgroundColor(ContextCompat.getColor(view.context, R.color.blue_700))
    val textView = sbView.findViewById<TextView>(R.id.snackbar_text)
    textView.setTextColor(Color.WHITE)
    snackbar.show()
}

/*
    * Get android version info
    * */
fun currentVersion(): String? {
    val release = Build.VERSION.RELEASE.replace("(\\d+[.]\\d+)(.*)".toRegex(), "$1").toDouble()
    var codeName = "Unsupported" //below Jelly Bean
    when {
        release >= 4.1 && release < 4.4 -> codeName = "Jelly Bean"
        release < 5 -> codeName = "Kit Kat"
        release < 6 -> codeName = "Lollipop"
        release < 7 -> codeName = "Marshmallow"
        release < 8 -> codeName = "Nougat"
        release < 9 -> codeName = "Oreo"
        release < 10 -> codeName = "Pie"
        release >= 10 -> codeName = "Android " + release.toInt()
    }
    return "Manufacturer: ${Build.MANUFACTURER} Model: ${Build.MODEL} Brand: ${Build.BRAND} ID: ${Build.ID} codeName: v${release} API Level: ${Build.VERSION.SDK_INT}"
}