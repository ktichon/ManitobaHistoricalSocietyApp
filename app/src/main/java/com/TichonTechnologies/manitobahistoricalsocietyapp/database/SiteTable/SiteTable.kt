package com.TichonTechnologies.manitobahistoricalsocietyapp.database.SiteTable

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "siteTable")
data class SiteTable(
    @PrimaryKey
    val id: Int,

    val siteId: Int,
    val name: String?,
    val numOfColumns: Int,


    val contentHTML: String,
    val contentMarkdown: String,

    val importDate: String
)
