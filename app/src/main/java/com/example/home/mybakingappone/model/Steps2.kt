package com.example.home.mybakingappone.model

import kotlinx.serialization.Serializable

@Serializable
data class Steps2(val id:Int,
val shortDescription:String,
val description:String,
val videoURL:String,
val thumbnailURL:String) {}
