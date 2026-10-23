package com.example.lab10_2.data

import kotlinx.serialization.Serializable

@Serializable
data class QueryResponse(
    val items: List<BookItem>? = null
)

@Serializable
data class BookItem(
    val id: String
)

@Serializable
data class BookInfo(
    val id: String,
    val volumeInfo: VolumeInfo? = null
) {
    val thumbnailUrl: String?
        get() = volumeInfo?.imageLinks?.thumbnail?.replace("http:", "https:")
}

@Serializable
data class VolumeInfo(
    val title: String? = null,
    val imageLinks: ImageLinks? = null
)

@Serializable
data class ImageLinks(
    val thumbnail: String? = null
)