package com.pixelarts.isschallenge.model

data class Request(
    val altitude: Int,
    val datetime: Int,
    val latitude: Double,
    val longitude: Double,
    val passes: Int
)