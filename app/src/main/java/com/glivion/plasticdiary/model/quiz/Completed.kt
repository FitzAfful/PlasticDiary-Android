package com.glivion.plasticdiary.model.quiz

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Completed(
    override var description: String?,
    override var icon: String?,
    override var id: Int,
    override var name: String?,
    override var average: String?
): QuizInterface, Parcelable
