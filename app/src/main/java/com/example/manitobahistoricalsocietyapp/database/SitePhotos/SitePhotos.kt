package com.example.manitobahistoricalsocietyapp.database.SitePhotos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sitePhotos")
data class SitePhotos (
    @PrimaryKey
    @ColumnInfo(name = "photo_id")
    val id: Int,

    @ColumnInfo(name = "site_id")
    val siteId: Int,

    @ColumnInfo(name = "photo_name")
    val name: String?,

    val width: Int,
    val height: Int,

    @ColumnInfo(name = "photo_url")
    val url: String?,

    val info: String?,

    @ColumnInfo(name = "import_date")
    val importDate: String?
)