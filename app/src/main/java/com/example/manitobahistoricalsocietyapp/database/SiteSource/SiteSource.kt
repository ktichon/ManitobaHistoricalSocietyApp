package com.example.manitobahistoricalsocietyapp.database.SiteSource

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "siteSource")
data class SiteSource(
    @PrimaryKey
    val id: Int,

    val siteId: Int,

    val infoHTML: String,
    val infoMarkdown: String,

    val importDate: String
)
