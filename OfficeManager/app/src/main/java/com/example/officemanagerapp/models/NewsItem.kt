package com.example.officemanagerapp.models

import android.os.Parcelable
import com.example.officemanagerapp.database.CacheNewsItem
import com.example.officemanagerapp.util.smartTruncate
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewsItem(
    val title: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
): Parcelable {
    val shortDescription: String
        get() = description.smartTruncate(200)
}

data class NetworkNewsItem(
    val title: String?,
    val description: String?,
    val photoUrl: String?,
    val createdAt: String?,
)

fun List<NetworkNewsItem>.asCacheModel(): Array<CacheNewsItem> {
    return map {
        CacheNewsItem(
            title = it.title ?: "",
            description = it.description ?: "",
            photoUrl = it.photoUrl ?: "",
            createdAt = it.createdAt ?: "",
        )
    }.toTypedArray()
}