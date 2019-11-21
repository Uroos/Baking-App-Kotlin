package com.example.home.mybakingappone.model

import kotlinx.serialization.Serializable

@Serializable
data class Ingredients2(val quantity: Double, val measure: String, val ingredient: String) : java.io.Serializable {
}