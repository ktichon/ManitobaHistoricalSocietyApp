package com.example.manitobahistoricalsocietyapp.database.SiteSource

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "siteSource")
data class SiteSource(
    @PrimaryKey
    @ColumnInfo(name = "source_id")
    val id: Int,

    @ColumnInfo(name = "site_id")
    val siteId: Int,

    val info: String,

    @ColumnInfo(name = "import_date")
    val importDate: String
)
