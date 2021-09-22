package com.glivion.plasticdiary.model.profile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Badges(
    var description: String? = null,
    var icon: String? = null,
    var id: Int? = null,
    var milestones: List<Milestone?>? = null,
    var name: String? = null
) : Parcelable
