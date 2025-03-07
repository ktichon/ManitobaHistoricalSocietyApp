package com.TichonTechnologies.manitobahistoricalsocietyapp.database.HistoricalSite

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng


@Entity(tableName = "historicalSite" )
data class HistoricalSite (
    @PrimaryKey
    val id: Int,
    val name: String,

    val address: String?,

    val mainType: Int,
    val latitude: Double,
    val longitude: Double,

    val municipality: String?,
    val province: String?,
    val descriptionHTML: String?,
    val descriptionMarkdown: String?,

    val siteUrl: String,

    val keywords: String?,

    val importDate: String,
){

    fun getPosition(): LatLng {
        return LatLng(latitude, longitude)
    }



}




