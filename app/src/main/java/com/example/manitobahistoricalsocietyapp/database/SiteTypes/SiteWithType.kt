package com.example.manitobahistoricalsocietyapp.database.SiteTypes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "siteWithType")
data class SiteWithType(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "siteTypeId")
    val typeId: Int,

    val siteId: Int,

    val importDate: String
)
