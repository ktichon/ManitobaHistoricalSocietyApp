package com.example.manitobahistoricalsocietyapp.database.SitePhotos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sitePhotos")
data class SitePhotos (
    @PrimaryKey
    val id: Int,

    val siteId: Int,

    @ColumnInfo(name = "photoName")
    val name: String,

    val width: Int,
    val height: Int,

    @ColumnInfo(name = "photoUrl")
    val url: String,

    val info: String?,

    val importDate: String
)