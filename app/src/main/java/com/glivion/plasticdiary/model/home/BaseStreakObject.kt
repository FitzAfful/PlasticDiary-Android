package com.glivion.plasticdiary.model.home


import com.google.gson.annotations.SerializedName

class BaseStreakObject : ArrayList<BaseStreakObject.BaseStreakObjectItem>(){
    data class BaseStreakObjectItem(
        @SerializedName("max_streak")
        var maxStreak: Int? = null
    )
}