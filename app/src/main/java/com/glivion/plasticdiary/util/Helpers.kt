package com.glivion.plasticdiary.util

import android.app.Activity
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.util.Linkify
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.room.rxjava3.EmptyResultSetException
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.databinding.ViewMoreInfoDialogBinding
import com.glivion.plasticdiary.view.ui.ViewAllContentTypeActivity
import com.glivion.plasticdiary.view.ui.WebViewActivity
import com.google.android.material.snackbar.Snackbar
import com.tinydavid.deliverymanrider.extensions.Quintuple
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.*
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

fun formatHttpError(e: Throwable): String {
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
    return message
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

    val yesterday = parser.minusDays(1).toLocalDate().toString()

    return Quintuple(currentDate, currentTime, month, day, yesterday)
}


fun getDaysOfTheWeek(): ArrayList<String> {
    val days = ArrayList<String>()
    val calender = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    val firstDay = calender.firstDayOfWeek
    calender.set(Calendar.DAY_OF_WEEK, firstDay)
    val monday = SimpleDateFormat("dd", Locale.getDefault()).format(calender.time).toString()
    days.add(monday)
    for (i in 1 until 7) {
        calender.set(Calendar.DAY_OF_WEEK, firstDay + i)
        val day = SimpleDateFormat("dd", Locale.getDefault()).format(calender.time).toString()
        days.add(day)
    }

    return days
}

fun getToday(): String {
    val calender = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    val today = SimpleDateFormat("dd", Locale.getDefault()).format(calender.time).toString()
    return today
}


fun getDayAndMonth(date: String?): String {

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val localDate = LocalDate.parse(date, formatter)

    val month = localDate.month.getDisplayName(
        TextStyle.SHORT, Locale.US
    )
    val day = localDate.dayOfMonth.toString()

    return "$day\n$month"
}
fun getTimeStamp(): OffsetDateTime {
    val dateTime = DateTimeFormatter
        .ofPattern("yyyy-MM-dd HH:mm:ss")
        .withZone(ZoneOffset.UTC)
        .format(Instant.now())

    return LocalDateTime.parse(dateTime.replace(" ", "T"))
        .atOffset(ZoneOffset.UTC)
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

fun showInWebView(context: Context, url: String?, contentID: Int?, contentType: String) {
    Intent(context, WebViewActivity::class.java).apply {
        putExtra(WEB_VIEW_URL, url)
        putExtra(WEB_VIEW_ID, contentID)
        putExtra(WEB_VIEW_TYPE, contentType)
        context.startActivity(this)
    }
}

fun viewMoreItems(context: Context, contentType: String, data: Bundle) {
    Intent(context, ViewAllContentTypeActivity::class.java).apply {
        putExtra(WEB_VIEW_TYPE, contentType)
        putExtras(data)
        context.startActivity(this)
    }
}

fun openViewMoreInfoDialog(activity: Activity, header: String, body: String) {
    val builder = AlertDialog.Builder(activity, R.style.myFullscreenAlertDialogStyle)
    val binding: ViewMoreInfoDialogBinding = DataBindingUtil.inflate(
        activity.layoutInflater,
        R.layout.view_more_info_dialog,
        null,
        false
    )

    builder.setView(binding.root)
    val dialog = builder.create()
    dialog.setCancelable(true)
    dialog.setCanceledOnTouchOutside(true)

    binding.apply {
        close.setOnClickListener {
            dialog.dismiss()
        }
        heading = header
        content = body
        description.text = body
    }
    Linkify.addLinks(binding.description, Linkify.WEB_URLS)

    dialog.show()
}

fun makeStatusNotification(title: String, message: String, context: Context) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = VERBOSE_NOTIFICATION_CHANNEL_NAME
        val description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = description

        // Add the channel
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager?.createNotificationChannel(channel)
    }

    // Create the notification
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(title)
        .setContentText(message)
        .setStyle(NotificationCompat.BigTextStyle().bigText(message))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(0))

    // Show the notification
    NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
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