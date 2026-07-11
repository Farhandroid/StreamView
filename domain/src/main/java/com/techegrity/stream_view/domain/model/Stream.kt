package com.techegrity.stream_view.domain.model

data class Stream(
    val id: String,
    val name: String,
    val thumbnailUrl: String,
    val streamUrl: String,
    val isConnected: Boolean,
    val provider: String,
    val metadata: String,
)
