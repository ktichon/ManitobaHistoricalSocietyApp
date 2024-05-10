package com.example.manitobahistoricalsocietyapp.map

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.manitobahistoricalsocietyapp.database.HistoricalSite.HistoricalSite
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState

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
        )
    }

}






