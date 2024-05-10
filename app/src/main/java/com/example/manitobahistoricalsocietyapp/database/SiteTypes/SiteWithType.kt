package com.example.manitobahistoricalsocietyapp.database.SiteTypes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "siteWithType")
data class SiteWithType(
    @PrimaryKey
    @ColumnInfo(name = "site_with_type_id")
    val id: Int,

    @ColumnInfo(name = "site_type_id")
    val typeId: Int,

    @ColumnInfo(name = "site_id")
    val siteId: Int,

    @ColumnInfo(name = "import_date")
    val importDate: String
)
