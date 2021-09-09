package com.glivion.plasticdiary.model.profile


import com.google.gson.annotations.SerializedName

data class BaseLeaderboardObject(
    var leaderboard: Leaderboard? = null
) {
    data class Leaderboard(
        @SerializedName("all_time")
        var allTime: List<AllTime>? = null,
        @SerializedName("this_week")
        var thisWeek: List<ThisWeek>? = null
    )
}