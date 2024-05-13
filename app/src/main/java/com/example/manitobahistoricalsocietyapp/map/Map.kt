package com.example.manitobahistoricalsocietyapp.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.manitobahistoricalsocietyapp.R
import com.example.manitobahistoricalsocietyapp.database.HistoricalSite.HistoricalSite
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.clustering.Clustering

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun DisplayMap(

    cameraPositionState: CameraPositionState,
    sites: List<HistoricalSite>,
    onClusterItemClick: (HistoricalSite)  -> Unit,
    modifier: Modifier = Modifier
) {


    GoogleMap(
        cameraPositionState = cameraPositionState,
        modifier = modifier
    ){
        Clustering(
            items = sites,
            onClusterItemClick = {
                onClusterItemClick(it)
                                 false
                                 },
            clusterItemContent = {item ->
                Marker(
                    title = item.title,
                    snippet = item.snippet,
                    icon = setMarkerIcon(item.id)


                )
            }
        )
    }

}


//Used to set the different icons for the main site type
//currently it only returns one type
private fun setMarkerIcon(typeID: Int): BitmapDescriptor {

    return BitmapDescriptorFactory.fromResource(R.drawable.site_marker)

}








