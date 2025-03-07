package com.TichonTechnologies.manitobahistoricalsocietyapp.database.SiteTypes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "siteType")
data class SiteType(
    @PrimaryKey
    val id: Int,

    val type: String,

    val importDate: String
)
