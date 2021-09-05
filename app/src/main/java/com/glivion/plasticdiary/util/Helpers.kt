package com.glivion.plasticdiary.util

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
import androidx.room.rxjava3.EmptyResultSetException
import com.glivion.plasticdiary.R
import com.google.android.material.snackbar.Snackbar
import com.tinydavid.deliverymanrider.extensions.Quintuple
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

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

fun showErrorMessage(view: View, e: Throwable) {
    var message = ""
    try {
        when (e) {
            is IOException -> {
                message = "No internet connection: ${e.message}"
            }
            is HttpException -> {
                val errorBody = e.response()!!.errorBody()!!.string()
                val jsonObject = JSONObject(errorBody)
                message = when (e.code()) {
                    404 -> "Not found"
                    500 -> "An unexpected error occurred... please try again later"
                    400 -> jsonObject.getString("error")
                    else -> jsonObject.getString("message")
                }
            }
            is EmptyResultSetException -> {
                message = e.message.toString()
            }
        }
    } catch (e1: IOException) {
        e1.printStackTrace()
    } catch (e1: JSONException) {
        e1.printStackTrace()
    } catch (e1: Exception) {
        e1.printStackTrace()
    }
    if (message.isEmpty()) {
        message = "Cannot retrieve info at this time. Please try again later"
    }
    val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
    val sbView = snackbar.view
    sbView.setBackgroundColor(ContextCompat.getColor(view.context, R.color.blue_700))
    val textView = sbView.findViewById<TextView>(R.id.snackbar_text)
    textView.setTextColor(Color.WHITE)
    snackbar.show()
}

fun getCurrentDateTimeParams(): Quintuple<String, String, String, String, String> {
    val dateTime = DateTimeFormatter
        .ofPattern("yyyy-MM-dd HH:mm:ss")
        .withZone(ZoneOffset.UTC)
        .format(Instant.now())

    val parser = LocalDateTime.parse(dateTime.replace(" ", "T"))
        .atOffset(ZoneOffset.UTC)

    val month = parser.month.getDisplayName(
        TextStyle.FULL, Locale.US
    )

    val day = parser.dayOfMonth.toString()

    val dayInWords = parser.dayOfWeek.toString()

    val currentDate = parser.toLocalDate().toString()

    val currentTime = parser.toLocalTime().toString()

    return Quintuple(currentDate, currentTime, month, day, dayInWords)
}

fun getYoutubeVideoIdFromUrl(url: String): String? {

    val inUrl = url.replace("&feature=youtu.be", "")
    if (inUrl.lowercase().contains("youtu.be")) {
        return inUrl.substring(inUrl.lastIndexOf("/") + 1)
    }
    val pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
    val compiledPattern: Pattern = Pattern.compile(pattern)
    val matcher: Matcher = compiledPattern.matcher(inUrl)
    return if (matcher.find()) {
        matcher.group()
    } else null
}

fun getYoutubeThumbnailUrlFromVideoUrl(videoUrl: String?): String {
    return "https://img.youtube.com/vi/${getYoutubeVideoIdFromUrl(videoUrl!!)}/0.jpg"
}


// Get android version info
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