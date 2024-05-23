package com.example.manitobahistoricalsocietyapp.helperClasses

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import kotlin.math.roundToInt

//Displays the text for the distance between two latlng
class DistanceAwayFromSite {


    companion object{
        fun getDisplayDistance(latLng1: LatLng, latLng2: LatLng): String{
            val distanceInMeters = getDistanceInMeters(latLng1, latLng2)

            val displayDistance = if (distanceInMeters > 100000) (distanceInMeters/1000).roundToInt().toString() + " km"
            //When meters is greater than 10 km
            else if (distanceInMeters > 10000)  "%.1f".format (distanceInMeters/1000) + " km"
            //When meters is greater than 1 km
            else if (distanceInMeters >= 1000) "%.2f".format (distanceInMeters/1000) + " km"
            //When meters is less than 1 km
            else distanceInMeters.roundToInt().toString() + " m"

            return "$displayDistance away"

        }

        fun getDistanceInMeters(latLng1: LatLng, latLng2: LatLng): Float{
            return turnLatLongIntoLocation(latLng1).distanceTo(turnLatLongIntoLocation(latLng2))
        }

        //Turns latLng into location, so that we can use Location.distanceTo(Location)
        private fun turnLatLongIntoLocation(latLng: LatLng): Location {
            val location = Location("")
            location.latitude = latLng.latitude
            location.longitude = latLng.longitude
            return location
        }

    }
}