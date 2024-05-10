package com.example.manitobahistoricalsocietyapp.database.SiteTypes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "siteType")
data class SiteType(
    @PrimaryKey
    @ColumnInfo(name = "site_type_id")
    val id: Int,

    val type: String,

    @ColumnInfo(name = "import_date")
    val importDate: String
)
