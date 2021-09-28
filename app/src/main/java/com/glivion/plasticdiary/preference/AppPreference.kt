package com.glivion.plasticdiary.preference

import android.content.Context
import android.content.SharedPreferences
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.model.Streak
import com.glivion.plasticdiary.model.User
import com.glivion.plasticdiary.util.AUTH_USER
import com.glivion.plasticdiary.util.CURRENT_STREAK
import com.glivion.plasticdiary.util.getStringFromRes
import com.google.gson.Gson
import javax.inject.Inject

class AppPreference @Inject constructor(context: Context) {
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences(getStringFromRes(context, R.string.app_name),
            Context.MODE_PRIVATE
        )
    private var editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveUser(userJson: String) {
        editor.putString(AUTH_USER, userJson).apply()
    }

    fun saveStreak(streak: String) {
        editor.putString(CURRENT_STREAK, streak).apply()
    }

    fun getStreak(): Streak? {
        val streak = sharedPreferences.getString(CURRENT_STREAK, null)
        return Gson().fromJson(streak, Streak::class.java)
    }

    fun getUser(): User? {
        val userJson = sharedPreferences.getString(AUTH_USER, null)
        return Gson().fromJson(userJson, User::class.java)
    }

    fun deleteToken() {
        editor.remove(AUTH_USER).apply()
    }

}