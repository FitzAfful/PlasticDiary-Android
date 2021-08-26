package com.engineerskasa.plasticdiary.preference

import android.content.Context
import android.content.SharedPreferences
import com.engineerskasa.plasticdiary.R
import com.engineerskasa.plasticdiary.model.User
import com.engineerskasa.plasticdiary.util.AUTH_USER
import com.engineerskasa.plasticdiary.util.getStringFromRes
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

    fun getUser(): User? {
        val userJson = sharedPreferences.getString(AUTH_USER, null)
        return Gson().fromJson(userJson, User::class.java)
    }

}