package com.example.manitobahistoricalsocietyapp.database.HistoricalSite

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng


@Entity(tableName = "manitobaHistoricalSite" )
data class HistoricalSite (
    @PrimaryKey
    @ColumnInfo(name = "site_id")
    val id: Int,
    val name: String,

    val address: String?,

    @ColumnInfo(name = "main_type")
    val mainType: Int,
    val latitude: Double,
    val longitude: Double,

    val municipality: String?,
    val province: String?,
    val description: String?,

    @ColumnInfo(name = "site_url")
    val siteUrl: String,

    val keywords: String?,

    @ColumnInfo(name = "import_date")
    val importDate: String,
){

    fun getPosition(): LatLng {
        return LatLng(latitude, longitude)
    }



}




