package com.TichonTechnologies.manitobahistoricalsocietyapp.database.SitePhotos

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

    val infoHTML: String?,
    val infoMarkdown: String?,

    val importDate: String
)