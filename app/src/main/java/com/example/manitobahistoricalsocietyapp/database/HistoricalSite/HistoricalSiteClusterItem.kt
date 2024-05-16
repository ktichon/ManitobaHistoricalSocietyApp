package com.example.manitobahistoricalsocietyapp.database.HistoricalSite

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class HistoricalSiteClusterItem(
    val id: Int,
    val mainType: Int,
    val name: String,

    val address: String?,
    val municipality: String?,
    val latitude: Double,
    val longitude: Double,
) :ClusterItem {
    override fun getPosition(): LatLng {
        return LatLng(latitude, longitude)
    }

    override fun getTitle(): String? {
        return name
    }

    override fun getSnippet(): String? {
        return (if (address.isNullOrBlank()) "" else "$address, ") + municipality
    }

    override fun getZIndex(): Float? {
        return 0f
    }
}