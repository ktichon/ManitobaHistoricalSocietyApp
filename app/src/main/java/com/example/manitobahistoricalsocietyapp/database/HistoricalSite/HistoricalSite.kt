package com.example.manitobahistoricalsocietyapp.database.HistoricalSite

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "manitobaHistoricalSite" )
data class HistoricalSite (
    @PrimaryKey
    @ColumnInfo(name = "site_id")
    val id: Int,
    val name: String?,
    val address: String?,
    @ColumnInfo(name = "main_type")
    val mainType: Int,

    val latitude: Double,

    val longitude: Double,
    val province: String?,

    val municipality: String?,
    val description: String?,

    @ColumnInfo(name = "site_url")
    val siteUrl: String?,

    val keywords: String?,

    @ColumnInfo(name = "import_date")
    val importDate: String?,
)




