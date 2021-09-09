package com.glivion.plasticdiary.model.profile

data class AllTime(
    override var name: String,
    override var score: String,
    override var img_url: String
) : LeaderboardInterface
