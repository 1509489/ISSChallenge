package com.pixelarts.isschallenge.model

data class APIResponse(
    val message: String,
    val request: Request,
    val response: List<Response>
)
