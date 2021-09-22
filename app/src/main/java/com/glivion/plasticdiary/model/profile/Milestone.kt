package com.glivion.plasticdiary.model.profile

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Milestone(
    @SerializedName("completed_on")
    var completedOn: String? = null,
    @SerializedName("completed_points")
    var completedPoints: Int? = null,
    var points: Int? = null
) : Parcelable
