package com.TichonTechnologies.manitobahistoricalsocietyapp.database.HistoricalSite

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class HistoricalSiteClusterItem(
    val id: Int,
    val name: String,
    val address: String?,
    val mainType: Int,
    val latitude: Double,
    val longitude: Double,
    val municipality: String?,
) :ClusterItem {
    override fun getPosition(): LatLng {
        return LatLng(latitude, longitude)
    }

    override fun getTitle(): String? {
        return null //name
    }

    override fun getSnippet(): String? {
        return null // (if (address.isNullOrBlank()) "" else "$address, ") + municipality
    }

    override fun getZIndex(): Float {
        return 0f
    }
    //Gets the name and the address together for easy searching
    fun getNameAndAddress(): String{
        return "$name, $address"
    }
}